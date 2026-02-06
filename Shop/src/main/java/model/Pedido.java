package model;

import java.time.LocalDate;

public class Pedido {

    private int idPedido;       // El id_pedido
    private LocalDate fecha;    // Fecha Actual
    private float importe;      // Importe del pedido (float)
    private boolean pagado;     // SI o NO esta pagado  (boolean)
    private int idCliente;      // Clave externa del id_cliente

    public Pedido() {
    }

    // Constructor SIN ID ( Para insertar los datos)
    public Pedido(LocalDate fecha, float importe, boolean pagado, int idCliente) {
        this.fecha = fecha;
        this.importe = importe;
        this.pagado = pagado;
        this.idCliente = idCliente;
    }

    // Constructor CON ID ( Para consultar o leer en la BD)
    public Pedido(int idPedido, LocalDate fecha, float importe, boolean pagado, int idCliente) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.importe = importe;
        this.pagado = pagado;
        this.idCliente = idCliente;
    }

    // Getters y Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        return "Pedido [id=" + idPedido + ", fecha=" + fecha + ", importe=" + importe + "]";
    }
}