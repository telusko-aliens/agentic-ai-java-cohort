package com.telusko.llm_call_springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class ChatController
{

    private final ChatClient chatClient;

    public ChatController(OllamaChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/trainer")
    public String trainer(@RequestParam String message) {
        return chatClient.prompt()
                .system("You are a Java trainer. Answer in 3 short lines with a small example.")
                .user(message)
                .call()
                .content();
    }

    @GetMapping(value = "/stream",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String message) {

        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

}

// Command                 Purpose
// ollama --version        Check installed Ollama version
// ollama pull mistral     Download Mistral
// ollama list             Show downloaded models
// ollama run mistral      Start chatting with Mistral
// ollama ps               Show models currently loaded in memory
// ollama stop mistral     Unload Mistral from memory
// ollama show mistral     Display model information
// ollama rm mistral       Delete downloaded Mistral model
