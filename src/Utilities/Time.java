package Utilities;

import javafx.scene.control.SpinnerValueFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Time {


    /**
     * This method returns Spinner values containing time incremented in 15 minute intervals
     * @return SpinnerValueFactory
     */
    public static SpinnerValueFactory<LocalTime> populateTimeSpinner() {
        SpinnerValueFactory<LocalTime> factory = new SpinnerValueFactory<LocalTime>() {

            {
                setValue(defaultValue());
            }

            private LocalTime defaultValue() {
                return LocalTime.now().truncatedTo(ChronoUnit.HOURS);
            }

            @Override
            public void decrement(int steps) {
                LocalTime value = getValue();
                setValue(value == null ? defaultValue() : value.minusMinutes(15));
            }

            @Override
            public void increment(int steps) {
                LocalTime value = getValue();
                setValue(value == null ? defaultValue() : value.plusMinutes(15));
            }
        };
        return factory;
    }

    /**
     * Method takes a LocalDateTime object declares it at UTC and then converts
     * it to local system time, formats the date (mm/dd/yy 00:00) and outputs a string.
     * @param ldt
     * @return local time in String format
     */
    public static LocalDateTime utcToLocalTime(LocalDateTime ldt) {
        ZonedDateTime startZdt = ZonedDateTime.of(ldt, ZoneOffset.UTC);
        ZonedDateTime local = startZdt.withZoneSameInstant(ZoneId.systemDefault());
        return local.toLocalDateTime();
    }

    public static String ToTimeString(LocalDateTime ldt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return formatter.format(ldt);
    }

    /**
     * Method returns the week of the year
     * @param ldt LocalDateTime
     * @return int week of the year
     */
    public static int getWeekOfYear(LocalDateTime ldt) {
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        return ldt.get(weekOfYear);
    }

    /**
     * Method compares start and end times with specified business hours in EST
     * @param start start time
     * @param end end time
     * @return boolean
     */
    public static boolean inBusinessHours(ZonedDateTime start, ZonedDateTime end) {

        ZonedDateTime startEst = start.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime endEst = end.withZoneSameInstant(ZoneId.of("America/New_York"));

        // Return false if time falls outside of business hours 8:00 - 22:00PM
        if (startEst.getHour() < 8 || startEst.getHour() >= 22) return false;
        if (endEst.getHour() < 8 || endEst.getHour() >= 22) return false;

        // Return false if times fall on the weekend
        if (startEst.getDayOfWeek() == DayOfWeek.SUNDAY || startEst.getDayOfWeek() == DayOfWeek.SATURDAY) return false;
        if (endEst.getDayOfWeek() == DayOfWeek.SUNDAY || endEst.getDayOfWeek() == DayOfWeek.SATURDAY) return false;
        return true;
    }
}
