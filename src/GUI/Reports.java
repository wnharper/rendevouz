package GUI;

import DBAccess.DBAppointments;
import DBAccess.DBContacts;
import Model.Appointment;
import Model.Contact;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * <h2>Reports</h2>
 * The Reports class defines the functions for the reports screen.
 * A user can view various reports in chart and table form, related to
 * appointments.
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-20
 */

public class Reports implements Initializable {

    // Appointments table
    @FXML private ComboBox<Contact> contacts;
    @FXML private TableView<Appointment> appTable;
    @FXML private TableColumn<Appointment, Integer> id;
    @FXML private TableColumn<Appointment, String> title;
    @FXML private TableColumn<Appointment, String> description;
    @FXML private TableColumn<Appointment, String> location;
    @FXML private TableColumn<Appointment, String> contact;
    @FXML private TableColumn<Appointment, String> type;
    @FXML private TableColumn<Appointment, LocalDateTime> start;
    @FXML private TableColumn<Appointment, LocalDateTime> end;
    @FXML private TableColumn<Appointment, String> customer;

    // Charts
    @FXML private StackedBarChart<String, Number> monthChart;
    @FXML private CategoryAxis x;
    @FXML private NumberAxis y;
    @FXML private BarChart<?, ?> typeChart;
    @FXML private CategoryAxis x1;
    @FXML private NumberAxis y1;
    @FXML private BarChart<?, ?> locationChart;
    @FXML private CategoryAxis x2;
    @FXML private NumberAxis y2;

    // user section
    @FXML private Label user;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private FilteredList<Appointment> filteredAppointments;


    // Initialize method, required in order for the UI/Scene to launch and function
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Charts




        for (XYChart.Series series : DBAppointments.appointmentCountData()) { // iterate through Arraylist to add month/type data
            monthChart.getData().add(series);
        }

        typeChart.getData().addAll(DBAppointments.appointmentTypeData());
        locationChart.getData().addAll(DBAppointments.appointmentLengthData());

        // set user name
        user.setText(Login.currentUser.getUsername());

        // Initialize the appointments table
        id.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        description.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        location.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        contact.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contact"));
        type.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        start.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("start"));
        end.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("end"));
        customer.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customer"));

        appointments.addAll(DBAppointments.getAllAppointments());
        // Wrap the ObservableList appointments in a FilteredList
        filteredAppointments = new FilteredList<>(appointments, b -> true);
        appTable.setItems(appointments);

        // Date time formatter for start/end columns
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd hh:mm a");

        // Custom formatting of the start column
        start.setCellFactory(column -> {
            return new TableCell<Appointment, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date and time.
                        setText(formatter.format(item));
                    }
                }
            };
        });

        // Custom formatting of the end column
        end.setCellFactory(column -> {
            return new TableCell<Appointment, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date and time.
                        setText(formatter.format(item));
                    }
                }
            };
        });

        // Populate contacts combo box
        contacts.getItems().addAll(DBContacts.getAllContacts());


        // Set the filter Predicate whenever the filter changes
        contacts.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredAppointments.setPredicate(appointment -> {

                // If the search box is empty, display all customers
                if (newValue == null) {
                    return true;
                }

                // compare customer attributes with all customers in list
                int contactId = newValue.getId();

                if (appointment.getContactId() == contactId) {
                    return true; // filter matches customer name
                }
                else
                    return false; // no match found
            });
        });
        // Wrap the FilteredList in a SortedList.
        SortedList<Appointment> sortedData = new SortedList<>(filteredAppointments);

        // Bind the SortedList comparator to the appointments table comparator
        sortedData.comparatorProperty().bind(appTable.comparatorProperty());

        // Load sorted and filtered data into appointments table
        appTable.setItems(sortedData);

        // Sort table by appointment start date/time
        appTable.getSortOrder().setAll(start);

        // Set focus to name box
        Platform.runLater(()->contacts.requestFocus());
    }


    /**
     * This method switches to the Customer scene
     */
    public void customers(ActionEvent event) throws IOException
    {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("customers.fxml"));
        Scene scene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This method switches to the appointments scene
     */
    public void appointments(ActionEvent event) throws IOException
    {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("appointments.fxml"));
        Scene scene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This method logs a user out
     */
    public void logOut(ActionEvent event) throws IOException
    {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

}
