package app.controller;

import java.math.BigDecimal;
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
			//DBへ投稿文章を登録する処理を追記
			//redirectAttributes.addFlashAttribute("sentimentType", playForm.getSentimentType()); //感情タイプの選択をフラッシュスコープへ格納
			//redirectAttributes.addFlashAttribute("input",         playForm.getInput()        ); //フォームへの投稿をフラッシュスコープへ格納
			redirectAttributes.addFlashAttribute("playForm", playForm);
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
			@ModelAttribute("playForm") PlayForm playForm,
			Model model) {
		//float score = detectSentiment.amazonComprehend(text);
		//-----セッションの値を取り出す処理（challengeはテーブル名ではなくPlay型のオブジェクトであることに注意）-----
		Play challenges = (Play)session.getAttribute("challenges");//セッションへ保存されたchallengesテーブルのレコードを取得
		int    challengeId = challenges.getChallengeId();          //challengesテーブルのレコードからお題IDを取得
		String challenge   = challenges.getChallenge();            //challengesテーブルのレコードからお題のテキストを取得

		//-----inputsテーブルへのデータ登録のためにEntityに詰め替える処理-----
		Play play = new Play();
		play.setCurrentChallengeId(challengeId);                  //inputsテーブルのcurrent_challenge_idに値をセット
		play.setSentimentType(playForm.getSentimentType());       //FormからEntityへ感情タイプの詰め替え
		play.setInput(playForm.getInput());                       //FormからEntityへ投稿内容の詰め替え
		play.setScore(BigDecimal.valueOf(0.57));

		//-----最後まとめ-----
		playService.insert(play);                                  //DBへinsert
		String sentimentAnalyzed = challenge + playForm.getInput();//「お題のテキスト」＋「フォームへの投稿」
		model.addAttribute("sentimentAnalyzed", sentimentAnalyzed);
		session.invalidate();                                      //セッションを切断
		return "result";
	}

	/*
	 * 投稿結果のランキングをグラフで見る
	 */
	@GetMapping("/chart")
	public String showRankingByChart(Model model) {
		String[] label = {"a", "b", "c"};
		//BigDecimal[] foo = {BigDecimal.valueOf(0.97), BigDecimal.valueOf(0.85), BigDecimal.valueOf(0.61)};
		List<BigDecimal> scoreList  = playService.findScore();                             //DBからスコアのListを取得
		BigDecimal[]     scoreArray = scoreList.toArray(new BigDecimal[scoreList.size()]); //BigDecimal型の配列に変換
		model.addAttribute("label", label);
		model.addAttribute("scoreArray", scoreArray);
		return "chart";
	}
}