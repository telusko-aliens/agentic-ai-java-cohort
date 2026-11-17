package com.telusko.services;

public class CourseServices
{
    // Tool 1: List all available courses
    public String listCourses() {
        return "Available courses: Java, Python, DevOps";
    }

    // Tool 2: Return the price of a course
    public String getCoursePrice(String courseName) {

        return switch (courseName.toLowerCase()) {
            case "java" -> "Java course costs 3000 INR";
            case "python" -> "Python course costs 2500 INR";
            case "devops" -> "DevOps course costs 4000 INR";
            default -> "Course not found";
        };
    }

    // Tool 3: Return the duration of a course
    public String getCourseDuration(String courseName) {

        return switch (courseName.toLowerCase()) {
            case "java" -> "Java course duration is 3 months";
            case "python" -> "Python course duration is 2 months";
            case "devops" -> "DevOps course duration is 4 months";
            default -> "Course not found";
        };
    }
}
