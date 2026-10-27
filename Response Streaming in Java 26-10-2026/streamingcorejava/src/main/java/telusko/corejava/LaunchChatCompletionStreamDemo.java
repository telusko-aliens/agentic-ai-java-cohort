package telusko.corejava;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.http.StreamResponse;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionChunk;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class LaunchChatCompletionStreamDemo
{
	
	static OpenAIClient client = OpenAIOkHttpClient.fromEnv();

	
	public static void main(String[] args)
	{
		
	      ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
	                // Select the model that will generate the answer.
	                .model("gpt-5.4-mini")
	                .addSystemMessage("You're Java expert trainer ")
	                .addUserMessage("Explain the Java Collections Framework in 5 short points.")
	                .build();
	     
	      System.out.println("======each chunk separately =======");
	    	 
	      try( StreamResponse<ChatCompletionChunk> streaming = client.chat().completions().createStreaming(params))
		     {
		    	 streaming.stream()
		    	 .flatMap(chunk -> chunk.choices().stream())// get choices from each chunk
		    	 .flatMap(choice -> choice.delta().content().stream())//extract newly generated text from each choice
		    	 .forEach(text ->System.out.print(text + " | "));
		     }
	      System.out.println();
	      System.out.println();
	      
	      System.out.println("======print as its as how it arrives  =======");

	 	 
	      try( StreamResponse<ChatCompletionChunk> streaming = client.chat().completions().createStreaming(params))
		     {
		    	 streaming.stream()
		    	 .flatMap(chunk -> chunk.choices().stream())// get choices from each chunk
		    	 .flatMap(choice -> choice.delta().content().stream())//extract newly generated text from each choice
		    	 .forEach(System.out::print);
		     }
	      System.out.println();
	      
	      
	}

}
