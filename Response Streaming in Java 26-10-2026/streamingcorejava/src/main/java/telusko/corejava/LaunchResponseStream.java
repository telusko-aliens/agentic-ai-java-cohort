package telusko.corejava;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.http.StreamResponse;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseStreamEvent;
import com.openai.models.responses.ResponseUsage;

public class LaunchResponseStream
{
	
	static OpenAIClient client = OpenAIOkHttpClient.fromEnv();

	
	public static void main(String[] args)
	{
		ResponseCreateParams params = ResponseCreateParams.builder()
		.model("gpt-5.4-mini")
		.instructions("You're Java expert trainer")
		.input("Explain the Java Collections Framework in 5 short points.")
		.build();
		
		try(StreamResponse<ResponseStreamEvent>stream=client.responses().createStreaming(params))
		{
			stream.stream().forEach(event -> 
			{
				event.outputTextDelta().ifPresent(textEvent -> System.out.print(textEvent.delta()));

				//the complete response with token usage
                event.completed().ifPresent(completedEvent -> {
                    // Completed event contains final response metadata, including usage if available.
                    ResponseUsage usage = completedEvent.response().usage().orElseThrow();
                    System.out.println("\n");
                    System.out.println("input tokens  : " + usage.inputTokens());
                    System.out.println("output tokens : " + usage.outputTokens());
                });
			});
			
		}
	     
	      
	      
	}

}
