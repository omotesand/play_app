package app.controller;

import java.math.BigDecimal;

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
	public String formPage(Model model) {
		Play play = playService.findChallenge();   //DBからお題を取得
		session.setAttribute("challenge", play);  //お題をセッションへ保存
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
			RedirectAttributes redirectAttributes) {
		if (!result.hasErrors()) {
			//play.setText(playForm.getText());
			//DBへ投稿文章を登録する処理を追記
			redirectAttributes.addFlashAttribute("input", playForm.getInput()); //フォームへの投稿をフラッシュスコープへ格納
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
			@ModelAttribute("input") String input,
			Model model) {
		//float score = detectSentiment.amazonComprehend(text);
		//String score = text;
		Play play = (Play)session.getAttribute("challenge"); //セッション情報を取得
		String challenge = play.getChallenge();
		String score = challenge + input;
		model.addAttribute("score", score);
		session.invalidate();
		return "result";
	}

	/*
	 * 投稿結果のランキングをグラフで見る
	 */
	@GetMapping("/chart")
	public String showRankingByChart(Model model) {
		String label[] = {"a", "b", "c"};
		//BigDecimal score[] = {0.99, 0.85, 0.61};
		BigDecimal score[] = {BigDecimal.valueOf(0.99), BigDecimal.valueOf(0.85), BigDecimal.valueOf(0.61)};
		model.addAttribute("label", label);
		model.addAttribute("score", score);
		return "chart";
	}
}