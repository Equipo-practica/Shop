package model;

import java.time.LocalDate;

public class Pedido {

    private int idPedido;
    private LocalDate fecha;
    private float importe;
    private boolean pagado;
    private int idCliente; // Clave foránea hacia Cliente

    // Constructor vacío
    public Pedido() {
    }

    // Constructor completo
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

    // toString para facilitar depuración
    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + idPedido +
                ", fecha=" + fecha +
                ", importe=" + importe +
                ", pagado=" + pagado +
                '}';
    }
}