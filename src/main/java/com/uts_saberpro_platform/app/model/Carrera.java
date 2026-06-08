package com.uts_saberpro_platform.app.model;

import com.uts_saberpro_platform.app.enums.TipoCarrera;  // ← IMPORT CORRECTO
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "carreras")
public class Carrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    
    @Enumerated(EnumType.STRING)
    private TipoCarrera tipo;
    
    private Integer duracionSemestres;
    private Integer puntajeMinimoGraduacion;
    private Boolean activo = true;
    private LocalDateTime fechaRegistro;
    
    @ManyToOne
    @JoinColumn(name = "facultad_id")
    private Facultad facultad;
    
    public Carrera() {}
    
    public Carrera(String nombre, TipoCarrera tipo, Integer duracionSemestres, Integer puntajeMinimoGraduacion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.duracionSemestres = duracionSemestres;
        this.puntajeMinimoGraduacion = puntajeMinimoGraduacion;
        this.fechaRegistro = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public TipoCarrera getTipo() { return tipo; }
    public void setTipo(TipoCarrera tipo) { this.tipo = tipo; }
    
    public Integer getDuracionSemestres() { return duracionSemestres; }
    public void setDuracionSemestres(Integer duracionSemestres) { this.duracionSemestres = duracionSemestres; }
    
    public Integer getPuntajeMinimoGraduacion() { return puntajeMinimoGraduacion; }
    public void setPuntajeMinimoGraduacion(Integer puntajeMinimoGraduacion) { this.puntajeMinimoGraduacion = puntajeMinimoGraduacion; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public Facultad getFacultad() { return facultad; }
    public void setFacultad(Facultad facultad) { this.facultad = facultad; }
}