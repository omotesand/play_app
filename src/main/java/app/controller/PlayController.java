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
import app.form.TypeForm;
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
		model.addAttribute("title", "感情分析アプリ");
		return "home";
	}

	/*
	 * 感情タイプ選択画面
	 */
	@GetMapping("/sentiment")
	public String typePage(PlayForm playForm, Model model) {
		model.addAttribute("title", "感情タイプを選択しよう！");
		return "chart";
	}

	/*
	 * ユーザーが選択した感情タイプによりお題を選別
	 */
	@PostMapping("/type")
	public String selectType(
			@Valid @ModelAttribute TypeForm typeForm,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes
			) {
		if (!result.hasErrors()) {
			session.setAttribute("typeForm", typeForm); //フォームオブジェクトをセッションへ格納
			return "redirect:/play/form";
		} else {
			model.addAttribute("title", "文章を入力し直してください");
			return "chart";
		}
	}

	/*
	 * フォーム画面
	 */
	@GetMapping("/form")
	public String formPage(PlayForm playForm, Model model) {
		//使わない
		//Play play = playService.findChallenge();  //challengesテーブルのレコードをランダムに一つ取得
		//session.setAttribute("challenges", play); //challengesテーブルのレコードををセッションへ保存

		TypeForm typeForm              = (TypeForm)session.getAttribute("typeForm");
		int      analyzedSentimentType   = typeForm.getYourSentimentType();
		Play play = new Play();
		play.setYourSentimentType(analyzedSentimentType);
		List<Play> challengesList = playService.findAllForSelectedSentimentType(analyzedSentimentType);
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
		TypeForm typeForm              = (TypeForm)session.getAttribute("typeForm");//セッションに保存されたtypeFormオブジェクトを取得
		PlayForm playForm              = (PlayForm)session.getAttribute("playForm");//セッションに保存されたplayFormオブジェクトを取得
		int      analyzedSentimentType = typeForm.getYourSentimentType();           //typeFormで選択した感情タイプを取得
		int      analyzedChallengeId   = playForm.getYourChallengeId();             //playFormで選択したお題IDを取得

		//-----セッションの値をEntityに詰める（findByIdのSQLでバインド変数を使うため）-----
		Play playForFindById = new Play();
		playForFindById.setYourChallengeId(analyzedChallengeId);

		//-----フォームで選択されたお題IDに紐づくお題オブジェクトををchallengesテーブルから取得-----
		Play analyzedChallenge = playService.findById(analyzedChallengeId);

		//-----Amazon Comprehendで感情分析する-----
		String challenge             = analyzedChallenge.getChallenge();        //お題
		String analyzedSentimentText = challenge + playForm.getInput();         //「お題」＋「投稿」のテキスト
		BigDecimal score = detectSentiment
				.amazonComprehend(analyzedSentimentType, analyzedSentimentText) //Amazon Comprehendで感情分析スコア算出
				.setScale(3, RoundingMode.HALF_UP);                             //小数第4位四捨五入

		//-----inputsテーブルへのデータ登録、および順位表示のためEntityに詰める処理-----
		Play play = new Play();
		play.setYourChallengeId(analyzedChallengeId);//inputsテーブルのcurrent_challenge_idに値をセット
		play.setYourSentimentType(analyzedSentimentType);   //FormからEntityへ感情タイプの詰め替え
		play.setInput(playForm.getInput());             //FormからEntityへ投稿内容の詰め替え
		play.setScore(score);                           //感情分析スコアをセット

		//-----SQL発行-----
		playService.insert(play);                                    //DBへinsert
		List<Play> showResultList = playService.findRank(play);      //スコア上位5位のリストを取得
		Play       yourRank       = playService.findYourRank(play);  //スコアの順位を取得
		Play       totalCount     = playService.findTotalCount(play);//投稿した部門のレコード総数取得

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
		model.addAttribute("analyzedSentimentText", analyzedSentimentText); //分析した文章
		model.addAttribute("score", score);                                 //結果のスコア
		model.addAttribute("yourRank", yourRank.getYourRank());             //ただいまの順位
		model.addAttribute("totalCount", totalCount.getTotalCount());       //投稿部門のレコード総数
		model.addAttribute("inputArray", inputArray);                       //グラフのラベル（縦軸）
		model.addAttribute("scoreArray", scoreArray);                       //グラフのスコア（横軸）
		session.invalidate();                                               //セッションを切断
		return "result";
	}
}