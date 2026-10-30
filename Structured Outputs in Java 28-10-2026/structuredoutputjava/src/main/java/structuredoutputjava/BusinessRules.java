package structuredoutputjava;

import java.time.LocalDate;
import java.util.List;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.StructuredChatCompletionCreateParams;

public class BusinessRules 
{
	
    static OpenAIClient client = OpenAIOkHttpClient.builder()
            .apiKey(System.getenv("OPENAI_API_KEY"))
            .build();
	public static void main(String[] args)
	{
		
		List<String> emails = List.of(Emails.SICK_LEAVE, Emails.TOO_LONG);
		
		LocalDate today = LocalDate.now();
		
		for(String email:emails)
		{
			LeaveRequest leave = extract(email, today);
			
			
			List<String> problems = LeaveRules.check(leave, today);
			System.out.println(leave.employeeName() + " | " + leave.leaveType() + " | " + leave.startDate() + " | " + leave.endDate());
			if(problems.isEmpty()) {
				System.out.println("ACCEPETD - send to  manager for approval");
			}
			else
			{
				System.out.println("REJECTED - "+ problems);
			}
			System.out.println();
			
		}
		
	}
	  static LeaveRequest extract(String email, LocalDate today) {
	        StructuredChatCompletionCreateParams<LeaveRequest> params = ChatCompletionCreateParams.builder()
	                .model("gpt-5.4-mini")
	                .addSystemMessage("Extract the leave details from the email. Today's date is " + today + ".")
	                .addUserMessage(email)
	                .responseFormat(LeaveRequest.class)
	                .build();

	        return client.chat().completions().create(params)
	                .choices().get(0).message().content().orElseThrow();
	    }

}
