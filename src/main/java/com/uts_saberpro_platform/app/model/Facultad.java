package com.uts_saberpro_platform.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facultades")
public class Facultad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String decano;
    private String coordinadorNombre;
    private String coordinadorEmail;
    private Boolean activo = true;
    private LocalDateTime fechaRegistro;
    
    @OneToMany(mappedBy = "facultad", cascade = CascadeType.ALL)
    private List<Carrera> carreras = new ArrayList<>();
    
    public Facultad() {}
    
    public Facultad(String nombre, String decano, String coordinadorNombre, String coordinadorEmail) {
        this.nombre = nombre;
        this.decano = decano;
        this.coordinadorNombre = coordinadorNombre;
        this.coordinadorEmail = coordinadorEmail;
        this.fechaRegistro = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDecano() { return decano; }
    public void setDecano(String decano) { this.decano = decano; }
    public String getCoordinadorNombre() { return coordinadorNombre; }
    public void setCoordinadorNombre(String coordinadorNombre) { this.coordinadorNombre = coordinadorNombre; }
    public String getCoordinadorEmail() { return coordinadorEmail; }
    public void setCoordinadorEmail(String coordinadorEmail) { this.coordinadorEmail = coordinadorEmail; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public List<Carrera> getCarreras() { return carreras; }
    public void setCarreras(List<Carrera> carreras) { this.carreras = carreras; }
}