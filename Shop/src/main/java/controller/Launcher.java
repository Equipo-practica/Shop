package controller;

import datos.ConexionBD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/LoginView.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("System Shop");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);

            primaryStage.setOnCloseRequest(event -> ConexionBD.cerrarConexion());

            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Fatal error starting application. Check /vista/LoginView.fxml exists.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}