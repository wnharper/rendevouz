package GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {

    // Form fields
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button login;


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
        Parent sceneParent = FXMLLoader.load(getClass().getResource("customers.fxml"));
        Scene scene = new Scene(sceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


}
