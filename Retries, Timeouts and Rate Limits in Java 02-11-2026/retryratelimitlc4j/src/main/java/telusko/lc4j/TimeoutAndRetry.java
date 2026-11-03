package telusko.lc4j;

import java.time.Duration;

import dev.langchain4j.exception.TimeoutException;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class TimeoutAndRetry 
{

	public static void main(String[] args)
	{
		
	
		tryWithRetries(0);
		System.out.println("**********************************************");
		System.out.println();
		tryWithRetries(2);
		

	}
	
	static void tryWithRetries(int maxRetries)
	{
		
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-5.4-mini")
                .timeout(Duration.ofSeconds(15))
                .maxRetries(maxRetries)
                .build();
        long start=System.currentTimeMillis();
        try
        {
        	String result=model.chat("Write a 300 word essay on history of Java");
        	System.out.println(result);
        	
        }
        catch(TimeoutException e)
        {
        	long time=System.currentTimeMillis()-start;
            System.out.println("maxRetries(" + maxRetries + ") -> TimeoutException after " + time + " ms");
        	
        }
		
	}

}
