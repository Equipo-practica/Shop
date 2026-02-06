package controller;


import datos.ClientesDATOS;
import datos.PedidoDATOS;
import model.Cliente;
import model.Pedido;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class TableController {

    // --- TABLA CLIENTES ---
    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<Cliente, Integer> colIdCli;
    @FXML
    private TableColumn<Cliente, String> colNombre;
    @FXML
    private TableColumn<Cliente, String> colEmail;
    @FXML
    private TableColumn<Cliente, Boolean> colVip;
    @FXML
    private TableColumn<Cliente, LocalDate> colFechaAlta;

    // --- TABLA PEDIDOS ---
    @FXML
    private TableView<Pedido> tablaPedidos;
    @FXML
    private TableColumn<Pedido, Integer> colIdPed;
    @FXML
    private TableColumn<Pedido, LocalDate> colFechaPed;
    @FXML
    private TableColumn<Pedido, Float> colImporte;
    @FXML
    private TableColumn<Pedido, Boolean> colPagado;
    @FXML
    private TableColumn<Pedido, Integer> colIdClienteFK;

    // DAOs
    private ClientesDATOS ClientesDATOS = new ClientesDATOS();

}

@FXML
public void initialize() {
    configurarTablaClientes();
    configurarTablaPedidos();
    cargarDatos();
}

private void configurarTablaClientes() {
    // PropertyValueFactory busca el getter en el modelo (ej: "nombre" -> getNombre())
    setCellValueFactory(new PropertyValueFactory<>("idCliente"));
    colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
    colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    colVip.setCellValueFactory(new PropertyValueFactory<>("vip"));
    colFechaAlta.setCellValueFactory(new PropertyValueFactory<>("fechaAlta"));
}

private void configurarTablaPedidos() {
    colIdPed.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
    colFechaPed.setCellValueFactory(new PropertyValueFactory<>("fecha"));
    colImporte.setCellValueFactory(new PropertyValueFactory<>("importe"));
    colPagado.setCellValueFactory(new PropertyValueFactory<>("pagado"));
    colIdClienteFK.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
}

private void cargarDatos() {
    // Cargar Clientes
    ObservableList<Cliente> listaClientes = FXCollections.observableArrayList(ClientesDATOS.listarTodos());
    tablaClientes.setItems(listaClientes);

    // Cargar Pedidos (Esto requiere implementar listarTodos en PedidoDATOS)
    // ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList(pedidoDAO.listarTodos());
    // tablaPedidos.setItems(listaPedidos);
}

// Metodo para refrescar datos manualmente (puedes vincularlo a un botón)
@FXML
private void handleRefrescar() {
    cargarDatos();
}