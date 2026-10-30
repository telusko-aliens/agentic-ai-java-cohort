package structuredoutputjava;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LeaveRules {

    static final int MAX_DAYS = 10;

    public static List<String> check(LeaveRequest leave, LocalDate today) {

        List<String> problems = new ArrayList<>();

        if (!leave.askingForLeave()) {
            problems.add("This email is not a leave request");
            return problems;
        }

        // The model may return null or an empty string. Check both before parsing.
        if (leave.startDate() == null || leave.endDate() == null
                || leave.startDate().isBlank() || leave.endDate().isBlank()) {
            problems.add("Dates are missing");
            return problems;
        }

        LocalDate start;
        LocalDate end;
        try {
            // ISO yyyy-MM-dd text is converted into a real LocalDate.
            start = LocalDate.parse(leave.startDate());
            end = LocalDate.parse(leave.endDate());
        } catch (DateTimeParseException e) {
            problems.add("Dates are not in yyyy-MM-dd format");
            return problems;
        }

        if (end.isBefore(start)) {
            problems.add("End date is before start date");
        }

        if (start.isBefore(today)) {
            problems.add("Leave cannot start in the past");
        }

        // between() excludes the last day, so add 1 for inclusive leave days.
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        if (days > MAX_DAYS) {
            problems.add("Leave is " + days + " days, company limit is " + MAX_DAYS + " days");
        }

        return problems;
    }
}
