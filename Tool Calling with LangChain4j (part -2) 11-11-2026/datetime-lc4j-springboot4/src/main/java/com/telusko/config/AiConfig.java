package com.telusko.config;

import com.telusko.DateTimeAssistant;
import com.telusko.tools.DateTimeTools;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public OpenAiChatModel chatModel(
            @Value("${openai.api.key}") String apiKey) {

        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-4o-mini")
                .build();
    }

    @Bean
    public DateTimeTools dateTimeTools() {
        return new DateTimeTools();
    }

    @Bean
    public DateTimeAssistant dateTimeAssistant(
            OpenAiChatModel model,
            DateTimeTools tools) {

        return AiServices.builder(DateTimeAssistant.class)
                .chatModel(model)
                .tools(tools)
                .build();
    }
}
