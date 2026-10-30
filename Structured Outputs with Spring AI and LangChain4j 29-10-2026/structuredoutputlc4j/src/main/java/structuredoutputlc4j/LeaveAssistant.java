package structuredoutputlc4j;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import structuredoutputlc4j.util.LeaveRequest;

public interface LeaveAssistant 
{
    @SystemMessage("Extract the leave details from the email. Today's date is {{today}}.")
    LeaveRequest extract(@UserMessage String email, @V("today") String today);
    
  

}
