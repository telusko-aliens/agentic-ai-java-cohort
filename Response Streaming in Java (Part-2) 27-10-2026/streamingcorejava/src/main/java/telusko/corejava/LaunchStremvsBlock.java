package telusko.corejava;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.http.StreamResponse;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionChunk;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class LaunchStremvsBlock
{
	
	static OpenAIClient client = OpenAIOkHttpClient.fromEnv();
	
	static long start;
	static long firstTextAT=0;
	
	public static void main(String[] args)
	{
		
	      ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
	                // Select the model that will generate the answer.
	                .model("gpt-5.4-mini")
	                .addUserMessage("Explain the Java Collections Framework in 5 short points.")
	                .build();
	      
	      System.out.println("============BLOCKING==============");
	      start=System.currentTimeMillis();
	      
	      ChatCompletion completion = client.chat().completions().create(params);
	      
	     long blockingTime= System.currentTimeMillis() - start;
	     
	     String answer1=completion.choices().get(0).message().content().orElse("");
	     System.out.println(answer1);
	     
	     System.out.println("============Streaming==============");
	      start=System.currentTimeMillis();
	      
	      //choices == ai generated answer options
	     try( StreamResponse<ChatCompletionChunk> streaming = client.chat().completions().createStreaming(params))
	     {
	    	 streaming.stream()
	    	 .flatMap(chunk -> chunk.choices().stream())// get choices from each chunk
	    	 .flatMap(choice -> choice.delta().content().stream())//extract newly generated text from each choice
	    	 .forEach(text ->
	    	 {
	    		 if(firstTextAT==0)
	    		 {
	    			 firstTextAT = System.currentTimeMillis() - start;
	    		 }
	    		 System.out.print(text);
	    	 }
	    	 );
	    	 
	    	 
	    	 long streamingTime = System.currentTimeMillis() - start;
	    	 System.out.println();
	         System.out.println("Blocking  : user saw the first word after " + blockingTime + " ms");
	         System.out.println("Streaming : user saw the first word after " + firstTextAT + " ms");
	         System.out.println("Streaming : full answer finished after    " + streamingTime + " ms");
	     
	    	 
	    	 
	     }
	      
	      
	      
	}

}
