package controller;

import datos.ClientesDATOS;
import datos.EnvioDATOS;
import datos.PedidoDATOS;
import model.Cliente;
import model.Envio;
import model.Pedido;
import utils.ExportadorJSON;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;

public class TableController {

    // Tabla CLientes
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, Integer> colIdCli;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, Boolean> colVip;
    @FXML private TableColumn<Cliente, LocalDate> colFechaAlta;

    // Tabla Pedidos
    @FXML private TableView<Pedido> tablaPedidos;
    @FXML private TableColumn<Pedido, Integer> colIdPed;
    @FXML private TableColumn<Pedido, LocalDate> colFechaPed;
    @FXML private TableColumn<Pedido, Float> colImporte;
    @FXML private TableColumn<Pedido, Boolean> colPagado;
    @FXML private TableColumn<Pedido, Integer> colIdClienteFK;

    // Tabla Envios
    @FXML private TableView<Envio> tablaEnvios;
    @FXML private TableColumn<Envio, Integer> colIdEnvio;
    @FXML private TableColumn<Envio, String> colDireccion;
    @FXML private TableColumn<Envio, Integer> colIdPedFK;
    @FXML private TableColumn<Envio, LocalDate> colFechaEntrega;
    @FXML private TableColumn<Envio, String> colEstado;

    private ClientesDATOS clientesDAO = new ClientesDATOS();
    private PedidoDATOS pedidoDAO = new PedidoDATOS();
    private EnvioDATOS envioDAO = new EnvioDATOS();

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarDatos();
    }

    private void configurarColumnas() {
        // Elementos Clientes
        colIdCli.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colVip.setCellValueFactory(new PropertyValueFactory<>("vip"));
        colFechaAlta.setCellValueFactory(new PropertyValueFactory<>("fechaAlta"));

        // Elementos Pedidos
        colIdPed.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colFechaPed.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colImporte.setCellValueFactory(new PropertyValueFactory<>("importe"));
        colPagado.setCellValueFactory(new PropertyValueFactory<>("pagado"));
        colIdClienteFK.setCellValueFactory(new PropertyValueFactory<>("idCliente"));

        // Elementos Envios
        colIdEnvio.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colIdPedFK.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void cargarDatos() {
        tablaClientes.setItems(FXCollections.observableArrayList(clientesDAO.listarTodos()));
        tablaPedidos.setItems(FXCollections.observableArrayList(pedidoDAO.listarTodos()));
        tablaEnvios.setItems(FXCollections.observableArrayList(envioDAO.listarTodos()));
    }

    // Exportar JSON
    @FXML
    private void handleExportarJson(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select folder to save JSONs");

        Stage stage = (Stage) tablaClientes.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            String rutaClientes = new File(selectedDirectory, "clientes.json").getAbsolutePath();
            String rutaPedidos = new File(selectedDirectory, "pedidos.json").getAbsolutePath();
            String rutaEnvios = new File(selectedDirectory, "envios.json").getAbsolutePath();

            boolean exitoCli = ExportadorJSON.exportarClientes(tablaClientes.getItems(), rutaClientes);
            boolean exitoPed = ExportadorJSON.exportarPedidos(tablaPedidos.getItems(), rutaPedidos);
            boolean exitoEnv = ExportadorJSON.exportarEnvios(tablaEnvios.getItems(), rutaEnvios);

            if (exitoCli && exitoPed && exitoEnv) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Export Successful",
                        "Files generated in:\n" + selectedDirectory.getAbsolutePath());
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "There was a problem saving files.");
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}