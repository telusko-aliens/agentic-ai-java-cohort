package com.telusko;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telusko.tools.CourseTools;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        String apiKey = System.getenv("OPENAI_API_KEY");
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-4o-mini")
                .logRequests(true)
                .logResponses(true)
                .build();
        CourseTools tools = new CourseTools();
//        CourseAssistant assistant = AiServices.builder(CourseAssistant.class)
//                .chatModel(model)
//                .tools(tools)
//                .build();
//
//        String reply=assistant.chat("What is the duration of AI Course and Price of AI course?");
        List<ToolSpecification> specifications =
                ToolSpecifications.toolSpecificationsFrom(tools);

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(UserMessage.from(
                "What is the duration and price of AI Course?"
        ));


        ChatRequest request = ChatRequest.builder()
                .messages(messages)
                .toolSpecifications(specifications)
                .build();

        ChatResponse response = model.chat(request);
        AiMessage aiMessage = response.aiMessage();
        messages.add(aiMessage);


//        if(aiMessage.hasToolExecutionRequests())
//        {
//            aiMessage.toolExecutionRequests().forEach(request1 -> {
//                System.out.println("Tool Name: "+request1.name());
//                System.out.println("Tool Input: "+request1.arguments());
//
//            });
//        }

        if(aiMessage.hasToolExecutionRequests())
        {
            for(ToolExecutionRequest toolRequest:aiMessage.toolExecutionRequests())
            {
                System.out.println("Tool Name: "+toolRequest.name());
                System.out.println("Tool Input: "+toolRequest.arguments());
                String arguments=toolRequest.arguments();
                System.out.println("Argument names" +arguments);
                ObjectMapper mapper=new ObjectMapper();
                JsonNode json = mapper.readTree(arguments);
                String courseArgu=json.get("arg0").asText();
                System.out.println("After parsing argument names" +courseArgu);
                String result;
                switch (toolRequest.name()) {

                    case "getCourseDuration":
                        result=tools.getCourseDuration(courseArgu);
                        break;

                    case "getCoursePrice":
                        result = tools.getCoursePrice(courseArgu);
                        break;

                    default:
                        result = "Unknown tool";
                }

                System.out.println("Tool Result: " + result);
                messages.add(ToolExecutionResultMessage.from(toolRequest, result));

            }
            ChatRequest finalRequest = ChatRequest.builder()
                    .messages(messages)
                    .toolSpecifications(specifications)
                    .build();

            ChatResponse finalAnswer = model.chat(finalRequest);
            System.out.println("Final Answer: " + finalAnswer.aiMessage().text());

        }
        else
        {
            System.out.println("Final Answer: " + aiMessage.text());

        }

    }
}
