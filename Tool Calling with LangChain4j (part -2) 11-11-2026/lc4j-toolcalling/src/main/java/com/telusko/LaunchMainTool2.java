package com.telusko;

import com.telusko.aiservice.DateTimeAssistant;
import com.telusko.tools.DateTimeTools;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class LaunchMainTool2
{
    public static void main(String[] args)
    {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .build();

        DateTimeTools tools = new DateTimeTools();
        DateTimeAssistant assistant =
                AiServices.builder(DateTimeAssistant.class)
                        .chatModel(model)
                        .tools(tools)
                        .build();
//        String response=model.chat("Tell me the current date and time in bangalore");
        String response=assistant.chat("Tell me the current date and time in london");
        System.out.println(response);
    }
}
