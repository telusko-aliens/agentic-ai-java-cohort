package com.telusko.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telusko.service.RagService;

@RestController
@RequestMapping("/api-rag")
public class RagController 
{
    private RagService service;

    public RagController(RagService service)
    {
        this.service = service;
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String question)
    {
        return service.ask(question);
    
    }
       

}
