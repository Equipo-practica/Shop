package model;

import java.time.LocalDate;

public class Envio {

    private int idEnvio;
    private String direccion;
    private int idPedido; // Foreign Key hacia la tabla Pedido
    private LocalDate fechaEntrega;
    private String estado; // Ej: "Pendiente", "Entregado", "Cancelado"

    // Constructor vacío
    public Envio() {
    }

    // Constructor completo
    public Envio(int idEnvio, String direccion, int idPedido, LocalDate fechaEntrega, String estado) {
        this.idEnvio = idEnvio;
        this.direccion = direccion;
        this.idPedido = idPedido;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
    }

    // Getters y Setters

    public int getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(int idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // toString para facilitar depuración
    @Override
    public String toString() {
        return "Envio{" +
                "id=" + idEnvio +
                ", direccion='" + direccion + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}