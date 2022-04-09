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
	 * 投稿とそれに不随するデータをDBに登録するメソッド
	 */
	public void insert(Play play) {
		dao.insert(play);
	}

	/*
	 * inputsテーブルからスコアのListを取得するメソッド
	 */
	public List<BigDecimal> findScore() {
		//List<Map<String, Object>> dbScoreList = dao.findScore();
		List<Play>       dbScoreList = dao.findScore();             //DBからList<Play>型オブジェクトを取得
		List<BigDecimal> scoreList   = new ArrayList<BigDecimal>(); //BigDecimal型の空Listを用意
		for(int i = 0; i < 3; i++) {
			//Play play = new Play();
			scoreList.add(dbScoreList.get(i).getScore());           //List<Play>型オブジェクトからスコアを取得
		}
		return scoreList;
	}

}