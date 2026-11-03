package telusko.lc4j;

import java.util.List;
import java.util.Map;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatResponseMetadata;

public class RateLimit
{
	private static OpenAiChatResponseMetadata metadata;

	public static void main(String[] args)
	{
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-5.4-mini")
               
                .build();
        
        ChatResponse response = model.chat(UserMessage.from("What is JVM? Answer in one line"));
        
        metadata=(OpenAiChatResponseMetadata)response.metadata();
        
        Map<String, List<String>> headers = metadata.rawHttpResponse().headers();
        
        // Maximum number of requests allowed
        // in the relevant rate limit window.
        System.out.println("requests per minute: " + headers.get("x-ratelimit-limit-requests"));

        // Number of requests still available
        // in the current rate limit window.
        System.out.println("requests left      : " + headers.get("x-ratelimit-remaining-requests"));

        // Maximum number of tokens allowed
        // in the relevant rate limit window.
        System.out.println("tokens per minute  : " + headers.get("x-ratelimit-limit-tokens"));

        // Number of tokens remaining
        // in the current rate limit window.
        System.out.println("tokens left        : " + headers.get("x-ratelimit-remaining-tokens"));

        // Print an empty line for readability.
        System.out.println();
        
        System.out.println("answer: " + response.aiMessage().text());

	}

}
