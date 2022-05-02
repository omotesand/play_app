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
//	public Play findChallenge() {
//		return dao.findChallenge();
//	}

	/*
	 * challengesテーブルからお題全件を取得するメソッド
	 */
	public List<Play> findAll() {
		return dao.findAll();
	}

	/*
	 * Formで選択したお題IDに合致するお題を取得するメソッド
	 */
	public Play findById(int analyzedChallengeId) {
		return dao.findById(analyzedChallengeId);
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
	public List<Play> findRank(Play play) {
		List<Play> dbSelectedList = dao.findRank(play);
		List<Play> showResultList = new ArrayList<Play>();
//		List<Play>       dbScoreList = dao.findScore();             //DBからList<Play>型オブジェクトを取得
//		List<BigDecimal> scoreList   = new ArrayList<BigDecimal>(); //BigDecimal型の空Listを用意

		//-----inputsテーブルから上位5位の投稿とスコアを取得-----
		for(int i = 0; i < 5; i++) {
			Play playFromDB = new Play();
			String dbRegisteredInput = dbSelectedList.get(i).getInput();     //第i位の投稿文章を取得
			BigDecimal dbRegisteredScore = dbSelectedList.get(i).getScore(); //第i位のスコアを取得
			//-----上位5位の内容をEntityへ詰める（上位5位がなければスコア"0"扱い-----）
			if(dbRegisteredInput != null && dbRegisteredScore != null) {     //投稿・スコアともに"0"でなければ
				playFromDB.setInput(dbSelectedList.get(i).getInput());
				playFromDB.setScore(dbSelectedList.get(i).getScore());
			}else {
				playFromDB.setInput("登録なし");
				playFromDB.setScore(BigDecimal.valueOf(0));
			}
			showResultList.add(playFromDB);
		}
		return showResultList;
	}

}