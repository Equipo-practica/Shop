package com.shop.model;

import java.time.LocalDate;

public class Cliente {

    private int idCliente;          // Primary Key
    private String nombre;          // Requerido
    private String email;           // Requerido y que tenga formato válido
    private int puntosFidelidad;    // Entero
    private LocalDate fechaRegistro; // Tipo LocalDate


    public Cliente() {
    }


    public Cliente(String nombre, String email, int puntosFidelidad, LocalDate fechaRegistro) {
        this.nombre = nombre;
        this.email = email;
        this.puntosFidelidad = puntosFidelidad;
        this.fechaRegistro = fechaRegistro;
    }

    // Para recuperar desde la BD
    public Cliente(int idCliente, String nombre, String email, int puntosFidelidad, LocalDate fechaRegistro) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.puntosFidelidad = puntosFidelidad;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters desde la BD.
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getPuntosFidelidad() { return puntosFidelidad; }
    public void setPuntosFidelidad(int puntosFidelidad) { this.puntosFidelidad = puntosFidelidad; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + idCliente + ", nombre=" + nombre + ", email=" + email + '}';
    }
}