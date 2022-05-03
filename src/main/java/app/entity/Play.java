package app.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Play {
	//challengesテーブル
	private int challengeId;
	private String challenge;

	//inputsテーブル
	private int        inputId;
	private int        currentChallengeId;
	private int        sentimentType;
	private String     input;
	private BigDecimal score;

	private int        yourRank; //スコア順位取得時にSQLのASで作成
//	private LocalDateTime created_at;

	//sentimentテーブル
	private int sentimentId;
}