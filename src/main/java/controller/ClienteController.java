package controller;

import datos.ClientesDATOS;
import model.Cliente;
import utils.Validador;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Stack;

public class ClienteController {

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private CheckBox chkVip;
    @FXML private DatePicker dpFechaAlta;
    @FXML private Label lblMensaje;

    // Instancia de acceso a datos
    private ClientesDATOS clientesDatos = new ClientesDATOS();

    // Pila para manejar la funcionalidad "Deshacer"
    private Stack<Runnable> pilaDeshacer = new Stack<>();

    @FXML
    public void initialize() {
        dpFechaAlta.setValue(LocalDate.now());

        // Validación visual en tiempo real del Email
        txtEmail.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty() && !Validador.esEmailValido(newVal)) {
                txtEmail.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            } else {
                txtEmail.setStyle(null); // Restaurar estilo por defecto
            }
        });
    }

    // --- FUNCIONALIDAD 1: BUSCAR EN NUEVA VENTANA ---
    @FXML
    private void handleBuscar() {
        try {
            if (txtId.getText().isEmpty()) {
                lblMensaje.setText("Introduce un ID para buscar.");
                return;
            }

            int id = Integer.parseInt(txtId.getText());
            Cliente c = clientesDatos.buscarPorId(id);

            if (c != null) {
                mostrarVentanaDetalle(c); // Requisito: Abrir en nueva ventana
                lblMensaje.setText("Cliente encontrado. Ver ventana emergente.");
            } else {
                lblMensaje.setText("No se encontró ningún cliente con ID: " + id);
            }
        } catch (NumberFormatException e) {
            lblMensaje.setText("El ID debe ser un número entero.");
        }
    }

    private void mostrarVentanaDetalle(Cliente c) {
        Stage stage = new Stage();
        stage.setTitle("Detalle del Cliente ID: " + c.getIdCliente());

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f4f4;");

        root.getChildren().addAll(
                new Label("ID: " + c.getIdCliente()),
                new Label("Nombre: " + c.getNombre()),
                new Label("Email: " + c.getEmail()),
                new Label("VIP: " + (c.isVip() ? "Sí" : "No")),
                new Label("Fecha Alta: " + c.getFechaAlta().toString())
        );

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> stage.close());
        root.getChildren().add(btnCerrar);

        Scene scene = new Scene(root, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    // --- FUNCIONALIDAD 2: GUARDAR (Con validación de existencia) ---
    @FXML
    private void handleGuardar() {
        if (!validarCampos()) return;

        // Comprobar si intentan guardar con un ID manual que ya existe
        if (!txtId.getText().isEmpty()) {
            try {
                int idCheck = Integer.parseInt(txtId.getText());
                if (clientesDatos.buscarPorId(idCheck) != null) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Duplicado", "El cliente con este ID ya existe.");
                    return;
                }
            } catch (NumberFormatException ignored) {}
        }

        // Crear objeto (ID 0 si es autoincrement, o el del texto si es manual)
        int idParaGuardar = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());

        Cliente nuevo = new Cliente(
                idParaGuardar,
                txtNombre.getText(),
                txtEmail.getText(),
                chkVip.isSelected(),
                dpFechaAlta.getValue()
        );

        if (clientesDatos.insertar(nuevo)) {
            lblMensaje.setText("Cliente creado correctamente.");

            // AGREGAR AL DESHACER: La acción inversa es eliminar este cliente
            // Necesitamos saber qué ID se generó si fue autoincrement.
            // Asumiremos para el ejemplo que el ID se puede recuperar o fue el insertado.
            // Si es AutoIncrement puro, necesitaríamos recuperar el último ID insertado.
            // Para simplificar el ejemplo de deshacer, asumimos que podemos borrar por el objeto.
            int idGenerado = nuevo.getIdCliente(); // Asumiendo que el objeto 'nuevo' se actualiza tras insertar o usamos el manual

            pilaDeshacer.push(() -> {
                clientesDatos.eliminar(idGenerado);
                lblMensaje.setText("Deshacer: Cliente eliminado.");
                handleLimpiar();
            });

            limpiarCampos();
        } else {
            lblMensaje.setText("Error al guardar en la base de datos.");
        }
    }

    // --- FUNCIONALIDAD 3: MODIFICAR (Preguntar si crear si no existe) ---
    @FXML
    private void handleModificar() {
        if (txtId.getText().isEmpty()) {
            lblMensaje.setText("Introduce el ID para modificar.");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());
            Cliente clienteExistente = clientesDatos.buscarPorId(id);

            if (clienteExistente == null) {
                // Requisito: Si no existe, pedir crear nuevo
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Cliente no existe");
                alert.setHeaderText("El cliente con ID " + id + " no existe.");
                alert.setContentText("¿Deseas crearlo como nuevo?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    handleGuardar();
                }
            } else {
                // Modificar existente
                if (!validarCampos()) return;

                // Guardar estado anterior para DESHACER
                Cliente estadoAnterior = new Cliente(
                        clienteExistente.getIdCliente(),
                        clienteExistente.getNombre(),
                        clienteExistente.getEmail(),
                        clienteExistente.isVip(),
                        clienteExistente.getFechaAlta()
                );

                Cliente editado = new Cliente(
                        id,
                        txtNombre.getText(),
                        txtEmail.getText(),
                        chkVip.isSelected(),
                        dpFechaAlta.getValue()
                );

                if (clientesDatos.actualizar(editado)) {
                    lblMensaje.setText("Cliente actualizado correctamente.");

                    // AGREGAR AL DESHACER: Restaurar el estado anterior
                    pilaDeshacer.push(() -> {
                        clientesDatos.actualizar(estadoAnterior);
                        lblMensaje.setText("Deshacer: Modificación revertida.");
                    });
                }
            }
        } catch (NumberFormatException e) {
            lblMensaje.setText("ID inválido.");
        }
    }

    // --- FUNCIONALIDAD 4: ELIMINAR ---
    @FXML
    private void handleEliminar() {
        if (txtId.getText().isEmpty()) {
            lblMensaje.setText("Indica el ID a eliminar.");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());
            Cliente aEliminar = clientesDatos.buscarPorId(id); // Buscar antes de borrar para poder restaurar

            if (aEliminar != null) {
                if (clientesDatos.eliminar(id)) {
                    lblMensaje.setText("Registro eliminado.");

                    // AGREGAR AL DESHACER: Volver a insertar el cliente borrado
                    pilaDeshacer.push(() -> {
                        clientesDatos.insertar(aEliminar);
                        // Nota: Si la BD es autoincrement estricto, esto podría generar un nuevo ID
                        // en lugar de restaurar el viejo, dependiendo de la implementación SQL.
                        lblMensaje.setText("Deshacer: Cliente restaurado.");
                    });

                    limpiarCampos();
                } else {
                    lblMensaje.setText("No se pudo eliminar.");
                }
            } else {
                lblMensaje.setText("El cliente no existe en la BD.");
                // Si es solo una entrada de la interfaz no guardada:
                limpiarCampos();
            }
        } catch (NumberFormatException e) {
            lblMensaje.setText("ID inválido.");
        }
    }

    // --- FUNCIONALIDAD 5: LIMPIAR ---
    @FXML
    private void handleLimpiar() {
        limpiarCampos();
        lblMensaje.setText("Formulario limpio.");
    }

    // --- FUNCIONALIDAD 6: DESHACER ---
    @FXML
    private void handleDeshacer() {
        if (!pilaDeshacer.isEmpty()) {
            Runnable accionDeshacer = pilaDeshacer.pop();
            accionDeshacer.run();
        } else {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Deshacer", "No hay acciones para deshacer.");
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty()) {
            lblMensaje.setText("El nombre es obligatorio.");
            txtNombre.requestFocus();
            return false;
        }
        if (txtEmail.getText().isEmpty() || !Validador.esEmailValido(txtEmail.getText())) {
            lblMensaje.setText("Email inválido o vacío.");
            txtEmail.requestFocus();
            return false;
        }
        if (dpFechaAlta.getValue() == null) {
            lblMensaje.setText("Selecciona una fecha de alta.");
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtEmail.clear();
        chkVip.setSelected(false);
        dpFechaAlta.setValue(LocalDate.now());
        txtEmail.setStyle(null);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}