package com.telusko.tools;


import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class DateTimeTools
{
    // Small location database for classroom demonstration
    private static final Map<String, String> ZONES = Map.ofEntries(
            Map.entry("bangalore", "Asia/Kolkata"),
            Map.entry("bengaluru", "Asia/Kolkata"),
            Map.entry("560001", "Asia/Kolkata"),
            Map.entry("mumbai", "Asia/Kolkata"),
            Map.entry("400001", "Asia/Kolkata"),
            Map.entry("new york", "America/New_York"),
            Map.entry("10001", "America/New_York"),
            Map.entry("los angeles", "America/Los_Angeles"),
            Map.entry("90001", "America/Los_Angeles"),
            Map.entry("london", "Europe/London"),
            Map.entry("tokyo", "Asia/Tokyo"),
            Map.entry("dubai", "Asia/Dubai")
    );

    @Tool("Get the current local date and time for a city or postal code")
    public String getCurrentDateTime(
            @P("City name or postal/ZIP code") String location) {

        System.out.println("Tool called: getCurrentDateTime");
        System.out.println("Location received: " + location);

        // Find the time zone
        String zone = ZONES.get(
                location.toLowerCase().trim()
        );

        if (zone == null) {
            return "Location not supported: " + location;
        }

        // Get current time in the requested time zone
        ZonedDateTime now = ZonedDateTime.now(
                ZoneId.of(zone)
        );

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "EEEE, dd MMMM yyyy, hh:mm:ss a z"
                );

        return "Current date and time in " + location
                + ": " + now.format(formatter);
    }
}

