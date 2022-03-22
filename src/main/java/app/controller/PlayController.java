package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.entity.Play;
import app.service.PlayService;

@Controller
@RequestMapping("/play")
public class PlayController {

	private final PlayService playService;

	@Autowired
	public PlayController(PlayService playService) {
		this.playService = playService;
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
		model.addAttribute("play", play);
		return "form";
	}

	/*
	 * 結果表示画面
	 */
	@GetMapping("/result")

}