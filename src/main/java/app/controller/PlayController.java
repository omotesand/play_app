package app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.entity.Play;
import app.form.PlayForm;
import app.service.DetectSentiment;
import app.service.PlayService;

@Controller
@RequestMapping("/play")
public class PlayController {
	//---------------------------------------------
	//ServiceクラスのDI化
	//---------------------------------------------
	private final PlayService playService;
	private final DetectSentiment detectSentiment;

	@Autowired
	public PlayController(PlayService playService, DetectSentiment detectSentiment) {
		this.playService = playService;
		this.detectSentiment = detectSentiment;
	}

	@Autowired
	HttpSession session;

	/*
	 * ホーム画面
	 */
	@GetMapping
	public String homePage(Model model) {
		model.addAttribute("title", "さあ、始めよう");
		return "home";
	}

	/*
	 * フォーム画面
	 */
	@GetMapping("/form")
	public String formPage(PlayForm playForm, Model model) {
		Play play = playService.findChallenge();  //challengesテーブルのレコードをランダムに一つ取得
		session.setAttribute("challenges", play); //challengesテーブルのレコードををセッションへ保存
		model.addAttribute("title", "文章を入力してください");
		model.addAttribute("play", play);
		return "form";
	}

	/*
	 * 投稿文章のValidation結果による振り分け
	 * Validation OK：結果確認画面へ
	 * Validation NG：再度フォーム画面へ
	 */
	@PostMapping("/judge")
	public String judgeText(
			@Valid @ModelAttribute PlayForm playForm,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes
			) {
		if (!result.hasErrors()) {
			//redirectAttributes.addFlashAttribute("playForm", playForm); //フォームオブジェクトをフラッシュスコープへ格納
			session.setAttribute("playForm", playForm);
			return "redirect:/play/result";
		} else {
			model.addAttribute("title", "文章を入力し直してください");
			return "form";
		}
	}

	/*
	 * 投稿文章Validation：OKの場合の結果
	 */
	@GetMapping("/result")
	public String resultPage(
			//@ModelAttribute("sentimentType") int    sentimentType,   //viewで選択した感情タイプをcontrollerで取得
			//@ModelAttribute("input")         String input,           //view(フォームへの投稿)をcontrollerで取得
			//@ModelAttribute("playForm") PlayForm playForm,  //フラッシュスコープのplayFormを引数で受け取る
			Model model) {
		//-----セッションの値を取り出す処理（challengeはテーブル名ではなくPlay型のオブジェクトであることに注意）-----
		Play challenges = (Play)session.getAttribute("challenges");//セッションへ保存されたchallengesテーブルのレコードを取得
		int    challengeId = challenges.getChallengeId();          //challengesテーブルのレコードからお題IDを取得
		String challenge   = challenges.getChallenge();            //challengesテーブルのレコードからお題のテキストを取得

		//-----Amazon Comprehendで感情分析する-----
		PlayForm playForm = (PlayForm)session.getAttribute("playForm");
		int    analyzedSentimentType = playForm.getSentimentType();            //フォームで選択した感情タイプを取得
		String analyzedSentimentText = challenge + playForm.getInput();        //「お題」＋「投稿」のテキスト
		BigDecimal score = detectSentiment
				.amazonComprehend(analyzedSentimentType, analyzedSentimentText);//Amazon Comprehendで感情分析スコア算出

		//-----inputsテーブルへのデータ登録のためにEntityに詰める処理-----
		Play play = new Play();
		play.setCurrentChallengeId(challengeId);            //inputsテーブルのcurrent_challenge_idに値をセット
		play.setSentimentType(playForm.getSentimentType()); //FormからEntityへ感情タイプの詰め替え
		play.setInput(playForm.getInput());                 //FormからEntityへ投稿内容の詰め替え
		play.setScore(score);                               //感情分析スコアをセット

		//-----最後まとめ-----
		playService.insert(play);                                    //DBへinsert
		model.addAttribute("analyzedSentimentText", analyzedSentimentText);
		model.addAttribute("score", score);
		//session.invalidate();                                        //セッションを切断
		return "result";
	}

	/*
	 * 投稿結果のランキングをグラフで見る
	 */
	@GetMapping("/chart")
	public String showRankingByChart(Model model) {
		//String[] label = {"a", "b", "c"};
		//BigDecimal[] foo = {BigDecimal.valueOf(0.97), BigDecimal.valueOf(0.85), BigDecimal.valueOf(0.61)};
		Play challenges = (Play)session.getAttribute("challenges");
		PlayForm playForm = (PlayForm)session.getAttribute("playForm");
		Play play = new Play();
		play.setCurrentChallengeId(challenges.getChallengeId());
		play.setSentimentType(playForm.getSentimentType());

		List<Play> showResultList  = playService.findRank();
		List<String> inputList = new ArrayList<String>();
		List<BigDecimal> scoreList = new ArrayList<BigDecimal>();
		for(int i = 0; i < 5; i++) {
			inputList.add(showResultList.get(i).getInput());
			scoreList.add(showResultList.get(i).getScore());
		}
//		BigDecimal[]     scoreArray = scoreList.toArray(new BigDecimal[scoreList.size()]); //BigDecimal型の配列に変換
		String[]     inputArray = inputList.toArray(new String    [inputList.size()]);
		BigDecimal[] scoreArray = scoreList.toArray(new BigDecimal[scoreList.size()]);
		model.addAttribute("inputArray", inputArray);//ラベル
		model.addAttribute("scoreArray", scoreArray);//値
		return "chart";
	}
}