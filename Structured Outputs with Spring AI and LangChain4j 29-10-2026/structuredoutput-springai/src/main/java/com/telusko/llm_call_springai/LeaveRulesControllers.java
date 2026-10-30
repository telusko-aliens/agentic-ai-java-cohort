package com.telusko.llm_call_springai;

import java.time.LocalDate;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeaveRulesControllers 
{
	private final ChatClient chatClient;
	
	public LeaveRulesControllers(OpenAiChatModel chatModel)
	{
		this.chatClient=ChatClient.create(chatModel);
	}
	
    @PostMapping(value = "/leave/rules", consumes = MediaType.TEXT_PLAIN_VALUE)
    public LeaveDecision check(@RequestBody String email)
    {
    	LocalDate today=LocalDate.now();
    	  
		LeaveRequest leave = chatClient.prompt()
                   .system("Read the email and return the leave details as JSON.")
                   .user(email)
                   .call()
//                   .entity(LeaveRequest.class, spec -> spec.useProviderStructuredOutput());
                   .entity(LeaveRequest.class);

                  
		
		List<String> problems = LeaveRules.check(leave, today);
		return new LeaveDecision(leave, problems.isEmpty(), problems);
    }

}
