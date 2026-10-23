package main;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LaunchAiAPP
{

	public static void main(String[] args) throws IOException, InterruptedException 
	{
		
		//API Key
		String apiKey=System.getenv("OPENAI_API_KEY");
	
		String requestBody="""
				{
					"model":"gpt-5.6-luna",
					"input":"Explain Java in One sentence"
				}
				""";
		
		// HTTP Client
		
		HttpClient httpClient = HttpClient.newHttpClient();
		
		
		//HTTP request (requestbody)

		HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("https://api.openai.com/v1/responses"))
		.header("Authorization", "Bearer " + apiKey)
		.header("Content-Type", "application/json")
		.POST(HttpRequest.BodyPublishers.ofString(requestBody))
		.build();


		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
	
	}

}
