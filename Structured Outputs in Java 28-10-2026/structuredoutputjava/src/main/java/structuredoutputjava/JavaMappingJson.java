package structuredoutputjava;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.StructuredChatCompletionCreateParams;

public class JavaMappingJson 
{
	public static void main(String[] args)
	{
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .build();
        
        StructuredChatCompletionCreateParams<LeaveRequest> params = ChatCompletionCreateParams.builder()
                .model("gpt-5.4-mini")
                .addSystemMessage("Read the email and return the leave details as JSON.")
                .addUserMessage(Emails.SICK_LEAVE)
                .responseFormat(LeaveRequest.class)
                .build();
        
        LeaveRequest leave = client.chat().completions().create(params)
                .choices().get(0).message().content().orElseThrow();
       System.out.println(leave);
		
	}


}
