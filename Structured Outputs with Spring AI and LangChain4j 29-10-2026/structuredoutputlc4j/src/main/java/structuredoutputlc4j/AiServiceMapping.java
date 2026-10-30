package structuredoutputlc4j;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import structuredoutputlc4j.util.Emails;
import structuredoutputlc4j.util.LeaveRequest;

import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;

import java.time.LocalDate;


public class AiServiceMapping
{
	public static void main(String[] args) 
	{
		String apiKey=System.getenv("OPENAI_API_KEY");
	    
		ChatModel model = OpenAiChatModel.builder()
	                .apiKey(apiKey)
	                .modelName("gpt-5.4-mini")
	                .supportedCapabilities(RESPONSE_FORMAT_JSON_SCHEMA)
	                .strictJsonSchema(true)
	                .build();
		String today=LocalDate.now().toString();
		
		LeaveAssistant assistant = AiServices.create(LeaveAssistant.class, model);
		
		LeaveRequest leave = assistant.extract(Emails.SICK_LEAVE,today);
		
		System.out.println("=== leave llm response===");
		System.out.println(leave);
		
		
		
	}

}
