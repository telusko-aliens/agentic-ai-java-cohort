package com.telusko.llm_call_springai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

@Configuration
public class AiConfig
{
	
	@Value("${openai.api.key}")
	private String apiKey;
	
	@Value("${openai.model}")
	private String modelName;
	
	//official OpenAI Java SDK Client
	
	@Bean
	public OpenAIClient openAIClient()
	{
		return OpenAIOkHttpClient.builder()
				.apiKey(apiKey)
				.build();
		
	}
	@Bean
	public ChatModel chatModel()
	{
		return OpenAiChatModel.builder()
				.apiKey(apiKey)
				.modelName(modelName)
				.temperature(0.8)
				.build();
	}

	
}
