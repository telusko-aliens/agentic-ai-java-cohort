package ollamalc4j.ollama;

import java.time.Duration;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class LaunchOllama 
{
	public static void main(String[] args) 
	{
        ChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("mistral:latest")          // the name shown by "ollama list"
                .temperature(0.3)
                .timeout(Duration.ofSeconds(120))     // local models can be slow, the first call also loads the model
                .build();
        long start = System.currentTimeMillis();

        String answer=model.chat("In one sentence, answer what is agentic ai");
        System.out.println(answer);
        System.out.println("(took " + (System.currentTimeMillis() - start) + " ms)\n");
        
        
        System.out.println("*******************************");
        start = System.currentTimeMillis();
        
        ChatResponse response = model.chat(
                SystemMessage.from("You are a Java trainer. Answer in 3 short lines with a small example."),
                UserMessage.from("What is method overloading?"));

        System.out.println(response.aiMessage().text());
        System.out.println("(took " + (System.currentTimeMillis() - start) + " ms, "
                + "input tokens: " + response.tokenUsage().inputTokenCount() + ", "
                + "output tokens: " + response.tokenUsage().outputTokenCount() + ")");

        
        
		
	}

}
