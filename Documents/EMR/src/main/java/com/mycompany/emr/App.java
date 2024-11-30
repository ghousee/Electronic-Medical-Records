package com.mycompany.emr;

import com.mycompany.emr.controllers.SceneController;

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
 */
public class App extends Application {

    private static Connection connection;

    @Override
    public void start(Stage stage) {
        try {
            SceneController.setPrimaryStage(stage);
            SceneController.switchScene("homepage");
            stage.setTitle("EMR System");
            stage.show();
        } catch (Exception e) {
            System.err.println("Scene Controller error");
            e.printStackTrace();
        }
    }

//    public static void switchScene(String fxmlFile) {
//        try {
//            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlFile + ".fxml"));
//            Parent root = loader.load();
//            Scene scene = new Scene(root);
//            primaryStage.setScene(scene);
//        } catch (IOException e) {
//            System.err.println("Failed to load FXML: " + fxmlFile);
//            e.printStackTrace();
//        }
//    }
//
//    static void setRoot(String fxml) throws IOException {
//        scene.setRoot(loadFXML(fxml));
//    }
//
//    @Override
//    public void stop() throws Exception {
//        // Close the database connection when the application stops
//        if (connection != null && !connection.isClosed()) {
//            connection.close();
//            System.out.println("Database connection closed.");
//        }
//    }

    // Getter for the connection to be used by DAOs and controllers
    public static Connection getConnection() {
        return connection;
    }

//    private static Parent loadFXML(String fxml) throws IOException {
////        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
////        return fxmlLoader.load();
//        String path = "" + fxml + ".fxml";
//        URL resource = App.class.getResource(path);
//        if (resource == null) {
//            throw new IOException("FXML file not found at: " + path);
//        }
//        System.out.println("FXML file loaded from: " + resource.toString());
//        FXMLLoader fxmlLoader = new FXMLLoader(resource);
//        return fxmlLoader.load();
//    }

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

