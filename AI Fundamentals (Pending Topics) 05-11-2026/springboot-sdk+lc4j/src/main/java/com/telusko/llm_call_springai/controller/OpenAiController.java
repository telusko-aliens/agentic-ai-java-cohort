package com.telusko.llm_call_springai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

@RestController
@RequestMapping("/api/openai")
public class OpenAiController
{
	private final OpenAIClient client;
	public OpenAiController(OpenAIClient client)
	{
		this.client=client;
	}
	
	@GetMapping("/chat")
	public String chat(@RequestParam String message)
	{
		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
		.model(ChatModel.GPT_4O_MINI_2024_07_18)
		.addUserMessage(message)
		.build();
		
		return client.chat().completions().create(params)
		.choices().get(0).message().content().orElse("No response");
	}
	
	@GetMapping("/explain")
	public String explain(@RequestParam String message)
	{
		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
		.model(ChatModel.GPT_4O_MINI_2024_07_18)
		.addSystemMessage(
				"You are Java and AI expert"
						+ "Explain this in simplified way also very short")
		.addUserMessage(message)
		.temperature(0.4)
		.build();
		
		return client.chat().completions().create(params)
		.choices().get(0).message().content().orElse("No response");
	}
	@GetMapping("/metadata")
	public Object explainMetadata(@RequestParam String message)
	{
		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
		.model(ChatModel.GPT_4O_MINI_2024_07_18)
		.addSystemMessage(
				"You are Java and AI expert"
						+ "Explain this in simplified way also very short")
		.addUserMessage(message)
		.temperature(0.4)
		.build();
		
		ChatCompletion response = client.chat().completions().create(params);
		return response.usage();
	}

}
