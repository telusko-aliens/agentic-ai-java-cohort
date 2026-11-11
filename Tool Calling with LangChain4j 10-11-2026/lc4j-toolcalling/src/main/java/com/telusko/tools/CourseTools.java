package com.telusko.tools;

import dev.langchain4j.agent.tool.Tool;

public class CourseTools
{
    @Tool("Get the price of a training course")
    public String getCoursePrice(String courseName)
    {
        System.out.println(courseName);

        System.out.println("Tool called: getCoursePrice");
        if (courseName.equalsIgnoreCase("Java Course")) {
            return "3000 INR";
        }
       else if (courseName.equalsIgnoreCase("Python Course")) {
            return "2500 INR";
        }
        else if (courseName.equalsIgnoreCase("AI Course")) {
            return "4500 INR";
        }
        return "Course price not available";
    }
    @Tool("Get the duration of a training course")
    public String getCourseDuration(String courseName)
    {

        System.out.println(courseName);
        System.out.println("Tool called: getCourseDuration");
        if (courseName.equalsIgnoreCase("Java Course")) {
            return "3 months";
        }
        if (courseName.equalsIgnoreCase("Python Course")) {
            return "2 months";
        }
        if (courseName.equalsIgnoreCase("AI Course")) {
            return "4 months";
        }
        return "Course duration not available";
    }
}
