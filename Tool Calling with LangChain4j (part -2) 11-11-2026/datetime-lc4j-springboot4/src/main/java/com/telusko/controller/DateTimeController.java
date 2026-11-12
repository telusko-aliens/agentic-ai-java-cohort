package com.telusko.controller;

import com.telusko.DateTimeAssistant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/datetime")
public class DateTimeController
{
    private final DateTimeAssistant assistant;

    public DateTimeController(DateTimeAssistant assistant)
    {
        this.assistant = assistant;
    }
    @GetMapping("/time")
    public String getTime(@RequestParam String question)
    {
        return assistant.chat(question);
    }

}
