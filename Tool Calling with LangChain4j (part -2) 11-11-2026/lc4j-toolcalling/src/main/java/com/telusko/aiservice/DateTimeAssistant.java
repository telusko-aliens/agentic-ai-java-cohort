package com.telusko.aiservice;

import dev.langchain4j.service.SystemMessage;

public interface DateTimeAssistant
{
    @SystemMessage("""
        You are a helpful date and time assistant.

        Whenever the user asks for the current date
        or time of a location, use the available tool.

        Never guess the current date or time.
        If the location is unsupported, explain that.
        """)
    String chat(String message);
}
