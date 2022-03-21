/*
  	This code is from https://docs.aws.amazon.com/code-samples/latest/catalog/javav2-comprehend-src-main-java-com-example-comprehend-DetectSentiment.java.html
*/
package app.service;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.ComprehendException;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentRequest;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;

public class DetectSentiment {

	public static void main(String[] args) {
		String text = "やったあ。接続に成功したぞ。";
		Region region = Region.AP_NORTHEAST_1;
		ComprehendClient comClient = ComprehendClient.builder()
				.region(region)
				.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
				.build();

		System.out.println("Calling DetectSentiment");
		detectSentiments(comClient, text);
		comClient.close();
	}

	public static void detectSentiments(ComprehendClient comClient, String text){

	try {
		DetectSentimentRequest detectSentimentRequest = DetectSentimentRequest.builder()
				.text(text)
				.languageCode("ja")
				.build();

		DetectSentimentResponse detectSentimentResult = comClient.detectSentiment(detectSentimentRequest);
		System.out.println("The Neutral value is " +detectSentimentResult.sentimentScore().neutral() );

	} catch (ComprehendException e) {
		System.err.println(e.awsErrorDetails().errorMessage());
		System.exit(1);
		}
	}
}