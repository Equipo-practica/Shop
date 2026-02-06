package controller;

import datos.ConexionBD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    public static ConexionBD ConexionBD;

    @Override
    public void start(Stage primaryStage) {
        try {
            ConexionBD.getConexion();
            // Carga la vista principal desde la carpeta de recursos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
            Parent root = loader.load();

            // Configura la escena principal
            Scene scene = new Scene(root);

            primaryStage.setTitle("Shop System v1.0");
            primaryStage.setScene(scene);

            // Al cerrar la ventana principal, cerramos la conexión a la BD
            primaryStage.setOnCloseRequest(event -> {
                ConexionBD.cerrarConexion();
                System.out.println("Aplication disconnected.");
            });

            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Critical error: MainView.fxml failed to load.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}