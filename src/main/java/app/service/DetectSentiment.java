/*
  	Original code is from
  	https://docs.aws.amazon.com/code-samples/latest/catalog/javav2-comprehend-src-main-java-com-example-comprehend-DetectSentiment.java.html
*/
package app.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.ComprehendException;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentRequest;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;

@Service
public class DetectSentiment {

	public BigDecimal amazonComprehend(int type, String text) {
		Region region = Region.AP_NORTHEAST_1;
		ComprehendClient comClient = ComprehendClient.builder()
				.region(region)
				.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
				.build();

		BigDecimal score = detectSentiments(comClient, type, text);
		comClient.close();
		return score;
	}

	public static BigDecimal detectSentiments(ComprehendClient comClient, int type, String text){

	try {
		DetectSentimentRequest detectSentimentRequest = DetectSentimentRequest.builder()
				.text(text)
				.languageCode("ja")
				.build();

		DetectSentimentResponse detectSentimentResult = comClient.detectSentiment(detectSentimentRequest);
		BigDecimal oneHundred = new BigDecimal("100"); //最後にスコアを100倍（100点満点にする）

		//-----type==1:positiveスコア、type==2:neutralスコア、type==3:negativeスコア-----
		if(type == 1) {
			return BigDecimal.valueOf(detectSentimentResult.sentimentScore().positive()).multiply(oneHundred);
		}else if(type == 2) {
			return BigDecimal.valueOf(detectSentimentResult.sentimentScore().neutral()).multiply(oneHundred);
		}else {
			return BigDecimal.valueOf(detectSentimentResult.sentimentScore().negative()).multiply(oneHundred);
		}

	} catch (ComprehendException e) {
		System.err.println(e.awsErrorDetails().errorMessage());
		System.exit(1);
		return BigDecimal.valueOf(-1.0);
		}
	}
}