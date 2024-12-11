package com.mycompany.emr;

<<<<<<< HEAD
import com.mycompany.emr.controllers.SceneController;

=======
>>>>>>> f43c1cb7dcbfb59f21bfb92452c9c1ab7922fd0a
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import utils.DatabaseConnection;
import java.sql.Connection;

/**
 * JavaFX App
 * @author Ghouse
 */
/**
 * 
 * @author eboka
 */
public class App extends Application {

    private static Connection connection;

    @Override
    public void start(Stage stage) {
        try {
<<<<<<< HEAD
            SceneController.setPrimaryStage(stage);
            SceneController.switchScene("DoctorForm"); //Change Scenes
=======
>>>>>>> f43c1cb7dcbfb59f21bfb92452c9c1ab7922fd0a
            stage.setTitle("EMR System");
            stage.show();
        } catch (Exception e) {
            System.err.println("Scene Controller error");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    };

    public static void main(String[] args) {
        launch();
        //Databse Connection
        try {
            connection = DatabaseConnection.getConnection();
        } catch (Exception e) {
            System.err.println("Database Connection Error");
            e.printStackTrace();
        }
    }

}
<<<<<<< HEAD

=======
>>>>>>> f43c1cb7dcbfb59f21bfb92452c9c1ab7922fd0a
