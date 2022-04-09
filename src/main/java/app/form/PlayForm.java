package app.form;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PlayForm {
	@NotNull(message = "感情のタイプを選択してください。")
	@Digits(integer = 1, fraction = 0) //整数部1桁、小数部0桁
	private int sentimentType;

	@NotNull(message = "投稿する文章を入力してください。")
	@Size(min = 10, max = 120, message = "10文字以上120文字以下で入力してください。")
	private String input;
}