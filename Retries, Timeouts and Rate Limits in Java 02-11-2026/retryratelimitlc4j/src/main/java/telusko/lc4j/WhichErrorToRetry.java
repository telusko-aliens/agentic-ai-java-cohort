package telusko.lc4j;

import java.time.Duration;

import dev.langchain4j.exception.NonRetriableException;
import dev.langchain4j.exception.RetriableException;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class WhichErrorToRetry 
{

	public static void main(String[] args)
	{
		//RetriableException
				//-->TimeoutException 
				//-->RateLimitException 
				//-->InternalServerException
				
				
				//NonRetriableException
				// --> AuthenticationException
				//--> InvalidRequestException
				// --> ModelNotFoundException
		check("Wrong API Key", "sk-wrongkeynk bn ", "gpt-5.4-mini", Duration.ofSeconds(40));
		check("Wrong Model Name", System.getenv("OPENAI_API_KEY"), "telusko-ai-gpt", Duration.ofSeconds(40));

		check("Time out ", System.getenv("OPENAI_API_KEY"), "gpt-5.4-mini", Duration.ofSeconds(2));
	
			

	}
			
			static void check(String mistake, String apiKey, String modelName, Duration timeout)
			{
			     ChatModel model = OpenAiChatModel.builder()
			                .apiKey(apiKey)
			                .modelName(modelName)
			                .timeout(timeout)
			                .maxRetries(0)
			                .build();
			     
			     System.out.println("mistake : "+ mistake);
			     try
			     {
			    	 String result=model.chat("Write 300 word essay on the history of Java");
			    	 System.out.println(result);
			    	 System.out.println();
			    	 System.out.println("*************************************************");
			    	 
			    	 System.out.println("Smooth flow with no error");
			    	 
			     }
			     catch(RetriableException e)
			     {
			            System.out.println("  error  : " + e.getClass().getSimpleName());
			            System.out.println("  action : RETRY (wait a little and try again)");


			    	 
			     }
			     catch(NonRetriableException e)
			     {
			            System.out.println("  error  : " + e.getClass().getSimpleName());

			            System.out.println("  action : DO NOT RETRY (fix it / show the user a message)");

			    	 
			     }
				
			}
		


}
