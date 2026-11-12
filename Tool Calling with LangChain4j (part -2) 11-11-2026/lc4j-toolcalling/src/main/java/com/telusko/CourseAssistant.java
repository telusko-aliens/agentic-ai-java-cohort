package com.telusko;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CourseAssistant
{
    @SystemMessage("""
        You are a helpful course assistant.
        Use available tools to answer course
        price and duration questions.
        Do not invent course details.
        """)
    @UserMessage("{{it}}")
    String chat(String message);
//    @UserMessage("{{message}}")
//    String chat(@V("message")String message);
}
