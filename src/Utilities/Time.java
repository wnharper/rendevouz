package Utilities;

import javafx.scene.control.SpinnerValueFactory;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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
}
