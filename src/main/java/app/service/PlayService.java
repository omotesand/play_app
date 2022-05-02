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
//		List<BigDecimal> scoreList   = new ArrayList<BigDecimal>(); //BigDecimal型の空Listを用意

		//-----inputsテーブルから取得したスコアが5つの場合-----
		if(dbSelectedList.size() >= 5) {
			for(int i = 0; i < 5; i++) {
				Play playForTop5 = new Play();                   //List（参照型）にaddするため、forループ毎にPlayをnewする
				playForTop5.setInput((String)dbSelectedList.get(i).getInput());
				playForTop5.setScore((BigDecimal)dbSelectedList.get(i).getScore());
				showResultList.add(playForTop5);
			}
		//-----inputsテーブルから取得したスコアが5つに満たない場合-----
		}else{
			for(int i = 0; i < dbSelectedList.size(); i++) {     //取得した数だけリストにaddする
				Play playForTop1to4 = new Play();                //List（参照型）にaddするため、forループ毎にPlayをnewする
				playForTop1to4.setInput((String)dbSelectedList.get(i).getInput());
				playForTop1to4.setScore((BigDecimal)dbSelectedList.get(i).getScore());
				showResultList.add(playForTop1to4);
			}
			for(int i = 0; i < 5 - dbSelectedList.size(); i++) { //5つに満たないぶんは0埋めする
				Play playFor0padding = new Play();               //List（参照型）にaddするため、forループ毎にPlayをnewする
				playFor0padding.setInput("登録なし");
				playFor0padding.setScore(BigDecimal.valueOf(0));
				showResultList.add(playFor0padding);
			}
		}
		return showResultList;
	}

}