package com.telusko.llm_call_springai;

import java.util.List;

public record LeaveDecision
(LeaveRequest leave, boolean accepted, List<String> problems) {
}
