package controller;

import datos.ConexionBD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carga la vista principal desde la carpeta de recursos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
            Parent root = loader.load();

            // Configura la escena principal
            Scene scene = new Scene(root);

            primaryStage.setTitle("Sistema de Shop v1.0");
            primaryStage.setScene(scene);

            // Al cerrar la ventana principal, cerramos la conexión a la BD
            primaryStage.setOnCloseRequest(event -> {
                ConexionBD.cerrarConexion();
                System.out.println("Aplicación finalizada correctamente.");
            });

            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error fatal: No se pudo cargar MainView.fxml.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}