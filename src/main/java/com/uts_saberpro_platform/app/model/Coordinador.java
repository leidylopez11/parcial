package com.uts_saberpro_platform.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "coordinadores")
public class Coordinador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "facultad_id")
    private Facultad facultad;
    
    public Coordinador() {}
    
    public Coordinador(Usuario usuario, Facultad facultad) {
        this.usuario = usuario;
        this.facultad = facultad;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Facultad getFacultad() { return facultad; }
    public void setFacultad(Facultad facultad) { this.facultad = facultad; }
}