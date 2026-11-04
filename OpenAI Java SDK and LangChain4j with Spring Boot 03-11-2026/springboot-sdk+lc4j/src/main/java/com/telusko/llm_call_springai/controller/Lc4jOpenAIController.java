package com.telusko.llm_call_springai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telusko.llm_call_springai.assistant.JavaAssistant;
import com.telusko.llm_call_springai.dto.ChatRequest;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;

@RestController
@RequestMapping("/api/lc4j")
public class Lc4jOpenAIController
{
	private final ChatModel chatModel;
	
	private final JavaAssistant assist;
	
	public Lc4jOpenAIController(ChatModel chatModel, JavaAssistant assist)
	{
		this.chatModel=chatModel;
		this.assist=assist;
	}
	
	@GetMapping("/chat")
	public String chat(@RequestParam String message)
	{
		return chatModel.chat(message);
	}
	
	@GetMapping("/explain")
	public String explain(@RequestParam String topic)
	{
		SystemMessage system = SystemMessage.from(	"You are Java and AI expert"
				+ "Explain this in simplified way also very short");
		
		UserMessage user=UserMessage.from("Explain "+ topic);
		return chatModel.chat(system, user)
				.aiMessage()
				.text();
	}
	
	@PostMapping("/chat")
	public String postChat(@RequestBody ChatRequest request)
	{
		return chatModel.chat(request.message());
	}
	
	@GetMapping("/chat/aiservice")
	public String chatAIService(@RequestParam String message)
	{
		return assist.anshul(message);
	}
	
	@GetMapping("/explain/aiservice")
	public String explainShramikAI(@RequestParam String message)
	{
		return assist.shramikAI(message);
	}
	

}
