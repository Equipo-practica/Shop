package model;

import java.time.LocalDate;

public class Cliente {

    private int idCliente;
    private String nombre;
    private String email;
    private boolean vip;
    private LocalDate fechaAlta;

    // Constructor vacío
    public Cliente() {
    }

    // Constructor completo
    public Cliente(int idCliente, String nombre, String email, boolean vip, LocalDate fechaAlta) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.vip = vip;
        this.fechaAlta = fechaAlta;
    }

    // Getters y Setters

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    // toString para facilitar depuración
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + idCliente +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}