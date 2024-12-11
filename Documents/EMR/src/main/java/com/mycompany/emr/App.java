package com.mycompany.emr;

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
/**
 * 
 * @author eboka
 */
public class App extends Application {

    private static Connection connection;

    @Override
    public void start(Stage stage) {
        try {
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
