package GUI;

import DBAccess.DBAppointments;
import Model.Appointment;
import Utilities.Alerts;
import Utilities.Time;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AppointmentsController implements Initializable {

    // Appointments table
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

    // search bar
    @FXML private TextField search;

    // filter toggles
    @FXML private Toggle week;
    @FXML private Toggle month;
    @FXML private Toggle all;
    @FXML private ToggleGroup timeSelect;

    // user section
    @FXML private Label user;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private FilteredList<Appointment> filteredAppointments;


    // Initialize method, required in order for the UI/Scene to launch and function
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

        // Initialize Radio buttons
        timeSelect = new ToggleGroup();
        this.week.setToggleGroup(timeSelect);
        this.month.setToggleGroup(timeSelect);
        this.all.setToggleGroup(timeSelect);


        /*
         * Search function for appointments table
         */

        // Wrap the ObservableList appointments in a FilteredList
        filteredAppointments = new FilteredList<>(appointments, b -> true);

        // Set the filter Predicate whenever the filter changes
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAppointments.setPredicate(appointment -> {

                // If the search box is empty, display all appointments
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // compare title and id with all appointments in list
                String lowerCaseFilter = newValue.toLowerCase();

                if (appointment.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // filter matches appointment title
                } else if (appointment.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // filter description
                } else if (appointment.getCustomer().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // filter matches customer
                } else if (appointment.getContact().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // filter matches contact
                } else if (appointment.getStart().toString().contains(lowerCaseFilter)) {
                    return true; // filter matches start time
                } else if (appointment.getLocation().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // filter matches location
                } else
                    return false; // no match found
            });
        });
        // Wrap the FilteredList in a SortedList.
        SortedList<Appointment> sortedData = new SortedList<>(filteredAppointments);

        // Bind the SortedList comparator to the customers table comparator
        sortedData.comparatorProperty().bind(appTable.comparatorProperty());

        // Load sorted and filtered data into parts table
        appTable.setItems(sortedData);

        LocalDateTime dateTime = LocalDateTime.now();
        /*
         * Radio button functionality
         */
        // Filter appointments occurring in the next 7 days
        week.selectedProperty().addListener(((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                Predicate<Appointment> predicate = i -> Time.getWeekOfYear(i.getStart()) == Time.getWeekOfYear(dateTime);
                filteredAppointments.setPredicate(predicate);
            }
        }));

        // Filter appointments occurring in the next month
        month.selectedProperty().addListener(((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                Predicate<Appointment> predicate = i -> i.getStart().getMonth() == dateTime.getMonth();
                filteredAppointments.setPredicate(predicate);
            }
        }));

        // Show all appointments
        all.selectedProperty().addListener(((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                Predicate<Appointment> predicate = i -> i.getId() >= 0;
                filteredAppointments.setPredicate(predicate);
            }
        }));

        // Select week toggle filter
        timeSelect.selectToggle(this.week);

        // Set focus to search box
        Platform.runLater(() -> search.requestFocus());

        // Sort table by appointment start date/time
        appTable.getSortOrder().setAll(start);
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
     * This method switches to the reports scene
     */
    public void reports(ActionEvent event) throws IOException
    {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("reports.fxml"));
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

    /**
     * This method switches to the create appointment scene
     */
    public void createAppointment(ActionEvent event) throws IOException
    {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("appointmentAdd.fxml"));
        Scene scene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This method deletes an appointment from a the appointment table
     */
    public void deleteAppointment() {

        // Delete confirmation
        Optional<ButtonType> result = Alerts.deleteConfirm("Delete selected appointment?").showAndWait();
        // Check if user selected "OK"
        if (result.get() == ButtonType.OK) {
            // Delete selected row
            DBAppointments.deleteAppointment(appTable.getSelectionModel().getSelectedItem().getId());
            appointments.remove(appTable.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * This method switches to the edit appointments scene
     */
    public void editAppointment(ActionEvent event) throws IOException
    {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("appointmentsEdit.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        // Load and access the controller
        AppointmentEditController controller = loader.getController();

        // populate form fields using appointment object from table
        controller.populateFields(appTable.getSelectionModel().getSelectedItem());

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.show();
    }

    public void upcomingAppointments() {
        //JFXDialogLayout dialogLayout = new JFXDialogLayout();
    }
}
