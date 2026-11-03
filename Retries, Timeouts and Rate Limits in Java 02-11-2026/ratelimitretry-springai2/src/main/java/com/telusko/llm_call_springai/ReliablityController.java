package com.telusko.llm_call_springai;

import java.time.Duration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openai.errors.OpenAIException;

@RestController
public class ReliablityController
{
	private ChatClient chatClient;
	
	public ReliablityController(OpenAiChatModel chatModel)
	{
		this.chatClient=ChatClient.create(chatModel);
	}
	
	@GetMapping("/wrongModel")
	public String wrongModel()
	{
		return callAndExplain(
				OpenAiChatOptions.builder()
				.model("telusko-gpt1.0")
				.maxRetries(0)
				);
				
	}
	

	@GetMapping("/timeout")
	public String timeout()
	{
		return callAndExplain(
				OpenAiChatOptions.builder()
				.timeout(Duration.ofSeconds(2))
				.maxRetries(0));
	
	}
	
    @GetMapping("/rate-limit")
    public RateLimitInfo rateLimit() {
        ChatResponse response = chatClient.prompt()
                .user("What is the JVM? Answer in one line.")
                .call()
                .chatResponse();
        
        System.out.println(response.getResult().getOutput().getText());

        RateLimit limit = response.getMetadata().getRateLimit();

        return new RateLimitInfo(
                limit.getRequestsLimit(), limit.getRequestsRemaining(), limit.getRequestsReset(),
                limit.getTokensLimit(), limit.getTokensRemaining(), limit.getTokensReset());
    }
	
	
	
	 private String callAndExplain(OpenAiChatOptions.Builder options) {
	        try {
	            return chatClient.prompt()
	                    .user("Write a 300 word essay on the history of Java.")
	                    .options(options)
	                    .call()
	                    .content();

	        } catch (OpenAIException e) {
	            String action;
	            if (ErrorRules.shouldRetry(e)) {
	                action = "RETRY (wait a little and try again)";
	            } else {
	                action = "DO NOT RETRY (fix it / show the user a message)";
	            }
	            return "error : " + e.getClass().getSimpleName() + "\naction: " + action;
	        }
	    }
	 
	    public record RateLimitInfo(Long requestsLimit, Long requestsRemaining, Duration requestsReset,
                Long tokensLimit, Long tokensRemaining, Duration tokensReset) {
}



}
