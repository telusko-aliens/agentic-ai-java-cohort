package telusko.lc4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialResponse;
import dev.langchain4j.model.chat.response.PartialResponseContext;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

public class LaunchStreamError
{
	
    static String question = "Write a detailed 600 word article on the history of Java."
    		+ "";

    static long start;
    
    
    public static void main(String[] args)
    {        

       
       
       System.out.println("====Streaming======");
       
        OpenAiStreamingChatModel streaming = OpenAiStreamingChatModel.builder()
     		.apiKey(System.getenv("OPENAI_API_KEY"))
             .modelName("gpt-5.4-mini")
             .build();
        
        StringBuilder partialAnswer = new StringBuilder();

        
        CompletableFuture<Boolean> done =new CompletableFuture<>();
        
        start = System.currentTimeMillis();
        
        streaming.chat(question,
        		
        		 new StreamingChatResponseHandler()
        		 {
        	
        	 		@Override
        	 		public void onPartialResponse(PartialResponse partialResponse, PartialResponseContext context)
        	 		{
        	 			System.out.print(partialResponse.text());

        	 			partialAnswer.append(partialResponse.text());
        	 			
                        if (System.currentTimeMillis() - start > 5000) {
                            context.streamingHandle().cancel();
                            System.out.println("\n\n>>> Stream broke: connection closed after 5 seconds");
                            done.complete(false);//streaming unsuccessful
                        }
        	 			
        	 		}

					@Override
					public void onCompleteResponse(ChatResponse completeResponse)
					{
						 done.complete(true);//streaming successful
						
					}

					@Override
					public void onError(Throwable error)
					{
					    System.out.println(
			                    "\n\n>>> Streaming error: " + error.getMessage()
			                );
						
						done.complete(false);//streaming unsuccessful
					}
        	
        		 }
        		
        		);
        
                  Boolean finished =done.join();
                  
                  System.out.println("\nReceived before the break : " + partialAnswer.length() + " characters");
                  System.out.println("Answer complete?          : " + finished);

                  if (finished) {
                      return;
                  }
                  System.out.println("===retry===");
                  
                  CompletableFuture<ChatResponse> retryDone = new CompletableFuture<>();
                  streaming.chat(
                		  List.of(UserMessage.from(question),
                				  AiMessage.from(partialAnswer.toString()),
                				  UserMessage.from("Continue exactly from where you stopped. Do not repeat anything.")
                				  )
                		  ,
                 		 new StreamingChatResponseHandler()
                 		 {
                 	
                 	 		@Override
                 	 		public void onPartialResponse(String partialResponse) {
                 	 		
                 	 					System.out.print(partialResponse);
                 	 			}

         					@Override
         					public void onCompleteResponse(ChatResponse completeResponse)
         					{
         						 retryDone.complete(completeResponse);
         						
         					}

         					@Override
         					public void onError(Throwable error)
         					{
         						
         						retryDone.completeExceptionally(error);
         					}
                 	
                 		 }
                 		
                 		);
                 
                  retryDone.join();
                  System.out.println();

		
	}

}
