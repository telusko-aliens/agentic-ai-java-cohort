package structuredoutputlc4j;

import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.output.OutputParsingException;
import structuredoutputlc4j.util.Emails;
import structuredoutputlc4j.util.LeaveRequest;

public class WhenNotMapping
{
	public static void main(String[] args)
	{
		
//		answerWasCut();
		retryWithTheError();
	}
	
	static void answerWasCut()
	{
		String apiKey=System.getenv("OPENAI_API_KEY");
	    
		ChatModel model = OpenAiChatModel.builder()
	                .apiKey(apiKey)
	                .modelName("gpt-5.4-mini")
	                .supportedCapabilities(RESPONSE_FORMAT_JSON_SCHEMA)
	                .strictJsonSchema(true)
	                //.responseFormat("json_object")// for new ai models
	                .maxCompletionTokens(15)
	                .build();
		
		LeaveAssistant assistant = AiServices.create(LeaveAssistant.class, model);
		System.out.println("=== answer was cut ====");
		try
		{
			LeaveRequest leave = assistant.extract(Emails.SICK_LEAVE, LocalDate.now().toString());
			System.out.println(leave);
		}
		catch(OutputParsingException e)
		{
			System.out.println("Could not convert : "+ e.getClass().getSimpleName());
			System.out.println("Answer is incomplete");
		}
	}
	
	
	
	 static void retryWithTheError() {

	        System.out.println("\n===== Case 2: retry with the error message =====");

			String apiKey=System.getenv("OPENAI_API_KEY");

	        ChatModel model = OpenAiChatModel.builder()
	                .apiKey(apiKey)
	                .modelName("gpt-5.4-mini")
	                .build();

	        ObjectMapper mapper = new ObjectMapper();

	        List<ChatMessage> conversation = new ArrayList<>();
	        conversation.add(SystemMessage.from("Read the email and return the leave details as JSON."));
	        conversation.add(UserMessage.from(Emails.SICK_LEAVE));

	        // Retry at most twice; each iteration calls the model and costs tokens.
	        for (int attempt = 1; attempt <= 2; attempt++) {

	            String reply = model.chat(conversation).aiMessage().text();

	            try {
	                LeaveRequest leave = mapper.readValue(reply, LeaveRequest.class);
	                System.out.println("Attempt " + attempt + " worked: " + leave);
	                return;

	            } catch (JsonProcessingException e) {
	                System.out.println("Attempt " + attempt + " failed: " + e.getOriginalMessage());

	                // Keep the conversation: the bad reply + what was wrong with it
	                // Send the failed model reply back as an assistant-role message.
	                conversation.add(AiMessage.from(reply));
	                // Give feedback as a NEW user-role message for the next call.
	                // add(...) alone does NOT call the model; next loop iteration does.
	                conversation.add(UserMessage.from("Your JSON could not be read: " + e.getOriginalMessage()
	                        + ". Reply again with ONLY a JSON object with exactly these keys: "
	                        + "askingForLeave (true/false), employeeName, leaveType (SICK, CASUAL or EARNED), "
	                        + "startDate and endDate (yyyy-MM-dd), reason."));
	            }
	        }

	        System.out.println("Still failing - send this email to a human.");
	    }

}
