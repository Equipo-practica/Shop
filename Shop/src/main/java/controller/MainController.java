package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Button btnGestionClientes;

    @FXML
    private Button btnVerTablas;

    // Maneja el clic en el botón de Gestión de Clientes. Carga el formulario CRUD.
    @FXML
    private void handleGestionClientes(ActionEvent event) {
        cargarVista("/view/ClienteForm.fxml", "Customers Management - Coffee Shop");
    }

    // Maneja el clic en el botón de Ver Tablas. Carga la vista con los TableView de ambas entidades.

    @FXML
    private void handleVerTablas(ActionEvent event) {
        cargarVista("/view/TableView.fxml", "General information");
    }

    /**
     * Método genérico para cargar archivos FXML y mostrarlos en una nueva ventana.
     * * @param fxmlPath Ruta relativa al archivo FXML en resources.
     * @param titulo Título que tendrá la nueva ventana.
     */
    private void cargarVista(String fxmlPath, String titulo) {
        try {
            // 1. Instanciar el cargador de FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // 2. Cargar el diseño
            Parent root = loader.load();

            // 3. Crear el escenario (Stage)
            Stage stage = new Stage();
            stage.setTitle(titulo);

            // Hacer que la ventana sea modal (opcional: impide tocar la principal hasta cerrar esta)
            stage.initModality(Modality.WINDOW_MODAL);
            // Definimos la ventana principal como "dueña" si es posible
            stage.initOwner(btnGestionClientes.getScene().getWindow());

            // 4. Configurar la escena y mostrar
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false); // Recomendado para formularios fijos
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error", "Could not load: " + fxmlPath);
        } catch (NullPointerException e) {
            System.err.println("Error: File FXML not found in this path: " + fxmlPath);
            mostrarError("Setup Error", "File FXML not found.");
        }
    }

    // Muestra una alerta de error al usuario.
    private void mostrarError(String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An error occured with the app ");
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}