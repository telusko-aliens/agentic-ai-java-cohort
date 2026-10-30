package structuredoutputlc4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import structuredoutputlc4j.util.Emails;
import structuredoutputlc4j.util.LeaveRequest;

public class PromptOnlyJson{

	public static void main(String[] args) 
	{
		String apiKey=System.getenv("OPENAI_API_KEY");
	    
		ChatModel model = OpenAiChatModel.builder()
	                .apiKey(apiKey)
	                .modelName("gpt-5.4-mini")
	                .build();
		
		String answer=model.chat(
				SystemMessage.from("Read the email and return the leave details as JSON."),
				UserMessage.from(Emails.SICK_LEAVE))
		.aiMessage().text();
		
		
		System.out.println("===model answer===");
		System.out.println(answer);
		ObjectMapper mapper=new ObjectMapper();
		try
		{
			LeaveRequest leave = mapper.readValue(answer, LeaveRequest.class);
			System.out.println("Maped data : json to java : "+ leave);
		}
		 catch (JsonProcessingException e) {
	            // We catch the parsing failure so the demo can print an explanation.
	            System.out.println("\nCould not map to LeaveRequest:");
	            System.out.println(e.getOriginalMessage());
		}
		
	}

}
