package telusko.lc4j;

import java.util.concurrent.CompletableFuture;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

public class LaunchBlockStreaming
{
    static long start;
    
    static long firstTextAt = 0;
    
    public static void main(String[] args)
    {
        String question = "Explain the Java Collections Framework in 5 short points.";
        
        //Blocking
        
        ChatModel blockmodel = OpenAiChatModel.builder()
        		.apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-5.4-mini")
                .build();
        System.out.println("====BLOCKING======");
        start = System.currentTimeMillis();

        
       String answer= blockmodel.chat(question);
       
       long blockingTime = System.currentTimeMillis() - start;

       
       System.out.println(answer);
       
       
       System.out.println("====Streaming======");
       
        OpenAiStreamingChatModel streaming = OpenAiStreamingChatModel.builder()
     		.apiKey(System.getenv("OPENAI_API_KEY"))
             .modelName("gpt-5.4-mini")
             .build();
        
        CompletableFuture<ChatResponse> done =new CompletableFuture<>();
        
        start = System.currentTimeMillis();
        
        streaming.chat(question,
        		
        		 new StreamingChatResponseHandler()
        		 {
        	
        	 		@Override
        	 		public void onPartialResponse(String partialResponse) {
        	 			if (firstTextAt == 0) {
        	 				firstTextAt = System.currentTimeMillis() - start;
        	 			}
        	 					System.out.print(partialResponse);
        	 			}

					@Override
					public void onCompleteResponse(ChatResponse completeResponse)
					{
						 done.complete(completeResponse);
						
					}

					@Override
					public void onError(Throwable error)
					{
						
						done.completeExceptionally(error);
					}
        	
        		 }
        		
        		);
        
        ChatResponse finalResponse = done.join();
        
        long streamingTime =
                System.currentTimeMillis() - start;
        
        System.out.println();
        
        System.out.println("RESULT of BLOCKING VS STREAMING");
        
        
        System.out.println("Blocking  : user saw the first word after " + blockingTime + " ms");

        System.out.println("Streaming : user saw the first word after " + firstTextAt + " ms");
        System.out.println("Streaming : full answer finished after    " + streamingTime + " ms");



		
	}

}
