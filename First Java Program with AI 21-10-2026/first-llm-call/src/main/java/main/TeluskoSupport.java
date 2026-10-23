package main;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface TeluskoSupport
{
    @SystemMessage(
            """
            You are an experienced Java instructor.
            Explain concepts in simple words.
            Always provide a small example.
            """
    )
    @UserMessage("Explain {{topic}} with a simple example! only defination short")
	String explain(@V("topic")String topic);

}
