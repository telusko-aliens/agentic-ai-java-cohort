package com.telusko.llm_call_springai.web;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ChatController 
{
	
	private final OpenAiChatModel chatModel;
	
	private final ChatClient chatClient;
	
	public ChatController(OpenAiChatModel chatModel)
	{
		this.chatModel=chatModel;
		this.chatClient=ChatClient.create(chatModel);
	}
	
	
	@GetMapping("/ask")
	public String askModel(@RequestParam String message)
	{
		return chatModel.call(message);
	}
	
	@GetMapping("/chat")
	public String chatModel(@RequestParam String message)
	{
		return chatClient.prompt()
		.system("You're Java expert. Keep answer short in 2 lines and also answer only tech related question")
		.user(message)
		.call()
		.content();
	}
	
	@GetMapping("/metadata")
	public UsageInfo chatModelMetaData(@RequestParam String message)
	{
		ChatResponse response = chatClient.prompt()
		        .system("You're a Java expert. Keep answers in 100 tokens")
		        .user(message)
		        .options(OpenAiChatOptions.builder()
		                .maxCompletionTokens(20))
		        .call()
		        .chatResponse();
		
		
		ChatResponseMetadata metadata = response.getMetadata();
		Usage usage = metadata.getUsage();
		
		return new UsageInfo(response.getResult().getOutput().getText(),
				metadata.getModel(),
				response.getResult().getMetadata().getFinishReason(),
				usage.getPromptTokens(),
				usage.getCompletionTokens(),
				usage.getTotalTokens());
		
		
	}
    public record UsageInfo(String answer, String model, String finishReason,
            Integer inputTokens, Integer outputTokens, Integer totalTokens) {
	
	
    }

}
