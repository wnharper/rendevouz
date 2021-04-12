package Utilities;

import javafx.scene.control.Alert;

public class Alerts {

    public static Alert cancelConfirm(String cancelObject) {

        /* Create alert dialog box */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Confirmation");
        alert.setHeaderText("Cancel " + cancelObject);
        alert.setContentText("Are you sure you?");
        return alert;
    }
}
