package app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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

	/*
	 * ホーム画面
	 */
	@GetMapping
	public String homePage(Model model) {
		model.addAttribute("title", "さあ、始めよう");
		return "home";
	}

	/*
	 * 文章投稿画面
	 */
	@GetMapping("/form")
	public String formPage(Model model) {
		Play play = playService.getText();
		model.addAttribute("title", "文章を入力してください");
		model.addAttribute("play", play);
		return "form";
	}

	/*
	 * 結果表示画面
	 */
	@GetMapping("/result")
	public String resultPage(
			@Valid @ModelAttribute PlayForm playForm,
			BindingResult result,
			Model model) {
		if (!result.hasErrors()) {
			//play.setText(playForm.getText());
			float score = detectSentiment.amazonComprehend(playForm.getText());
			model.addAttribute("score", score);
			return "redirect:/play/result";
		} else {
			model.addAttribute("title", "文章を入力し直してください");
			return "form";
		}

	}

}