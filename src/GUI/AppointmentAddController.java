package GUI;

import DBAccess.DBAppointments;
import DBAccess.DBContacts;
import DBAccess.DBCustomer;
import Model.Contact;
import Model.Customer;
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
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.*;
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
    private ComboBox<Contact> contact;

    @FXML
    private TextField type;

    @FXML
    private DatePicker start;

    @FXML
    private DatePicker end;

    @FXML
    private ComboBox<Customer> customer;

    @FXML
    private Button customers_nav;

    @FXML
    private Spinner<LocalTime> startTime;

    @FXML
    private Spinner<LocalTime> endTime;

    @FXML
    private Label businessHours;

    // Initialize method, required in order for the UI/Scene to launch and function
    @Override
    public void initialize (URL url, ResourceBundle rb) {



        // Populate contacts combo box
        contact.getItems().addAll(DBContacts.getAllContacts());
        // Set combo box values to contact name (instead of object reference)
        Callback<ListView<Contact>, ListCell<Contact>> contactfactory = lv -> new ListCell<Contact>() {

            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        contact.setCellFactory(contactfactory);
        contact.setButtonCell(contactfactory.call(null));

        // Populate customers combo box with customer objects
        customer.getItems().addAll(DBCustomer.getAllCustomers());
        // Set combo box values to customer name (instead of object reference)
        Callback<ListView<Customer>, ListCell<Customer>> factory = lv -> new ListCell<Customer>() {

            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        customer.setCellFactory(factory);
        customer.setButtonCell(factory.call(null));

        // populate start time spinner
        startTime.setValueFactory(Time.populateTimeSpinner());

        // populate end time spinner
        endTime.setValueFactory(Time.populateTimeSpinner());

        // Set focus to name box
        Platform.runLater(()->title.requestFocus());

    }

    /**
     * This method switches scenes to the appointments screen
     */
    public void switchToAppointments(ActionEvent event) throws IOException
    {

        // Confirmation button
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


    /**
     * This method uses form details to insert a new customer into the database, and
     * then switches back to the customer table scene
     */
    public void createAppointment(ActionEvent event) throws IOException
    {

        // Create local time object
        ZonedDateTime startDateTime = ZonedDateTime.of(start.getValue(), startTime.getValue(), ZoneId.systemDefault());
        ZonedDateTime endDateTime = ZonedDateTime.of(end.getValue(), endTime.getValue(), ZoneId.systemDefault());

        // Check that start time is before end time
        if(startDateTime.isAfter(endDateTime)) {
            businessHours.setText("Start and end time must fall within business hours, 8AM - 10PM Monday - Friday EST");
            return;
        }
        // Check if date / time are in business hours
        if (!Time.inBusinessHours(startDateTime, endDateTime)){
            businessHours.setText("Start and end time must fall within business hours, 8AM - 10PM Monday - Friday EST");
            return;
        }



        // Insert form data into database
        DBAppointments.insertAppointment(
            DBAppointments.getNewAppointmentId(),
            title.getText(),
            description.getText(),
            location.getText(),
            type.getText(),
            startDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime(), // Convert to UTC for database
            endDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime(),  // Convert to UTC for database
            customer.getValue().getId(),
            2, //TODO input USER ID
            contact.getValue().getId());

        // Load scene
        Parent sceneParent = FXMLLoader.load(getClass().getResource("appointments.fxml"));
        Scene scene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

}
