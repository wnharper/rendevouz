package GUI;

import DBAccess.DBCustomer;
import Model.Customer;
import Utilities.Alerts;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> id;

    @FXML
    private TableColumn<Customer, String> name;

    @FXML
    private TableColumn<Customer, String> address;

    @FXML
    private TableColumn<Customer, String> country;

    @FXML
    private TableColumn<Customer, Integer> appointments;

    @FXML
    private TextField search;

    @FXML
    private Button add;

    @FXML
    private Button appointments_nav;

    private ObservableList<Customer> customers = FXCollections.observableArrayList();


    // Initialize method, required in order for the UI/Scene to launch and function
    @Override
    public void initialize (URL url, ResourceBundle rb) {

        // Initialize the customers table
        id.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        address.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        country.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        appointments.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("appointments"));


        customers.addAll(DBCustomer.getAllCustomers());
        customerTable.setItems(customers);

        /*
         * Search function for customers table
         */

        // Wrap the ObservableList customers in a FilteredList
        FilteredList<Customer> filteredCustomers = new FilteredList<>(customers, b -> true);

        // Set the filter Predicate whenever the filter changes
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredCustomers.setPredicate(customer -> {

                // If the search box is empty, display all customers
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // compare customer attributes with all customers in list
                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // filter matches customer name
                } else if (Integer.toString(customer.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // filter matches id
                } else if (customer.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // filter matches address
                } else if (customer.getCountry().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // filter matches country
                } else if (customer.getState().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // filter matches state
                }
                else
                    return false; // no match found
            });
        });
        // Wrap the FilteredList in a SortedList.
        SortedList<Customer> sortedData = new SortedList<>(filteredCustomers);

        // Bind the SortedList comparator to the customers table comparator
        sortedData.comparatorProperty().bind(customerTable.comparatorProperty());

        // Load sorted and filtered data into parts table
        customerTable.setItems(sortedData);

        // Set focus to search box
        Platform.runLater(()->search.requestFocus());
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
     * This method switches to the Add Customer scene
     */
    public void addCustomer(ActionEvent event) throws IOException
    {
        Parent sceneParent = FXMLLoader.load(getClass().getResource("customerAdd.fxml"));
        Scene scene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This method switches to the edit customer scene
     */
    public void editCustomer(ActionEvent event) throws IOException
    {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("customerEdit.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        // Load and access the controller
        CustomerEditController controller = loader.getController();

        // populate form fields using part ID
        controller.populateFields(customerTable.getSelectionModel().getSelectedItem());

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.show();
    }

    /**
     * This method deletes a customer from a the customer table
     */
    public void deleteCustomer() {

        // Create alert dialog box
        if (customerTable.getSelectionModel().getSelectedItem().getAppointments() > 0) {
            Alerts.errorBox("Customer has active appointments!", "Remove customer's appointments and try again.");
            return;
        }

        // Delete confirmation
        Optional<ButtonType> result = Alerts.deleteConfirm("Delete selected customer?").showAndWait();
        // Check if user selected "OK"
        if (result.get() == ButtonType.OK) {
            // Delete selected row
            DBCustomer.deleteCustomer(customerTable.getSelectionModel().getSelectedItem().getId());
            customers.remove(customerTable.getSelectionModel().getSelectedItem());
        }
    }

}
