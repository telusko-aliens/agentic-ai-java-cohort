package main;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class LaunchOpenSDK 
{
	static OpenAIClient client = OpenAIOkHttpClient.fromEnv();
	

	public static void main(String[] args)
	{
//		OpenAIClient client = OpenAIOkHttpClient.fromEnv();
//		String apiKey=System.getenv("OPENAI_API_KEY");
//		if(apiKey == null  || apiKey.isBlank())
//		{
//			System.out.println("API key not found please set api key");
//		}
		
		String systemMessage="You're Java expert, Reply in short one line only about Java and related tech. Non tech deny";
	
		
		
		
		ChatCompletionCreateParams firstConversation = ChatCompletionCreateParams.builder()
				.model("gpt-5.4-mini")
				.addSystemMessage(systemMessage)
				.addUserMessage("Hi, My name is Shramik Masti")
				.build();
		
			String firstReply=ask(firstConversation);
			System.out.println("first call : "+firstReply);
			System.out.println("***************************************************");
		
		ChatCompletionCreateParams secondConversation = ChatCompletionCreateParams.builder()
				.model("gpt-5.4-mini")
				.addSystemMessage(systemMessage)
				.addUserMessage("what is my name")
				.build();
				
				String secondReply=ask(secondConversation);
				System.out.println("second call : "+secondReply);
				System.out.println("***************************************************");
				
				ChatCompletionCreateParams thirdConversation = ChatCompletionCreateParams.builder()
						.model("gpt-5.4-mini")
						.addSystemMessage(systemMessage)
//						.addUserMessage("hi my name is shramik")
						.addAssistantMessage(firstReply)
						.addUserMessage("what is my name")
						.build();
						
						String thirdReply=ask(thirdConversation);
						System.out.println("third call : "+thirdReply);
						System.out.println("***************************************************");
				
				
				
		
		
		
		
		
		
		
		
		
		
		
//
//		OpenAIClient client = OpenAIOkHttpClient.builder()
//				.apiKey(apiKey)
//				.build();
//		
//
//	    ResponseCreateParams params =
//	        ResponseCreateParams.builder()
//	        .input("Tell me about Telusko")
//	        .model("gpt-6-astra")
//	        .build();
//	    
//	    Response response = client.responses().create(params);
////	    System.out.println(response);
//	    
//	    response.output().stream()
//        .flatMap(item -> item.message().stream())
//        .flatMap(message -> message.content().stream())
//        .flatMap(content -> content.outputText().stream())
//        .forEach(outputText -> System.out.println(outputText.text()));
//  

	}
	
	static String ask(ChatCompletionCreateParams chatParams)
	{
		return client.chat()
				.completions()
				.create(chatParams)
				.choices()
				.get(0)
				.message()
				.content()
				.orElse("");
		
	}

}
