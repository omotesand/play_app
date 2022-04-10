package app.service;

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
//	public List<BigDecimal> findRank() {
	public List<Play> findRank() {
		List<Play> dbSelectedList = dao.findRank();
		List<Play> showResultList = new ArrayList<Play>();
//		List<Play>       dbScoreList = dao.findScore();             //DBからList<Play>型オブジェクトを取得
//		List<BigDecimal> scoreList   = new ArrayList<BigDecimal>(); //BigDecimal型の空Listを用意
		for(int i = 0; i < 5; i++) {
			Play play = new Play();
			play.setInput(dbSelectedList.get(i).getInput());
			play.setScore(dbSelectedList.get(i).getScore());
			showResultList.add(play);
//			scoreList.add(dbList.get(i).getScore());           //List<Play>型オブジェクトからスコアを取得
		}
		return showResultList;
	}

}