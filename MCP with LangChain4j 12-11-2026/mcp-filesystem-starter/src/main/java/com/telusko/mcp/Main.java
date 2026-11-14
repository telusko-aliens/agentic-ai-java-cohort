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
        System.out.println("MCP Filesystem Demo");
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .build();

        McpTransport transport = StdioMcpTransport.builder()
                .command(List.of(
                        "cmd", "/c",
                        "npx", "-y",
                        "@modelcontextprotocol/server-filesystem",
                        "--read-only",
                        "D:\\mcp-demo-files"
                ))
                .logEvents(true)
                .build();

        McpClient mcpClient = DefaultMcpClient.builder()
                .key("filesystem-server")
                .transport(transport)
                .build();

        List<ToolSpecification> tools = mcpClient.listTools();
        for(ToolSpecification tool : tools)
        {
            System.out.println("Tool: " + tool.name());
            System.out.println("Description: " + tool.description());
            System.out.println("Parameters: " + tool.parameters());
        }


        McpToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();

        FileAssistant assistant = AiServices.builder(FileAssistant.class)
                .chatModel(model)
                .toolProvider(toolProvider)
                .build();
        String answer=assistant.chat("Are there any .txt files in the folder?");
        System.out.println(answer);

    }
}
