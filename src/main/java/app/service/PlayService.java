package app.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.Play;
import app.repository.PlayDao;

@Service
public class PlayService {

	private final PlayDao dao;

	@Autowired
	public PlayService(PlayDao dao) {
		this.dao = dao;
	}

	/*
	 * challengesテーブルからお題をひとつ取得するメソッド
	 */
	public Play findChallenge() {
		return dao.findChallenge();
	}

	/*
	 * inputsテーブルからスコアを取得するメソッド
	 */
	public BigDecimal[] findScore() {
		//BigDecimal[] scoreList = dao.findScore();
		//BigDecimal[] scoreList = new BigDecimal[3];
		//for(int i = 0; i <= 3; i++) {
		//}
		return dao.findScore();
	}

}