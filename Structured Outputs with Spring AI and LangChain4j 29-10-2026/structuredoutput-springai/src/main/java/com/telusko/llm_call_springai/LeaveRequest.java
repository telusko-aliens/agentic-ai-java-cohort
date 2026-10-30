package com.telusko.llm_call_springai;

import org.springframework.context.annotation.Description;

@Description("Leave details taken from an employee's email")
public record LeaveRequest(
		
@Description("true only if the email is really asking for leave, otherwise false")
boolean askingForLeave,

@Description("Full name of the employee who wants the leave")
String employeeName,

// This enum restricts the allowed JSON values to SICK/CASUAL/EARNED.
LeaveType leaveType,

@Description("First day of leave in yyyy-MM-dd format")
String startDate,

@Description("Last day of leave in yyyy-MM-dd format")
String endDate,

@Description("Reason for the leave in a few words")
String reason) {
}
