package com.telusko.mcp;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface FileAssistant
{
    @SystemMessage("""
        You are a helpful file assistant.

        Use the available filesystem tools when
        the user asks about local files.

        Never invent file contents.
        """)
    @UserMessage("{{it}}")
    String chat(String message);
}
