package com.telusko.llm_call_springai.web;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class StreamingController 
{
    private final ChatClient chatClient;
    

    public StreamingController(OpenAiChatModel model)
    {
    	this.chatClient=ChatClient.create(model);
    }
    
    @GetMapping("/full-block")
    public String blockingFullRes(@RequestParam String question)
    {
    	return chatClient.prompt()
                .user(question)
                .call()
                .content();
    	
    }
    
    @GetMapping(value = "/stream",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streaming(@RequestParam String question)
    {
    	return chatClient.prompt()
                .user(question)
                .stream()
                .content();
    	
    }
    
//    @GetMapping(value = "/stream-usage",
//            produces = MediaType.TEXT_EVENT_STREAM_VALUE) 
    @GetMapping("/stream-usage")

    public Flux<String> streamWithUsage(@RequestParam String question)
    {
    	StringBuilder fullAnswer= new StringBuilder();
    	


    	return chatClient.prompt()
    	        .user(question)
//    	        .options(OpenAiChatOptions.builder()
//    	                .streamUsage(true)
//    	                .build())
    	        .stream()
    	        .chatResponse()
    	        .map(response -> {

//    	            Usage usage = response.getMetadata() != null
//    	                    ? response.getMetadata().getUsage(): null;

    	            String text = "";

                    if (response.getResult() != null
                            && response.getResult().getOutput() != null
                            && response.getResult().getOutput().getText() != null) {

                        text = response.getResult()
                                .getOutput()
                                .getText();

                        fullAnswer.append(text);
                    }

                    if (response.getMetadata() != null) {

                        var metadata = response.getMetadata();
                        Usage usage = metadata.getUsage();

                        if (usage != null
                                && usage.getPromptTokens() != null
                                && usage.getCompletionTokens() != null
                                && usage.getTotalTokens() != null
                                && usage.getTotalTokens() > 0) {

                            return text
                                    + "\n\n===== METADATA ====="
                                    + "\nResponse ID: " + metadata.getId()
                                    + "\nModel: " + metadata.getModel()
                                    + "\nInput Tokens: " + usage.getPromptTokens()
                                    + "\nOutput Tokens: " + usage.getCompletionTokens()
                                    + "\nTotal Tokens: " + usage.getTotalTokens()
                                    + "\n====================";
                        }
                    }

                    return text;
                })
    	        .filter(text -> !text.isEmpty())
    	        .doOnComplete(()->{
    	        	System.out.println("Full answer");
    	        	System.out.println(fullAnswer);
    	        	System.out.println(fullAnswer.length());
    	        });
    }
    
}
