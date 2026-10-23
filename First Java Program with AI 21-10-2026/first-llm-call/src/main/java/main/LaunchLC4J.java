package main;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class LaunchLC4J 
{

	public static void main(String[] args)
	{
//		SystemMessage systemMessage = SystemMessage.systemMessage(""
//				+ "Act like Java trainer and aswer only Java programming related queries if its out of Java and software please dny and tell"
//				+ "i can't answer your query as i m only into Java and tech");
//		
//		UserMessage userQuestion = UserMessage.userMessage("tell me about fifa worldcup");
		
		
		
		ChatModel model = OpenAiChatModel.builder()
				.apiKey(System.getenv("OPENAI_API_KEY"))
		        .modelName("gpt-4o-mini")
		        .build();
		
		String answer = model.chat("Tell me who is Navin Reddy Java Telusko");
		System.out.println(answer);
		
//		ChatResponse response = model.chat(systemMessage, userQuestion);
//		System.out.println(response.aiMessage().text());
		
//		TeluskoSupport teluskoSupport = AiServices.create(TeluskoSupport.class, model);
//		String answer=teluskoSupport.explain("Dependency Injection");
//		System.out.println(answer);
		
	}

}
