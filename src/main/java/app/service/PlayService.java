package app.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
	public List<BigDecimal> findScore() {
		//BigDecimal[] scoreList = dao.findScore();
		//List<Map<String, Object>> dbScoreList = dao.findScore();
		List<Play> dbScoreList = dao.findScore();
		List<BigDecimal> scoreList = new ArrayList<BigDecimal>();
		for(int i = 0; i < 3; i++) {
			//Play play = new Play();
			scoreList.add(dbScoreList.get(i).getScore());

		}
		return scoreList;
	}

}