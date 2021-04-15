package Utilities;

import javafx.scene.control.Alert;

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

    public static void errorBox(String error, String suggestion) {

        /* Create alert dialog box */
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Error");
        alert1.setHeaderText(error);
        alert1.setContentText(suggestion);
        alert1.showAndWait();
    }
}
