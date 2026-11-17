package com.telusko.mcp;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.List;

public class Main {
    public static void main(String[] args)
    {
        System.out.println("MCP Client / host app Demo");
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .build();
        System.out.println("chat model created");

//                McpTransport transport = StdioMcpTransport.builder()
//                .command(List.of(
//                        "cmd", "/c",
//                        "npx", "-y",
//                        "@modelcontextprotocol/server-filesystem",
//                        "--read-only",
//                        "D:\\mcp-demo-files"
//                ))
//                .logEvents(true)
//                .build();

        McpTransport transport = StdioMcpTransport.builder()
                .command(List.of(
                        "java",
                        "-jar",
                        // Build the server first: mvn package in custom-mcpserver
                        "../custom-mcpserver/target/custom-mcpserver-1.0-SNAPSHOT.jar"
                ))
                .logEvents(true)
                .build();
        System.out.println("Transport created");

        McpClient mcpClient = DefaultMcpClient.builder()
                .key("course-mcp-server")
                .transport(transport)
                .build();

        System.out.println("MCP Client is ready");

//        List<ToolSpecification> tools = mcpClient.listTools();
//        for(ToolSpecification tool : tools)
//        {
//            System.out.println("Tool: " + tool.name());
//            System.out.println("Description: " + tool.description());
//            System.out.println("Parameters: " + tool.parameters());
//        }


        McpToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();

//        FileAssistant assistant = AiServices.builder(FileAssistant.class)
//                .chatModel(model)
//                .toolProvider(toolProvider)
//                .build();
        CourseAssistant assistant = AiServices.builder(CourseAssistant.class)
                .chatModel(model)
                .toolProvider(toolProvider)
                .build();
        String answer=assistant.chat("What is duration and cost price of devOps course");
        System.out.println(answer);

    }
}
