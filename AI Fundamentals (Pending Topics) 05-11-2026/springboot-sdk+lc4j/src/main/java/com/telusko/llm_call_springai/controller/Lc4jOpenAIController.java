package com.telusko.llm_call_springai.controller;

import java.util.HashMap;
import java.util.Map;

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
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;

@RestController
@RequestMapping("/api/lc4j")
public class Lc4jOpenAIController
{
	private final ChatModel chatModel;
	
	private final JavaAssistant assist;
	
//	PromptTemplate template= PromptTemplate.from(     
//			"You are a friendly teacher. Explain {{topic}} to a {{level}} student " +
//            "in {{lines}} lines with one real-life example.");
	
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
	
	@GetMapping("/chat-pt")
	public String explainPt(@RequestParam String topic, 
			@RequestParam String level, @RequestParam String lines)
	{
//		Map<String, Object> variables=new HashMap<>();
//		variables.put("topic",topic);
//		variables.put("level",level);
//		variables.put("lines",lines);
//		Prompt prompt = template.apply(variables);
//		String finalQuestion=prompt.text();
//		System.out.println(finalQuestion);
//		return chatModel.chat(finalQuestion);
		return assist.pt(topic, level, lines);
	}
	

}
