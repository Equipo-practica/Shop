package controller;

import datos.ClientesDATOS;
import datos.EnvioDATOS;
import datos.PedidoDATOS;
import model.Cliente;
import model.Envio;
import model.Pedido;
import utils.Validador;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Stack;

public class GestionController {

    @FXML private Label lblMensaje;
    @FXML private TabPane tabPaneGestion;

    // Columnas Scene builder cliente
    @FXML private TextField txtIdCli, txtNombreCli, txtEmailCli;
    @FXML private DatePicker dpFechaCli;
    @FXML private CheckBox chkVipCli;

    // Columnas Scene builder pedido
    @FXML private TextField txtIdPed, txtImportePed;
    @FXML private DatePicker dpFechaPed;
    @FXML private CheckBox chkPagadoPed;
    @FXML private ComboBox<Cliente> cbClientePed;

    // Columnas Scene builder envio
    @FXML private TextField txtIdEnv, txtDireccionEnv;
    @FXML private DatePicker dpFechaEnv;
    @FXML private ComboBox<Pedido> cbPedidoEnv;
    @FXML private ComboBox<String> cbEstadoEnv;

    private ClientesDATOS clientesDAO = new ClientesDATOS();
    private PedidoDATOS pedidoDAO = new PedidoDATOS();
    private EnvioDATOS envioDAO = new EnvioDATOS();

    private Stack<Runnable> pilaDeshacer = new Stack<>();

