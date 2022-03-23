/*
  	Original code is from
  	https://docs.aws.amazon.com/code-samples/latest/catalog/javav2-comprehend-src-main-java-com-example-comprehend-DetectSentiment.java.html
*/
package app.service;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.ComprehendException;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentRequest;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;

public class DetectSentiment {

	public float amazonComprehend() {
		String text = "やったあ。接続に成功したぞ。";
		Region region = Region.AP_NORTHEAST_1;
		ComprehendClient comClient = ComprehendClient.builder()
				.region(region)
				.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
				.build();

		//System.out.println("Calling DetectSentiment");
		float score = detectSentiments(comClient, text);
		comClient.close();
		return score;
	}

	public static float detectSentiments(ComprehendClient comClient, String text){

	try {
		DetectSentimentRequest detectSentimentRequest = DetectSentimentRequest.builder()
				.text(text)
				.languageCode("ja")
				.build();

		DetectSentimentResponse detectSentimentResult = comClient.detectSentiment(detectSentimentRequest);
		return detectSentimentResult.sentimentScore().neutral();

	} catch (ComprehendException e) {
		System.err.println(e.awsErrorDetails().errorMessage());
		System.exit(1);
		return (float)-1.0;
		}
	}
}