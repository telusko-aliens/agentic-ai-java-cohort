package structuredoutputjava;

public class Emails {

    public static final String SICK_LEAVE = """
            Hi Neha,
            I have viral fever since last night and the doctor has advised rest.
            Please grant me sick leave from 28 November to 30 November 2026.
            I will share the medical certificate.
            Thanks,
            Rahul Sharma
            """;

    public static final String TEAM_PLAN = """
            Hi HR,
            Leave plan for Diwali week:
            Priya Nair will take earned leave from 9 Nov to 13 Nov 2026 to visit her family in Kochi.
            Arjun Mehta needs casual leave on 10 November 2026 for some personal work.
            Regards,
            Neha (Team Lead)
            """;

    public static final String TOO_LONG = """
            Hello HR,
            I am getting married in December, so I want earned leave
            from 1 December to 24 December 2026.
            Regards,
            Karan Verma
            """;

    public static final String PAST_DATE = """
            Hi Neha,
            I was not well, so I could not come to office on 20 and 21 October 2026.
            Please mark it as sick leave.
            Thanks,
            Sneha Iyer
            """;

    public static final String NOT_A_LEAVE = """
            Hi Neha,
            Can we move tomorrow's sprint review to 4 PM? I have a client call at 11.
            Thanks,
            Rahul Sharma
            """;
}
