package com.akumatech.backend.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Product {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank private String nombre;
    @Positive private int precio;
    @NotBlank private String imagen;

    private String descripcion;
    protected Product() {

    }

    public Product(String nombre, int precio, String imagen, String descripcion) {
        this.nombre = nombre; 
        this.precio = precio; 
        this.imagen = imagen; 
        this.descripcion = descripcion;
    }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public int getPrecio() { return precio; }
    public String getImagen() { return imagen; }
    public String getDescripcion() { return descripcion; }
    public void setNombre(String value) { nombre = value; }
    public void setPrecio(int value) { precio = value; }
    public void setImagen(String value) { imagen = value; }
    public void setDescripcion(String value) { descripcion = value; }
}
