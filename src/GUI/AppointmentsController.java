package GUI;

import DBAccess.DBAppointments;
import Model.Appointment;
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
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AppointmentsController implements Initializable {

    @FXML
    private TableView<Appointment> appTable;

    @FXML
    private TableColumn<Appointment, Integer> id;

    @FXML
    private TableColumn<Appointment, String> title;

    @FXML
    private TableColumn<Appointment, String> description;

    @FXML
    private TableColumn<Appointment, String> location;

    @FXML
    private TableColumn<Appointment, String> contact;

    @FXML
    private TableColumn<Appointment, String> type;

    @FXML
    private TableColumn<Appointment, String> start;

    @FXML
    private TableColumn<Appointment, String> end;

    @FXML
    private TableColumn<Appointment, String> customer;

    @FXML
    private TextField search;

    @FXML
    private Button add;

    @FXML
    private Button customers_nav;

    @FXML
    private Toggle week;

    @FXML
    private Toggle month;

    @FXML
    private ToggleGroup timeSelect;



    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    FilteredList<Appointment> filteredAppointments;


    // Initialize method, required in order for the UI/Scene to launch and function
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Initialize the appointments table
        id.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        description.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        location.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        contact.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contact"));
        type.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        start.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        end.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
        customer.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customer"));

        appointments.addAll(DBAppointments.getMonthAppointments());
        appTable.setItems(appointments);

        // Initialize Radio buttons
        timeSelect = new ToggleGroup();
        this.week.setToggleGroup(timeSelect);
        this.month.setToggleGroup(timeSelect);
        timeSelect.selectToggle(this.month);

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
                    return true; // filter matches id
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


        /*
         * Radio button functionality
         */
        // Filter appointments occurring in the next 7 days
        week.selectedProperty().addListener(((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                Predicate<Appointment> predicate = i -> i.getDays() < 8;
                filteredAppointments.setPredicate(predicate);
            }
        }));

        // Filter appointments occurring in the next month
        month.selectedProperty().addListener(((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                Predicate<Appointment> weekPred = i -> i.getDays() < 31;
                filteredAppointments.setPredicate(weekPred);
            }
        }));

        // Set focus to search box
        Platform.runLater(() -> search.requestFocus());
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
}
