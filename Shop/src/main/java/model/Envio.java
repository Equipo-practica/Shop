package model;

import java.time.LocalDate;

public class Envio {

    private int idPedido;
    private int idEnvio;      // El id_cliente
    private String estado;      // Nombre cliente
    private String direccion;       // Email de cliente
    private LocalDate fecha_entrega;// Fecha de alta del mismo.

    public Envio(int id_envio, String direccion, int id_pedido,LocalDate fecha_entrega, String estado) {
        this.idEnvio = id_envio;
        this.estado = estado;
        this.idPedido = id_pedido;
        this.direccion = direccion;
        this.fecha_entrega = fecha_entrega;
    }

    // Getters y Setters

    public int getIdEnvio() { return idEnvio; }
    public void setIdEnvio(int idEnvio) { this.idEnvio = idEnvio; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaEntrega() { return fecha_entrega; }
    public void setFechaEntrega(LocalDate fecha_entrega) { this.fecha_entrega = fecha_entrega; }

}
