package GUI;

import Database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <h1>Rendezvous</h1>
 * Rendezvous is an application designed to create, store and edit individual
 * Customer information. The user is then able to book appointments with
 * stored customers.
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-20
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Rendezvous");
        primaryStage.setScene(new Scene(root, 1120 , 800));
        primaryStage.show();
    }


    public static void main(String[] args) {

        // Establish connection to database
        DBConnection.startConnection();

        launch(args);

        // Close connection to database
        DBConnection.closeConnection();
    }
}
