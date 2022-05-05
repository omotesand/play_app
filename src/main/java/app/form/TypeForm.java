package app.form;

import javax.validation.constraints.Digits;

import lombok.Data;

@Data
public class TypeForm {
	//@NotNull(message = "感情のタイプを選択してください。")
	@Digits(integer = 1, fraction = 0) //整数部1桁、小数部0桁
	private int yourSentimentType;
}