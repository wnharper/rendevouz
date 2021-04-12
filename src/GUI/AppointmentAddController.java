package GUI;

import DBAccess.DBContacts;
import DBAccess.DBCountries;
import DBAccess.DBCustomer;
import DBAccess.DBStates;
import Utilities.Alerts;
import Utilities.Time;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentAddController implements Initializable {

    // Form fields
    @FXML
    private Button create;

    @FXML
    private Button cancel;

    @FXML
    private TextField title;

    @FXML
    private TextField description;

    @FXML
    private TextField location;

    @FXML
    private ComboBox<String> contact;

    @FXML
    private TextField type;

    @FXML
    private DatePicker start;

    @FXML
    private DatePicker end;

    @FXML
    private ComboBox<String> customer;

    @FXML
    private Button customers_nav;

    @FXML
    private Spinner<LocalTime> startTime;

    @FXML
    private Spinner<LocalTime> endTime;

    // Initialize method, required in order for the UI/Scene to launch and function
    @Override
    public void initialize (URL url, ResourceBundle rb) {

        // Populate countries combo box
        customer.getItems().addAll(DBCustomer.getAllCustomersString());

        // Populate contacts combo box
        contact.getItems().addAll(DBContacts.getAllContactsString());

        // populate start time spinner
        startTime.setValueFactory(Time.populateTimeSpinner());

        // populate start time spinner
        endTime.setValueFactory(Time.populateTimeSpinner());


        // Create localdatetime object from datepicker and spinner
        // TODO move to its own method
        //LocalDateTime startDateTime = LocalDateTime.of(start.getValue(), startTime.getValue());



        // Set focus to name box
        Platform.runLater(()->title.requestFocus());

    }

    /**
     * This method switches scenes to the customer screen
     */
    public void switchToAppointments(ActionEvent event) throws IOException
    {
//        /* Create alert dialog box */
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Cancel Confirmation");
//        alert.setHeaderText("Cancel Add Customer");
//        alert.setContentText("Are you sure you?");
        Optional<ButtonType> result = Alerts.cancelConfirm("Create Appointment").showAndWait();
        // Check if user selected "OK"
        if (result.get() == ButtonType.OK) {
            // Switch to scene
            Parent sceneParent = FXMLLoader.load(getClass().getResource("appointments.fxml"));
            Scene scene = new Scene(sceneParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    public void dateTest(ActionEvent event) {
        LocalDate value = end.getValue();
    }

    /**
     * This method uses form details to insert a new customer into the database, and
     * then switches back to the customer table scene
     */
    public void addCustomer(ActionEvent event) throws IOException
    {

        // Insert form data into database
        //DBCustomer.insertCustomer(DBCustomer.getNewCustomerId(), name.getText(), address.getText(), postcode.getText(), phone.getText(), DBStates.getStateId(state.getValue()));

        // Load scene
        Parent sceneParent = FXMLLoader.load(getClass().getResource("customers.fxml"));
        Scene scene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

}
