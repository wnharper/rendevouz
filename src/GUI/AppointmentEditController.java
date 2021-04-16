package GUI;

import DBAccess.DBAppointments;
import DBAccess.DBContacts;
import DBAccess.DBCustomer;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Utilities.Alerts;
import Utilities.Time;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentEditController implements Initializable {

    // Form fields
    @FXML private Button save;

    @FXML private Button cancel;

    @FXML private TextField title;

    @FXML private TextField description;

    @FXML private TextField location;

    @FXML private ComboBox<Contact> contact;

    @FXML private TextField type;

    @FXML private DatePicker start;

    @FXML private DatePicker end;

    @FXML private ComboBox<Customer> customer;

    @FXML private Button customers_nav;

    @FXML private Spinner<LocalTime> startTime;

    @FXML private Spinner<LocalTime> endTime;

    @FXML private Label businessHours;



    // Contact and Customer lists for combo boxes
    private ObservableList<Contact> contactsList = FXCollections.observableArrayList();
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    // stores incoming appointment, customer ID
    private int appointmentId;
    private int customerId;

    // Initialize method, required in order for the UI/Scene to launch and function
    @Override
    public void initialize (URL url, ResourceBundle rb) {


        // Populate contacts combo box
        contactsList.addAll(DBContacts.getAllContacts());
        contact.getItems().addAll(DBContacts.getAllContacts());

        // Populate customers combo box with customer objects
        customerList.addAll(DBCustomer.getAllCustomers());
        customer.getItems().addAll(DBCustomer.getAllCustomers());

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
        Optional<ButtonType> result = Alerts.cancelConfirm("Edit Appointment").showAndWait();
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
     * This method saves the current appointment information
     * @param event
     * @throws IOException
     */
    public void saveAppointment(ActionEvent event) throws IOException
    {

        // Create local time object
        ZonedDateTime startDateTime = ZonedDateTime.of(start.getValue(), startTime.getValue(), ZoneId.systemDefault());
        LocalDateTime startLtc = startDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime(); // Convert to UTC for database
        ZonedDateTime endDateTime = ZonedDateTime.of(end.getValue(), endTime.getValue(), ZoneId.systemDefault());
        LocalDateTime endLtc = endDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime(); // Convert to UTC for database

        // Check that start time is before end time
        if(startDateTime.isAfter(endDateTime)) {
            businessHours.setText("Start time/date must occur before end time/date");
            return;
        }
        // Check if date / time are in business hours
        if (!Time.inBusinessHours(startDateTime, endDateTime)){
            businessHours.setText("Start and end time must fall within business hours, 8AM - 10PM Monday - Friday EST");
            return;
        }

        // Check for overlapping appointment times
        ObservableList<Appointment> customerTimes = DBAppointments.getCustomerAppointments(customerId);
        for (Appointment appt : customerTimes) {
            // Do not compare current appointment
            if (appt.getId() != appointmentId) {
                // Check if start and end times conflict with any other appointments
                if ((appt.getStart().isAfter(startLtc) && appt.getStart().isBefore(endLtc)) || appt.getEnd().isAfter(startLtc) && appt.getEnd().isBefore(endLtc)){
                    businessHours.setText("Appointment time conflicts with another appointment (" + Time.ToTimeString(Time.utcToLocalTime(appt.getStart())) + " - " + Time.ToTimeString(Time.utcToLocalTime(appt.getEnd())) + ")");
                    return;
                }
            }
        }

        // Insert form data into database
        DBAppointments.updateAppointment(
                appointmentId,
                title.getText(),
                description.getText(),
                location.getText(),
                type.getText(),
                startLtc,
                endLtc,
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

    /**
     * Method populates form fields by retrieving attributes
     * from appointment object
     * @param appointment
     */
    public void populateFields(Appointment appointment) {

        // Extract data from customer object
        title.setText(appointment.getTitle());
        description.setText(appointment.getDescription());
        location.setText(appointment.getLocation());
        type.setText(appointment.getType());
        start.setValue(appointment.getStart().toLocalDate());
        startTime.getValueFactory().setValue(appointment.getStart().toLocalTime());
        end.setValue(appointment.getEnd().toLocalDate());
        endTime.getValueFactory().setValue(appointment.getEnd().toLocalTime());

        // Iterate through list and select customer via incoming customer ID
        for (Contact cont : contactsList) {
            if (cont.getId() == appointment.getContactId()) {
                contact.getSelectionModel().select(cont);
            }
        }

        // Iterate through list and select customer via incoming customer ID
        for (Customer cust : customerList) {
            if (cust.getId() == appointment.getCustomerId()) {
                customer.getSelectionModel().select(cust);
            }
        }

        // Set Appointment ID
        appointmentId = appointment.getId();
        customerId = appointment.getCustomerId();

    }

}
