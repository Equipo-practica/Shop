package controller;

import datos.ClientesDATOS;
import model.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class ClienteController {

    // Campos del formulario (FXML)
    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCliente; // Apellido o Razón Social
    @FXML private TextField txtEmail;
    @FXML private CheckBox chkVip;
    @FXML private DatePicker dpFechaAlta;
    @FXML private Label lblMensaje; // Para mostrar errores sin popups intrusivos

    //Para conectar con BBDD
    private ClientesDATOS clientesDATOS = new ClientesDATOS();

    // Validación de email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @FXML
    public void initialize() {
        // Inicializar fecha al día de hoy
        dpFechaAlta.setValue(LocalDate.now());

        // --- VALIDACIÓN EN LÍNEA  ---
        // Escucha cambios en el campo email mientras el usuario escribe
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!Pattern.matches(EMAIL_REGEX, newValue)) {
                txtEmail.setStyle("-fx-border-color: red; -fx-text-fill: red;");
            } else {
                txtEmail.setStyle("-fx-border-color: green; -fx-text-fill: black;");
            }
        });
    }

    // --- CRUD: Crear ---
    @FXML
    private void handleGuardar() {
        if (!esFormularioValido()) return;

        Cliente nuevoCliente = new Cliente(
                txtNombre.getText(),
                txtCliente.getText(),
                txtEmail.getText(),
                chkVip.isSelected(),
                dpFechaAlta.getValue()
        );

        boolean exito;
        exito = ClientesDATOS.insertar(nuevoCliente);

        if (exito) {
            mostrarAlerta("ok", "Customer registered successfully.");
            limpiarFormulario();
        } else {
            mostrarAlerta("Error", "Customer registration failed.");
        }
    }

    // --- CRUD: Buscar (Buscar por ID para editar) ---
    @FXML
    private void handleBuscar() {
        try {
            int id = Integer.parseInt(txtId.getText());
            Cliente c = ClientesDATOS.buscarPorId(id);

            if (c != null) {
                txtNombre.setText(c.getNombre());
                txtEmail.setText(c.getEmail());
                chkVip.setSelected(c.isVip());
                dpFechaAlta.setValue(c.getFechaAlta());
                lblMensaje.setText("Client found.");
            } else {
                mostrarAlerta("Info", "Client not found.");
                limpiarFormulario();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "ID must be a number.");
        }
    }

    // --- CRUD: Actualizar ---
    @FXML
    private void handleModificar() {
        if (txtId.getText().isEmpty()) {
            mostrarAlerta("Error", "Search a customer by inserting the ID and then modify.");
            return;
        }
        if (!esFormularioValido()) return;

        int id = Integer.parseInt(txtId.getText());
        Cliente clienteModificado = new Cliente(
                id,
                txtNombre.getText(),
                txtEmail.getText(),
                chkVip.isSelected(),
                dpFechaAlta.getValue()
        );

        boolean exito = ClientesDATOS.actualizar(clienteModificado);

        if (exito) {
            mostrarAlerta("ok", "Customer modified.");
        } else {
            mostrarAlerta("Error", "Unable to modify.");
        }
    }

    // --- CRUD: Eliminar ---
    @FXML
    private void handleEliminar() {
        if (txtId.getText().isEmpty()) return;

        int id = Integer.parseInt(txtId.getText());
        boolean exito = ClientesDATOS.eliminar(id);

        if (exito) {
            mostrarAlerta("ok", "Customer deleted.");
            limpiarFormulario();
        } else {
            mostrarAlerta("Error", "Unable to delete.");
        }
    }

    @FXML
    private void handleLimpiar() {
        limpiarFormulario();
    }

    // --- FUNCIONES EXTRA ---
    // Limpiar el formulario
    private void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
        txtCliente.clear();
        txtEmail.clear();
        chkVip.setSelected(false);
        dpFechaAlta.setValue(LocalDate.now());
        txtEmail.setStyle(null); // Resetear estilos de validación
    }

    private boolean esFormularioValido() {
        // Validación de campos obligatorios
        if (txtNombre.getText().isEmpty() || txtCliente.getText().isEmpty()) {
            mostrarAlerta("Validación", "Name and Client needed.");
            return false;
        }
        // Validación de formato Email
        if (!Pattern.matches(EMAIL_REGEX, txtEmail.getText())) {
            mostrarAlerta("Validación", "Invalid email format.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}