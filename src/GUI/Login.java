package GUI;

import DBAccess.DBLogin;
import Model.User;
import Utilities.Alerts;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class Login implements Initializable {

    // Form fields
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button login;
    @FXML private Label loginError;

    // Current user
    public static User currentUser;


    // Initialize method, required in order for the UI/Scene to launch and function
    @Override
    public void initialize (URL url, ResourceBundle rb) {



        // Set focus to name box
        Platform.runLater(()->username.requestFocus());

    }

    /**
     * This method checks the users credentials against the database and if successful
     * takes the user to the customer dashboard
     */
    public void login(ActionEvent event) throws IOException
    {
        if (Alerts.isFieldEmpty(username, loginError, "Enter a username")) return;
        if (Alerts.isFieldEmpty(password, loginError, "Enter a password")) return;

        // Log current time with location/time zone info
        ZonedDateTime timeStamp = ZonedDateTime.now();

        // Create log file if it doesn't exist
        File logFile = new File("login_activity.txt");
        if (!logFile.exists()) logFile.createNewFile();

        if (DBLogin.verifyUserPass(username.getText(), password.getText())) {

            // Write login success to log file
            FileWriter myWriter = new FileWriter("login_activity.txt", true);
            myWriter.write(timeStamp + " " +  username.getText() + " logged in successfully\n");
            myWriter.close();

            // Get user details
            currentUser = (DBLogin.getUser(username.getText(), password.getText()));

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("customers.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            // Write login attempt to log file
            FileWriter myWriter = new FileWriter("login_activity.txt", true);
            myWriter.write(timeStamp + " " + username.getText() + " logged in unsuccessfully\n");
            myWriter.close();
            // Display error
            loginError.setText("Invalid username or password");

            return;
        }


    }



}
