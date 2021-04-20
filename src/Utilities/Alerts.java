package Utilities;

import javafx.scene.control.*;

/**
 * <h2>Alerts</h2>
 * The alerts class provides predefined methods in order to display
 * alerts or warning dialog boxes in the JavaFX UI
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-04
 */

public class Alerts {

    /**
     * This method created cancellation confirmation dialog box
     * @param cancelObject
     * @return Alert
     */
    public static Alert cancelConfirm(String cancelObject) {

        /* Create alert dialog box */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Confirmation");
        alert.setHeaderText("Cancel " + cancelObject);
        alert.setContentText("Are you sure you?");
        return alert;
    }

    /**
     * This method creates a delete confirmation dialog box
     * @param deleteMessage
     * @return Alert
     */
    public static Alert deleteConfirm(String deleteMessage) {

        /* Create alert dialog box */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(deleteMessage);
        alert.setContentText("Are you sure you?");
        return alert;
    }

    /**
     * Method creates a error message dialog box
     * @param error The type of error
     * @param suggestion How to fix the error
     */
    public static void errorBox(String error, String suggestion) {

        /* Create alert dialog box */
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Error");
        alert1.setHeaderText(error);
        alert1.setContentText(suggestion);
        alert1.showAndWait();
    }

    /**
     * Method displays error message if there is no user input
     * @param field      field to be checked
     * @param errorLabel label to display message
     * @param error      error message to be displayed
     * @return true if field is empty
     */
    public static boolean isFieldEmpty(TextField field, Label errorLabel, String error) {
        if (field.getText().length() == 0) {
            field.setStyle("-fx-border-color: red; -fx-border-width: 0 0 2 0;");
            errorLabel.setText(error);
            return true;
        } else {
            field.setStyle(null);
            errorLabel.setText("");
            return false;
        }
    }

    /**
     * Method displays an error message if user has not made a selection
     * @param comboBox
     * @param errorLabel
     * @param error
     * @return
     */
    public static boolean isSelectionEmpty(ComboBox comboBox, Label errorLabel, String error) {
        if (comboBox.getSelectionModel().isEmpty()) {
            comboBox.setStyle("-fx-border-color: red; -fx-border-width: 0 0 2 0;");
            errorLabel.setText(error);
            return true;
        } else {
            comboBox.setStyle(null);
            errorLabel.setText("");
            return false;
        }
    }

    /**
     * Method displays an error if user has not selected a date
     * @param datePicker
     * @param errorLabel
     * @param error
     * @return
     */
    public static boolean isDateSelected(DatePicker datePicker, Label errorLabel, String error) {
        if (datePicker.getValue() == null) {
            datePicker.setStyle("-fx-border-color: red; -fx-border-width: 0 0 2 0;");
            errorLabel.setText(error);
            return true;
        } else {
            datePicker.setStyle(null);
            errorLabel.setText("");
            return false;
        }
    }


}
