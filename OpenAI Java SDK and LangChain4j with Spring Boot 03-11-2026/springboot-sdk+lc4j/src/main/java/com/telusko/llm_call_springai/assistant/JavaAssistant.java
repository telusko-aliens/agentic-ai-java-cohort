package com.telusko.llm_call_springai.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface JavaAssistant 
{
	String anshul(String message);
//	String chat(String message);
	
	
    @SystemMessage("""
            You are an expert Java instructor.
            Explain everything in simple language.
            Give short answers with examples.
            """)
    @UserMessage("Explain {{topic}}")
	String shramikAI(String topic);
//	String explain(String topic);
	

}
