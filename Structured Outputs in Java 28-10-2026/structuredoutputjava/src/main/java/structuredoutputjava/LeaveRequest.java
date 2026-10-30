package structuredoutputjava;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("Leave details taken from an employee's email")
public record LeaveRequest(

        @JsonPropertyDescription("true only if the email is really asking for leave, otherwise false")
        boolean askingForLeave,

        @JsonPropertyDescription("Full name of the employee who wants the leave")
        String employeeName,

        LeaveType leaveType,

        @JsonPropertyDescription("First day of leave in yyyy-MM-dd format")
        String startDate,

        @JsonPropertyDescription("Last day of leave in yyyy-MM-dd format")
        String endDate,

        @JsonPropertyDescription("Reason for the leave in a few words")
        String reason,
        
        
        @JsonPropertyDescription("Phone model mentionaed in the email. Return null if not mentioned")

        String phoneModel
        )

{
}

