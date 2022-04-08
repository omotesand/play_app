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
	private int        sentimentType;
	private String     input;
	private BigDecimal score;
//	private LocalDateTime created_at;

	//sentimentテーブル
	private int sentimentId;
}