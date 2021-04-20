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
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <h2>Login</h2>
 * The login class defines the functions for the login screen.
 * It enables a user to enter their credentials and have them validated
 * against the database in order to access the application.
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-20
 */

public class Login implements Initializable {

    // Form fields
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button login;
    @FXML private Label loginError;

    // Current user
    @FXML private Label timeZone;
    public static User currentUser;

    // error messages
    private String usernameError = "Enter a username";
    private String passwordError = "Enter a password";
    private String invalidUserPass = "Invalid username or password";



    // Initialize method, required in order for the UI/Scene to launch and function
    @Override
    public void initialize (URL url, ResourceBundle rb) {

        // Display user's time zone
        ZonedDateTime userLocation = ZonedDateTime.now();
        timeZone.setText(userLocation.getZone().toString());

        // If user's machine's language is set to French, change login form to copy to French
        if (Locale.getDefault() == Locale.FRENCH || Locale.getDefault() == Locale.CANADA_FRENCH || Locale.getDefault() == Locale.FRANCE) {
            username.setPromptText("Nom d'utilisateur");
            password.setPromptText("Le mot de passe");
            login.setText("CONNEXION");
            usernameError = "Entrez un nom d'utilisateur";
            passwordError = "Entrer un mot de passe";
            invalidUserPass = "Nom d'utilisateur ou mot de passe invalide";

        }

        // Set focus to username box
        Platform.runLater(()->username.requestFocus());

    }

    /**
     * This method checks the users credentials against the database and if successful
     * takes the user to the customer dashboard
     */
    public void login(ActionEvent event) throws IOException
    {
        if (Alerts.isFieldEmpty(username, loginError, usernameError)) return;
        if (Alerts.isFieldEmpty(password, loginError, passwordError)) return;

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
            loader.setLocation(getClass().getResource("appointments.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            // Load and access the controller
            AppointmentsController controller = loader.getController();

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            // Write login attempt to log file
            FileWriter myWriter = new FileWriter("login_activity.txt", true);
            myWriter.write(timeStamp + " " + username.getText() + " logged in unsuccessfully\n");
            myWriter.close();
            // Display error
            loginError.setText(invalidUserPass);

            return;
        }


    }



}
