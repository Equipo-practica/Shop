package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    @FXML private Label lblError;

    @FXML
    private void handleLogin(ActionEvent event) {
        String usuario = txtUser.getText();
        String password = txtPass.getText();

        if ("equipo".equals(usuario) && "equipo".equals(password)) {
            cargarMenuPrincipal(event);
        } else {
            lblError.setText("Incorrect username or password");
            lblError.setVisible(true);
        }
    }

    private void cargarMenuPrincipal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Main Menu");
            stage.setScene(new Scene(root));
            stage.show();

            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            lblError.setText("Critical Error: MainView.fxml not found");
            lblError.setVisible(true);
        }
    }
}