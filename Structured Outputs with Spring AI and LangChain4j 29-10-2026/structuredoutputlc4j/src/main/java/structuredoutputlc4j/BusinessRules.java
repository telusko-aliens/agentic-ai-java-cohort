package structuredoutputlc4j;

import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;

import java.time.LocalDate;
import java.util.List;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import structuredoutputlc4j.util.Emails;
import structuredoutputlc4j.util.LeaveRequest;
import structuredoutputlc4j.util.LeaveRules;

public class BusinessRules 
{
	public static void main(String[] args)
	{
		String apiKey=System.getenv("OPENAI_API_KEY");
	    
		ChatModel model = OpenAiChatModel.builder()
	                .apiKey(apiKey)
	                .modelName("gpt-5.4-mini")
	                .supportedCapabilities(RESPONSE_FORMAT_JSON_SCHEMA)
	                .strictJsonSchema(true)
	                //.responseFormat("json_object")// for new ai models
	                .build();
		
		LeaveAssistant assistant = AiServices.create(LeaveAssistant.class, model);
		
        List<String> emails = List.of(Emails.SICK_LEAVE, Emails.TOO_LONG);
        LocalDate today = LocalDate.of(2026, 10, 29);

        for(String email:emails)
		{
        	LeaveRequest leave = assistant.extract(email, today.toString());
  //      	LeaveRequest leave = assistant.extract("HI, I m dileep, i need sick leave for 3 days", "2026-10-29");
        	
        	List<String> problems = LeaveRules.check(leave, today);
        	
            System.out.println(leave.employeeName() + " | " + leave.leaveType()
            + " | " + leave.startDate() + " to " + leave.endDate());

            	if (problems.isEmpty()) {
            			System.out.println("  ACCEPTED - send to manager for approval");
            		} else {
            				System.out.println("  REJECTED - " + problems);
            		}	
            		System.out.println();
			
		}

		
	}

}
