package com.telusko.llm_call_springai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromptOnlyController
{
	private final ChatClient chatClient;
	
	public PromptOnlyController(OpenAiChatModel chatModel)
	{
		this.chatClient=ChatClient.create(chatModel);
	}
	
    @PostMapping(value = "/leave/prompt-only", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String promptOnly(@RequestBody String email) {
        return chatClient.prompt()
                .system("Read the email and return the leave details as JSON.")
                .user(email)
                .call()
                .content();
    }

}
