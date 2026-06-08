package com.uts_saberpro_platform.app.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "resoluciones")
public class Resolucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String archivoUrl;
    private LocalDate fechaPublicacion;
    private Boolean activo = true;
    
    public Resolucion() {}
    
    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getArchivoUrl() { return archivoUrl; }
    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public Boolean getActivo() { return activo; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setArchivoUrl(String archivoUrl) { this.archivoUrl = archivoUrl; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}