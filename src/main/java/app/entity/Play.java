package app.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Play {
	//challengesテーブル
	private int challengeId;
	private String sentimentType;
	private String challenge;

	//inputsテーブル
	private int        inputId;
	private int        yourChallengeId;  //ユーザーが選択したお題ID
	private int        yourSentimentType;//ユーザーが選択した感情タイプ
	private String     input;
	private BigDecimal score;

	private int        yourRank;        //スコア順位取得時にSQLのASで作成
	private int        totalCount;      //スコア順位取得時にSQLのASで作成
//	private LocalDateTime created_at;

	//sentimentテーブル
	private int sentimentId;
}