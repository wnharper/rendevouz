package GUI;

import DBAccess.DBCountries;
import DBAccess.DBCustomer;
import DBAccess.DBStates;
import Utilities.Alerts;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerAddController implements Initializable {

    // Form fields
    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private TextField postcode;
    @FXML private ComboBox<String> country;
    @FXML private ComboBox<String> state;
    @FXML private TextField phone;
    @FXML private Label nameError;
    @FXML private Label addressError;
    @FXML private Label zipError;
    @FXML private Label countryError;
    @FXML private Label stateError;
    @FXML private Label phoneError;

    // Initialize method, required in order for the UI/Scene to launch and function
    @Override
    public void initialize (URL url, ResourceBundle rb) {

        // Populate countries combo box
        country.getItems().addAll(DBCountries.getAllCountriesString());

        // Waits for selection on Country combobox and populates state combobox according to country selection
        country.valueProperty().addListener((obs, oldItem, newItem) -> {

            if (newItem.equals("Canada")) {
                state.getItems().clear();
                state.getItems().addAll(DBStates.getAllStatesString(3));
            } else if (newItem.equals("U.S")) {
                state.getItems().clear();
                state.getItems().addAll(DBStates.getAllStatesString(1));
            } else if (newItem.equals("UK")) {
                state.getItems().clear();
                state.getItems().addAll(DBStates.getAllStatesString(2));
                Platform.runLater(()->state.setPromptText("State"));
            }
        });

        // Set focus to name box
        Platform.runLater(()->name.requestFocus());

    }

    /**
     * This method switches scenes to the customer screen
     */
    public void switchToCustomer(ActionEvent event) throws IOException
    {
        /* Create alert dialog box */
        Optional<ButtonType> result = Alerts.cancelConfirm("Add customer").showAndWait();
        // Check if user selected "OK"
        if (result.get() == ButtonType.OK) {
            // Switch to scene
            Parent sceneParent = FXMLLoader.load(getClass().getResource("customers.fxml"));
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
    public void addCustomer(ActionEvent event) throws IOException
    {

          /*
        Form validation
         */
        if (Alerts.isFieldEmpty(name, nameError, "Enter a name")) return;
        if (Alerts.isFieldEmpty(address, addressError, "Enter an address")) return;
        if (Alerts.isFieldEmpty(postcode, zipError, "Enter a post / zip code")) return;
        if (Alerts.isSelectionEmpty(country, countryError, "Select a country")) return;
        if (Alerts.isSelectionEmpty(state, stateError, "Select a state")) return;
        if (Alerts.isFieldEmpty(phone, phoneError, "Enter a phone number")) return;

        // Insert form data into database
        DBCustomer.insertCustomer(
                DBCustomer.getNewCustomerId(),
                name.getText(),
                address.getText(),
                postcode.getText(),
                phone.getText(),
                DBStates.getStateId(state.getValue()));

        // Load scene
        Parent sceneParent = FXMLLoader.load(getClass().getResource("customers.fxml"));
        Scene scene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

}
