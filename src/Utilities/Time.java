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
     * @param ltc
     * @return local time in String format
     */
    public static LocalDateTime utcToLocalTime(LocalDateTime ltc) {
        ZonedDateTime startZdt = ZonedDateTime.of(ltc, ZoneOffset.UTC);
        ZonedDateTime local = startZdt.withZoneSameInstant(ZoneId.systemDefault());
        return local.toLocalDateTime();
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
}
