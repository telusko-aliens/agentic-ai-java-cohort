package telusko.lc4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.output.TokenUsage;

public class LaunchStreamFullResponse
{
	
 
    
    public static void main(String[] args)
    {        

       
       
       System.out.println("====Streaming======");
       
        OpenAiStreamingChatModel streaming = OpenAiStreamingChatModel.builder()
     		.apiKey(System.getenv("OPENAI_API_KEY"))
             .modelName("gpt-5.4-mini")
             .build();
        
        StringBuilder partialAnswer = new StringBuilder();

        
        CompletableFuture<ChatResponse> done =new CompletableFuture<>();
        
       streaming.chat(
                List.of(
                    SystemMessage.from("You are a Java trainer."),
                    UserMessage.from(
                        "Give 3 tips to write clean Java code."
                    )
                    
                ),

                new StreamingChatResponseHandler()
                {
                	
                	@Override
         	 		public void onPartialResponse(String partialResponse)
                	{
         	 		
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
       
       ChatResponse response = done.join();
       TokenUsage usage = response.tokenUsage();

       System.out.println("\n");
       System.out.println("full answer length : " + response.aiMessage().text().length() + " characters");
       System.out.println("finish reason      : " + response.finishReason());
       System.out.println("input tokens       : " + usage.inputTokenCount());
       System.out.println("output tokens      : " + usage.outputTokenCount());
       
		
	}

}
