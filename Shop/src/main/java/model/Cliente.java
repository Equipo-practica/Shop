package model;

import java.time.LocalDate;

public class Cliente {

    private int idCliente;      // El id_cliente
    private String nombre;      // Nombre cliente
    private String email;       // Email de cliente
    private boolean vip;        // SI el cliente es VIP (boolean)
    private LocalDate fechaAlta;// Fecha de alta del mismo.

    public Cliente() {
    }

    // Constructor SIN ID (para insertar los datos)
    public Cliente(String nombre, String cliente, String email, boolean vip, LocalDate fechaAlta) {
        this.nombre = nombre;
        this.email = email;
        this.vip = vip;
        this.fechaAlta = fechaAlta;
    }

    // Constructor CON ID (para Consultar/Leer de la BD)
    public Cliente(int idCliente, String nombre, String cliente, String email, boolean vip, LocalDate fechaAlta) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.vip = vip;
        this.fechaAlta = fechaAlta;
    }

    // Getters y Setters


    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isVip() { return vip; } // El getter de boolean suele empezar por 'is'
    public void setVip(boolean vip) { this.vip = vip; }

    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    @Override
    public String toString() {
        return "Cliente [id=" + idCliente + ", nombre=" + nombre + ", vip=" + vip + "]";
    }
}