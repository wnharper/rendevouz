package GUI;

import DBAccess.DBAppointments;
import Model.Appointment;
import Utilities.Time;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class Reports implements Initializable {

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

    // Appointments Bar Chart
    @FXML private BarChart<?, ?> monthChart;
    @FXML private CategoryAxis x;
    @FXML private NumberAxis y;
    @FXML private BarChart<?, ?> typeChart;
    @FXML private CategoryAxis x1;
    @FXML private NumberAxis y1;

    // Toggles
    @FXML private Toggle week;
    @FXML private Toggle month;
    @FXML private ToggleGroup timeSelect;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private FilteredList<Appointment> filteredAppointments;


    // Initialize method, required in order for the UI/Scene to launch and function
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Appointment count charts
        monthChart.getData().addAll(DBAppointments.appointmentCountData());
        typeChart.getData().addAll(DBAppointments.appointmentTypeData());

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
                Predicate<Appointment> weekPred = i -> i.getStart().getMonth() == dateTime.getMonth();
                filteredAppointments.setPredicate(weekPred);
            }
        }));

        // Select week toggle filter
        timeSelect.selectToggle(this.week);

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

}
