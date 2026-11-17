package com.telusko;

import com.telusko.services.CourseServices;
import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args)
    {
        CourseServices courseServices = new CourseServices();

        // Configure communication
        StdioServerTransportProvider transport = new StdioServerTransportProvider(McpJsonDefaults.getMapper());
        
        // Create MCP server

        McpSyncServer server = McpServer.sync(transport)
                .serverInfo("course-mcp-server", "1.0.0")
                .capabilities(
                        McpSchema.ServerCapabilities.builder()
                                .tools(true)
                                .build()).build();

        // Define first tool input schema
        Map<String, Object> emptySchema = Map.of("type", "object", "properties", Map.of());

        // create listCourses tool // register this as an mcp tool
        var listCoursesTool = McpServerFeatures.SyncToolSpecification.builder()
                .tool(
                        McpSchema.Tool.builder("listCourses", emptySchema)
                                .description("List all available courses")
                                .build()
                )
                .callHandler((exchange, request) ->
                {
                    String courses = courseServices.listCourses();
                    return result(courses);
                })
                .build();
        server.addTool(listCoursesTool);

        //input schema for tools
        Map<String, Object> courseSchema = Map.of(
                "type", "object",
                "properties", Map.of(
                        "courseName", Map.of(
                                "type", "string",
                                "description", "Name of the course"
                        )),// The client must supply courseName.
                "required", List.of("courseName")
        );

        var priceTool = McpServerFeatures.SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder(
                                "getCoursePrice", courseSchema)
                        .description("Get the price of a course")
                        .build())
                .callHandler((exchange, request) -> {

                    // Read the course name from tool arguments.
                    String name = (String)
                            request.arguments().get("courseName");

                    // Execute the actual Java method.
                    String price =
                            courseServices.getCoursePrice(name);

                    // Convert Java String into MCP tool response.
                    return result(price);
                }).build();

        // Expose the tool to MCP clients.
        server.addTool(priceTool);

        var durationTool = McpServerFeatures.SyncToolSpecification.builder()
                .tool(McpSchema.Tool.builder(
                                "getCourseDuration", courseSchema)
                        .description("Get the duration of a course")
                        .build())
                .callHandler((exchange, request) -> {

                    // Extract courseName from the request.
                    String name = (String)
                            request.arguments().get("courseName");

                    // Execute the actual Java method.
                    String duration = courseServices.getCourseDuration(name);

                    // Return the result to the MCP client.
                    return result(duration);
                }).build();

        // Expose the tool to MCP clients.
        server.addTool(durationTool);

        System.err.println("Course MCP Server ready");

    }
    private static McpSchema.CallToolResult result(String text) {

        return McpSchema.CallToolResult.builder()

                // MCP tool results contain content blocks.
                // TextContent represents a text response.
                .content(
                        List.of(
                                new McpSchema.TextContent(text)
                        )
                )

                .build();


    }

}