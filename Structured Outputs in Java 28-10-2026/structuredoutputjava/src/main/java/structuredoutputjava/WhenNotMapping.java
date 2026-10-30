package structuredoutputjava;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.errors.OpenAIException;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.StructuredChatCompletion;
import com.openai.models.chat.completions.StructuredChatCompletionCreateParams;

public class WhenNotMapping
{
	static OpenAIClient client = OpenAIOkHttpClient.builder()
	        .apiKey(System.getenv("OPENAI_API_KEY"))
	        .build();
	public static void main(String[] args)
	{
		//answerWasCut();
		retryWithTheError();
		
	}
	
	static void answerWasCut()
	{
		 StructuredChatCompletionCreateParams<LeaveRequest> params = ChatCompletionCreateParams.builder()
	                .model("gpt-5.4-mini")
	                .addSystemMessage("Extract the leave details from the email. Today's date is ")
	                .addUserMessage(Emails.SICK_LEAVE)
	                .responseFormat(LeaveRequest.class)
	                .maxCompletionTokens(12)
	                .build();
		 
		    StructuredChatCompletion.Choice<LeaveRequest> choice =
	                client.chat().completions().create(params).choices().get(0);

	        System.out.println("===== Case 1: answer was cut =====");
	        System.out.println("finish reason : " + choice.finishReason());
	        
	        if (choice.message().refusal().isPresent()) {
	            System.out.println("Model refused: " + choice.message().refusal().get());
	            return;
	        }
	        
	        if (choice.finishReason().equals(ChatCompletion.Choice.FinishReason.LENGTH)) {
	            System.out.println("Answer is incomplete - do not use it. Increase the token limit and try again.");
	            return; // Never continue to parse a known-incomplete result.
	        }
	        
	        try {
	            choice.message().content();
	        } catch (OpenAIException e) {
	            System.out.println("Reading it anyway fails: " + e.getClass().getSimpleName());
	        }
	    

	}
	
	// Case 2: no schema (like Topic 1) -> parse fails -> tell the model the error and ask again
    static void retryWithTheError() {

        System.out.println("\n===== Case 2: retry with the error message =====");

        ObjectMapper mapper = new ObjectMapper();

        ChatCompletionCreateParams.Builder conversation = ChatCompletionCreateParams.builder()
                .model("gpt-5.4-mini")
                .addSystemMessage("Read the email and return the leave details as JSON.")
                .addUserMessage(Emails.SICK_LEAVE);

        for (int attempt = 1; attempt <= 2; attempt++) {

            String reply = client.chat().completions().create(conversation.build())
                    .choices().get(0).message().content().orElse("");

            try {
                LeaveRequest leave = mapper.readValue(reply, LeaveRequest.class);
                System.out.println("Attempt " + attempt + " worked: " + leave);
                return;

            } catch (JsonProcessingException e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getOriginalMessage());

                // Keep the conversation: the bad reply + what was wrong with it
                conversation
                        .addAssistantMessage(reply)
                        .addUserMessage("Your JSON could not be read: " + e.getOriginalMessage()
                                + ". Reply again with ONLY a JSON object with exactly these keys: "
                                + "askingForLeave (true/false), employeeName, leaveType (SICK, CASUAL or EARNED), "
                                + "startDate and endDate (yyyy-MM-dd), reason.");
            }
        }

        System.out.println("Still failing - send this email to a human.");
    }

}
