# DateTime LangChain4j + Spring Boot 4

Tool calling inside a Spring Boot app. Open `pom.xml` in IntelliJ IDEA as a Maven project.

- Java 21
- Spring Boot 4.0.0
- LangChain4j 1.8.0 (core and OpenAI integration)
- Spring Web MVC

## Start
1. Select JDK 21 in IntelliJ.
2. Set `OPENAI_API_KEY` in the run environment (`application.properties` reads it as `${OPENAI_API_KEY}`).
3. Reload Maven and run `DateTimeApplication`.
4. Call `http://localhost:8080/api/datetime/time?question=What is the time in Tokyo`

## What's inside
- `tools/DateTimeTools.java`: `@Tool` method that returns the current date and time for a city or postal code
- `DateTimeAssistant.java`: AI Service interface with a system message telling the model to use the tool and never guess the time
- `config/AiConfig.java`: `OpenAiChatModel`, `DateTimeTools` and `DateTimeAssistant` as Spring beans
- `controller/DateTimeController.java`: `GET /api/datetime/time?question=...`

Note: This project uses the programmatic `AiServices.builder(...)` approach, not the LangChain4j Spring Boot starter auto-configuration.
