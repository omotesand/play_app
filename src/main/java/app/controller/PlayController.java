package app.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
		model.addAttribute("title", "ネガポジ バトル");
		return "home";
	}

	/*
	 * フォーム画面
	 */
	@GetMapping("/form")
	public String formPage(PlayForm playForm, Model model) {
		//使わない
		//Play play = playService.findChallenge();  //challengesテーブルのレコードをランダムに一つ取得

		List<Play> challengesList = playService.findAll();

		//使わない
		//session.setAttribute("challenges", play); //challengesテーブルのレコードををセッションへ保存

		model.addAttribute("title", "お題に続く文章を投稿して感情分析しよう！");
		model.addAttribute("challengesList", challengesList);
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
			session.setAttribute("playForm", playForm); //フォームオブジェクトをセッションへ格納
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
			//@ModelAttribute("playForm") PlayForm playForm,  //フラッシュスコープのplayFormを引数で受け取る
			Model model) {
		//-----セッションの値を取り出す処理（challengeはテーブル名ではなくPlay型のオブジェクトであることに注意）-----
		//Play     challenges  = (Play)session.getAttribute("challenges");//セッションへ保存されたchallengesテーブルのレコードを取得
		//int      challengeId = challenges.getChallengeId();             //challengesテーブルのレコードからお題IDを取得
		//String   challenge   = challenges.getChallenge();               //challengesテーブルのレコードからお題のテキストを取得
		PlayForm playForm              = (PlayForm)session.getAttribute("playForm");//セッションに保存されたフォームオブジェクトを取得
		int      analyzedChallengeId   = playForm.getCurrentChallengeId();          //フォームで選択したお題IDを取得
		int      analyzedSentimentType = playForm.getSentimentType();               //フォームで選択した感情タイプを取得

		//-----セッションの値をEntityに詰める（findByIdのSQLでバインド変数を使うため）-----
		Play playForFindById = new Play();
		playForFindById.setCurrentChallengeId(analyzedChallengeId);

		//-----フォームで選択されたお題IDに紐づくお題オブジェクトををchallengesテーブルから取得-----
		Play analyzedChallenge = playService.findById(analyzedChallengeId);

		//-----Amazon Comprehendで感情分析する-----
		String challenge             = analyzedChallenge.getChallenge();        //お題
		String analyzedSentimentText = challenge + playForm.getInput();         //「お題」＋「投稿」のテキスト
		BigDecimal score = detectSentiment
				.amazonComprehend(analyzedSentimentType, analyzedSentimentText) //Amazon Comprehendで感情分析スコア算出
				.setScale(3, RoundingMode.HALF_UP);                             //小数第3位四捨五入

		//-----chart.htmlで順位表示のSQLを使うため、セッションに保存-----
		session.setAttribute("score", score);

		//-----inputsテーブルへのデータ登録のためにEntityに詰める処理-----
		Play playForInsert = new Play();
		playForInsert.setCurrentChallengeId(analyzedChallengeId);//inputsテーブルのcurrent_challenge_idに値をセット
		playForInsert.setSentimentType(analyzedSentimentType);   //FormからEntityへ感情タイプの詰め替え
		playForInsert.setInput(playForm.getInput());             //FormからEntityへ投稿内容の詰め替え
		playForInsert.setScore(score);                           //感情分析スコアをセット

		//-----最後まとめ-----
		playService.insert(playForInsert); //DBへinsert
		model.addAttribute("analyzedSentimentText", analyzedSentimentText);
		model.addAttribute("score", score);
		return "result";
	}

	/*
	 * 投稿結果のランキングをグラフで見る
	 */
	@GetMapping("/chart")
	public String showRankingByChart(Model model) {
		//-----セッションの値をEntityに詰める-----
		PlayForm   playForm = (PlayForm)session.getAttribute("playForm"); //セッションに保存されたplayFormオブジェクトを取得
		BigDecimal score    = (BigDecimal)session.getAttribute("score");  //セッションに保存されたスコアを取得
		Play play = new Play();
		play.setCurrentChallengeId(playForm.getCurrentChallengeId());     //お題IDをEntityへ詰める
		play.setSentimentType(playForm.getSentimentType());               //感情タイプをEntityへ詰める
		play.setScore(score);                                             //スコアをEntityへ詰める

		//-----DBからスコア上位を取得-----
		List<Play> showResultList  = playService.findRank(play); //スコア上位5位のリストを取得
		Play yourRank = playService.findYourRank(play);          //スコアの順位を取得

		//-----DBから取得した値を詰めるためのリストをつくる-----
		List<String>     inputList = new ArrayList<String>();     //投稿を格納する空リストを生成
		List<BigDecimal> scoreList = new ArrayList<BigDecimal>(); //スコアを格納する空リストを生成

		//-----DBから取得した値を空リストに詰める-----
		for(int i = 0; i < 5; i++) {
			inputList.add(showResultList.get(i).getInput());
			scoreList.add(showResultList.get(i).getScore());
		}

		//-----値を詰めたリストを配列に変換-----
		String[]     inputArray = inputList.toArray(new String    [inputList.size()]);
		BigDecimal[] scoreArray = scoreList.toArray(new BigDecimal[scoreList.size()]);

		//-----最後まとめ-----
		model.addAttribute("inputArray", inputArray); //グラフのラベル（縦軸）
		model.addAttribute("scoreArray", scoreArray); //スコア（横軸）
		model.addAttribute("id", playForm.getCurrentChallengeId()); //あとで消す
		model.addAttribute("type", playForm.getSentimentType()); //あとで消す
		model.addAttribute("yourRank", yourRank.getYourRank()); //あとで消す
		session.invalidate();                         //セッションを切断
		return "chart";
	}
}