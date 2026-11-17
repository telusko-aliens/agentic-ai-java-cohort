package com.telusko.mcp;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface CourseAssistant
{
    @SystemMessage("""
        You are a helpful course assistant.

        Use the available MCP tools to answer
        questions about course prices and durations.

        Do not invent course information.
        """)
    @UserMessage("{{it}}")
    String chat(String message);
}