    @FXML
    public void initialize() {
        dpFechaCli.setValue(LocalDate.now());
        dpFechaPed.setValue(LocalDate.now());
        dpFechaEnv.setValue(LocalDate.now());

        // Estado de Envios
        cbEstadoEnv.setItems(FXCollections.observableArrayList("Pending", "On the Way", "Delivered", "Cancelled"));
        cbEstadoEnv.getSelectionModel().selectFirst();

        configurarComboClientes();
        configurarComboPedidos();

        tabPaneGestion.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                String titulo = newTab.getText();
                // Titulos pestañas
                if (titulo.equals("Orders")) cargarComboClientes();
                if (titulo.equals("Shipments")) cargarComboPedidos();
                lblMensaje.setText("");
            }
        });

        cargarComboClientes();
        cargarComboPedidos();
    }

    @FXML
    private void handleDeshacer() {
        if (!pilaDeshacer.isEmpty()) {
            Runnable accion = pilaDeshacer.pop();
            accion.run();
            cargarComboClientes();
            cargarComboPedidos();
            lblMensaje.setText("Action undone successfully.");
        } else {
            lblMensaje.setText("No actions to undo.");
        }
    }

    // Seccion completa para clientes

    @FXML private void handleGuardarCli() {
        if (!validarCliente()) return;
        int idManual = 0;
        try { if (!txtIdCli.getText().isEmpty()) idManual = Integer.parseInt(txtIdCli.getText()); } catch(Exception e){}

        Cliente c = new Cliente(idManual, txtNombreCli.getText(), txtEmailCli.getText(), chkVipCli.isSelected(), dpFechaCli.getValue());

        if (clientesDAO.insertar(c)) {
            lblMensaje.setText("Client saved.");
            handleLimpiarCli();
            cargarComboClientes();

            final int idParaBorrar = (idManual > 0) ? idManual : 0;
            if(idParaBorrar > 0) {
                pilaDeshacer.push(() -> clientesDAO.eliminar(idParaBorrar));
            } else {
                pilaDeshacer.push(() -> lblMensaje.setText("Info: Cannot undo automatic creation without manual ID."));
            }

        } else {
            lblMensaje.setText("Error saving client.");
        }
    }

    @FXML private void handleModificarCli() {
        try {
            int id = Integer.parseInt(txtIdCli.getText());
            Cliente old = clientesDAO.buscarPorId(id);

            if (old != null) {
                if (!validarCliente()) return;
                Cliente nuevo = new Cliente(id, txtNombreCli.getText(), txtEmailCli.getText(), chkVipCli.isSelected(), dpFechaCli.getValue());

                if (clientesDAO.actualizar(nuevo)) {
                    lblMensaje.setText("Client updated.");
                    cargarComboClientes();
                    pilaDeshacer.push(() -> clientesDAO.actualizar(old));
                }
            }
        } catch (NumberFormatException e) { lblMensaje.setText("Invalid ID."); }
    }

    @FXML private void handleEliminarCli() {
        try {
            int id = Integer.parseInt(txtIdCli.getText());
            Cliente aBorrar = clientesDAO.buscarPorId(id);

            if (aBorrar != null) {
                List<Pedido> pedidosHijos = pedidoDAO.listarPorCliente(id);

                if (clientesDAO.eliminar(id)) {
                    lblMensaje.setText("Client deleted.");
                    handleLimpiarCli();
                    cargarComboClientes();

                    pilaDeshacer.push(() -> {
                        clientesDAO.insertar(aBorrar);
                        for (Pedido p : pedidosHijos) {
                            pedidoDAO.insertar(p);
                        }
                        lblMensaje.setText("Undo: Client and orders restored.");
                        cargarComboClientes();
                    });
                } else { lblMensaje.setText("Could not delete."); }
            }
        } catch (NumberFormatException e) { lblMensaje.setText("Invalid ID."); }
    }

    @FXML private void handleBuscarCli() {
        try {
            int id = Integer.parseInt(txtIdCli.getText());
            Cliente c = clientesDAO.buscarPorId(id);
            if (c != null) {
                txtNombreCli.setText(c.getNombre());
                txtEmailCli.setText(c.getEmail());
                chkVipCli.setText(c.isVip());
                dpFechaCli.setValue(c.getFechaAlta());
                lblMensaje.setText("Client found.");
            } else { lblMensaje.setText("Client not found."); }
        } catch (NumberFormatException e) { lblMensaje.setText("Invalid ID."); }
    }

    @FXML private void handleLimpiarCli() {
        txtIdCli.clear(); txtNombreCli.clear(); txtEmailCli.clear();
        chkVipCli.setSelected(false); dpFechaCli.setValue(LocalDate.now());
    }

    // Seccion completa para Pedidos

    @FXML private void handleGuardarPed() {
        if (cbClientePed.getValue() == null) { lblMensaje.setText("Select a client."); return; }
        try {
            int idManual = 0;
            try { if(!txtIdPed.getText().isEmpty()) idManual = Integer.parseInt(txtIdPed.getText()); } catch(Exception e){}

            float importe = Float.parseFloat(txtImportePed.getText());
            Pedido p = new Pedido(idManual, dpFechaPed.getValue(), importe, chkPagadoPed.isSelected(), cbClientePed.getValue().getIdCliente());

            if (pedidoDAO.insertar(p)) {
                lblMensaje.setText("Order saved.");
                handleLimpiarPed();
                cargarComboPedidos();

                final int idParaBorrar = idManual;
                if(idParaBorrar > 0) pilaDeshacer.push(() -> pedidoDAO.eliminar(idParaBorrar));
            } else { lblMensaje.setText("Error saving order."); }
        } catch (NumberFormatException e) { lblMensaje.setText("Invalid amount."); }
    }

    @FXML private void handleModificarPed() {
        try {
            int id = Integer.parseInt(txtIdPed.getText());
            Pedido old = pedidoDAO.buscarPorId(id);
            if (old != null && cbClientePed.getValue() != null) {
                float importe = Float.parseFloat(txtImportePed.getText());
                Pedido nuevo = new Pedido(id, dpFechaPed.getValue(), importe, chkPagadoPed.isSelected(), cbClientePed.getValue().getIdCliente());

                if (pedidoDAO.actualizar(nuevo)) {
                    lblMensaje.setText("Order updated.");
                    cargarComboPedidos();
                    pilaDeshacer.push(() -> pedidoDAO.actualizar(old));
                }
            }
        } catch (NumberFormatException e) { lblMensaje.setText("Invalid numeric data."); }
    }

    @FXML private void handleEliminarPed() {
        try {
            int id = Integer.parseInt(txtIdPed.getText());
            Pedido aBorrar = pedidoDAO.buscarPorId(id);

            if (aBorrar != null) {
                List<Envio> enviosHijos = envioDAO.listarPorPedido(id);

                if (pedidoDAO.eliminar(id)) {
                    lblMensaje.setText("Order deleted.");
                    handleLimpiarPed();
                    cargarComboPedidos();

                    pilaDeshacer.push(() -> {
                        pedidoDAO.insertar(aBorrar);
                        for (Envio e : enviosHijos) {
                            envioDAO.insertar(e);
                        }
                        lblMensaje.setText("Undo: Order and shipments restored.");
                        cargarComboPedidos();
                    });
                } else { lblMensaje.setText("Error deleting."); }
            }
        } catch (NumberFormatException e) { lblMensaje.setText("Invalid ID."); }
    }

    @FXML private void handleBuscarPed() {
        try {
            int id = Integer.parseInt(txtIdPed.getText());
            Pedido p = pedidoDAO.buscarPorId(id);
            if (p != null) {
                dpFechaPed.setValue(p.getFecha());
                txtImportePed.setText(String.valueOf(p.getImporte()));
                chkPagadoPed.setText(p.isPagado());
                seleccionarClienteEnCombo(p.getIdCliente());
                lblMensaje.setText("Order found.");
            } else { lblMensaje.setText("Order not found."); }
        } catch (NumberFormatException e) { lblMensaje.setText("Invalid ID."); }
    }

    @FXML private void handleLimpiarPed() {
        txtIdPed.clear(); txtImportePed.clear();
        chkPagadoPed.setSelected(false); dpFechaPed.setValue(LocalDate.now());
        cbClientePed.getSelectionModel().clearSelection();
    }

    // Seccion completa para Envios

    @FXML private void handleGuardarEnv() {
        if (cbPedidoEnv.getValue() == null) { lblMensaje.setText("Select an order."); return; }
        int idManual = 0;
        try { if(!txtIdEnv.getText().isEmpty()) idManual = Integer.parseInt(txtIdEnv.getText()); } catch(Exception e){}

        Envio e = new Envio(idManual, txtDireccionEnv.getText(), cbPedidoEnv.getValue().getIdPedido(), dpFechaEnv.getValue(), cbEstadoEnv.getValue());

        if (envioDAO.insertar(e)) {
            lblMensaje.setText("Shipment registered.");
            handleLimpiarEnv();
            final int idParaBorrar = idManual;
            if(idParaBorrar > 0) pilaDeshacer.push(() -> envioDAO.eliminar(idParaBorrar));
        } else { lblMensaje.setText("Error saving shipment."); }
    }

    @FXML private void handleModificarEnv() {
        try {
            int id = Integer.parseInt(txtIdEnv.getText());
            Envio old = envioDAO.buscarPorId(id);
            if (old != null && cbPedidoEnv.getValue() != null) {
                Envio nuevo = new Envio(id, txtDireccionEnv.getText(), cbPedidoEnv.getValue().getIdPedido(), dpFechaEnv.getValue(), cbEstadoEnv.getValue());
                if (envioDAO.actualizar(nuevo)) {
                    lblMensaje.setText("Shipment updated.");
                    pilaDeshacer.push(() -> envioDAO.actualizar(old));
                }
            }
        } catch (NumberFormatException ex) { lblMensaje.setText("Invalid ID."); }
    }

    @FXML private void handleEliminarEnv() {
        try {
            int id = Integer.parseInt(txtIdEnv.getText());
            Envio aBorrar = envioDAO.buscarPorId(id);

            if (aBorrar != null && envioDAO.eliminar(id)) {
                lblMensaje.setText("Shipment deleted.");
                handleLimpiarEnv();

                pilaDeshacer.push(() -> {
                    envioDAO.insertar(aBorrar);
                    lblMensaje.setText("Undo: Shipment restored.");
                });
            }
        } catch (NumberFormatException e) { lblMensaje.setText("Invalid ID."); }
    }

    @FXML private void handleBuscarEnv() {
        try {
            int id = Integer.parseInt(txtIdEnv.getText());
            Envio e = envioDAO.buscarPorId(id);
            if (e != null) {
                txtDireccionEnv.setText(e.getDireccion());
                dpFechaEnv.setValue(e.getFechaEntrega());
                cbEstadoEnv.setValue(e.getEstado());
                seleccionarPedidoEnCombo(e.getIdPedido());
                lblMensaje.setText("Shipment found.");
            } else { lblMensaje.setText("Shipment not found."); }
        } catch (NumberFormatException e) { lblMensaje.setText("Invalid ID."); }
    }

    @FXML private void handleLimpiarEnv() {
        txtIdEnv.clear(); txtDireccionEnv.clear();
        dpFechaEnv.setValue(LocalDate.now());
        cbPedidoEnv.getSelectionModel().clearSelection();
        cbEstadoEnv.getSelectionModel().selectFirst();
    }

    private boolean validarCliente() {
        if(txtNombreCli.getText().isEmpty())  {
            lblMensaje.setText("Error: Name cannot be empty");
            return false;
        } else if (!Validador.esEmailValido(txtEmailCli.getText())) {
            lblMensaje.setText("Error: Enter a valid email");
            return false;
        }
        return !txtNombreCli.getText().isEmpty() && Validador.esEmailValido(txtEmailCli.getText());
    }

    private void cargarComboClientes() {
        cbClientePed.setItems(FXCollections.observableArrayList(clientesDAO.listarTodos()));
    }

    private void cargarComboPedidos() {
        cbPedidoEnv.setItems(FXCollections.observableArrayList(pedidoDAO.listarTodos()));
    }

    private void configurarComboClientes() {
        cbClientePed.setConverter(new StringConverter<Cliente>() {
            @Override public String toString(Cliente c) { return (c == null) ? null : "[" + c.getIdCliente() + "] " + c.getNombre(); }
            @Override public Cliente fromString(String s) { return null; }
        });
    }

    private void configurarComboPedidos() {
        cbPedidoEnv.setConverter(new StringConverter<Pedido>() {
            @Override public String toString(Pedido p) { return (p == null) ? null : "ID: " + p.getIdPedido() + " - " + p.getImporte() + "€"; }
            @Override public Pedido fromString(String s) { return null; }
        });
    }

    private void seleccionarClienteEnCombo(int idCliente) {
        for (Cliente c : cbClientePed.getItems()) {
            if (c.getIdCliente() == idCliente) { cbClientePed.setValue(c); break; }
        }
    }

    private void seleccionarPedidoEnCombo(int idPedido) {
        for (Pedido p : cbPedidoEnv.getItems()) {
            if (p.getIdPedido() == idPedido) { cbPedidoEnv.setValue(p); break; }
        }
    }
}