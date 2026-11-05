package ollamalc4j.ollama;

import java.time.Duration;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class OpenAivsOllama 
{
	
	public static void main(String[] args) 
	{
		 ChatModel openAi = OpenAiChatModel.builder()
	                .apiKey(System.getenv("OPENAI_API_KEY"))
	                .modelName("gpt-5.4-mini")
	                .build();

	        ChatModel mistral = OllamaChatModel.builder()
	                .baseUrl("http://localhost:11434")
	                .modelName("mistral:latest")
	                .timeout(Duration.ofSeconds(120))
	                .build();

	        ask("OpenAI gpt-5.4-mini (cloud)", openAi);
	        ask("Mistral 7B (local)", mistral);
	    }

	    // Works with ANY ChatModel - OpenAI, Ollama, or any other provider
	    static void ask(String name, ChatModel model) {
	        long start = System.currentTimeMillis();
	        String answer = model.chat("In one sentence, agentic ai");

	        System.out.println("===== " + name + " =====");
	        System.out.println(answer);
	        System.out.println("(took " + (System.currentTimeMillis() - start) + " ms)\n");
	    }
	

}
