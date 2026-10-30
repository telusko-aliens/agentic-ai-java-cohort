package structuredoutputjava;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class PromptOnlyJsonDemo 
{
	public static void main(String[] args)
	{
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .build();
        
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model("gpt-5.4-mini")
                .addSystemMessage("Read the email and return the leave details as JSON.")
                .addUserMessage(Emails.SICK_LEAVE)
                .build();
        
        String reply = client.chat().completions().create(params)
                .choices().get(0).message().content().orElse("");
        
        System.out.println("model answer");
        System.out.println(reply);
        
        System.out.println("===================================");
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            LeaveRequest leave = mapper.readValue(reply, LeaveRequest.class);
            System.out.println("\nMapped: " + leave);
        } catch (JsonProcessingException e) {
            // A parsing error is expected in this teaching demo, but not guaranteed.
            System.out.println("\nCould not map to LeaveRequest:");
            System.out.println(e.getOriginalMessage());
        }
		
		
	}

}
