package com.uts_saberpro_platform.app.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "estudiantes")
public class Estudiante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "carrera_id")
    private Carrera carrera;
    
    private Integer semestre;
    private Integer puntajeTyt;
    private Integer puntajeSaberPro;
    
    // Puntajes detallados
    private Integer puntajeComunicacionEscrita;
    private String nivelComunicacionEscrita;
    private Integer puntajeRazonamientoCuantitativo;
    private String nivelRazonamientoCuantitativo;
    private Integer puntajeLecturaCritica;
    private String nivelLecturaCritica;
    private Integer puntajeCompetenciasCiudadanas;
    private String nivelCompetenciasCiudadanas;
    private Integer puntajeIngles;
    private String nivelIngles;
    private Integer puntajeFormulacionProyectos;
    private String nivelFormulacionProyectos;
    private Integer puntajePensamientoCientifico;
    private String nivelPensamientoCientifico;
    private Integer puntajeDisenoSoftware;
    private String nivelDisenoSoftware;
    
    // Beneficios y estado
    private String beneficioAplicable;
    private Integer descuentoGrado;
    private Boolean cumpleGraduacion;
    private Boolean pagoAprobado;
    private Boolean resultadosPublicados;
    private LocalDate fechaResultados;
    
    public Estudiante() {
        this.cumpleGraduacion = false;
        this.pagoAprobado = false;
        this.resultadosPublicados = false;
        this.descuentoGrado = 0;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Carrera getCarrera() { return carrera; }
    public void setCarrera(Carrera carrera) { this.carrera = carrera; }
    
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    
    public Integer getPuntajeTyt() { return puntajeTyt; }
    public void setPuntajeTyt(Integer puntajeTyt) { this.puntajeTyt = puntajeTyt; }
    
    public Integer getPuntajeSaberPro() { return puntajeSaberPro; }
    public void setPuntajeSaberPro(Integer puntajeSaberPro) { this.puntajeSaberPro = puntajeSaberPro; }
    
    public Integer getPuntajeComunicacionEscrita() { return puntajeComunicacionEscrita; }
    public void setPuntajeComunicacionEscrita(Integer puntajeComunicacionEscrita) { this.puntajeComunicacionEscrita = puntajeComunicacionEscrita; }
    
    public String getNivelComunicacionEscrita() { return nivelComunicacionEscrita; }
    public void setNivelComunicacionEscrita(String nivelComunicacionEscrita) { this.nivelComunicacionEscrita = nivelComunicacionEscrita; }
    
    public Integer getPuntajeRazonamientoCuantitativo() { return puntajeRazonamientoCuantitativo; }
    public void setPuntajeRazonamientoCuantitativo(Integer puntajeRazonamientoCuantitativo) { this.puntajeRazonamientoCuantitativo = puntajeRazonamientoCuantitativo; }
    
    public String getNivelRazonamientoCuantitativo() { return nivelRazonamientoCuantitativo; }
    public void setNivelRazonamientoCuantitativo(String nivelRazonamientoCuantitativo) { this.nivelRazonamientoCuantitativo = nivelRazonamientoCuantitativo; }
    
    public Integer getPuntajeLecturaCritica() { return puntajeLecturaCritica; }
    public void setPuntajeLecturaCritica(Integer puntajeLecturaCritica) { this.puntajeLecturaCritica = puntajeLecturaCritica; }
    
    public String getNivelLecturaCritica() { return nivelLecturaCritica; }
    public void setNivelLecturaCritica(String nivelLecturaCritica) { this.nivelLecturaCritica = nivelLecturaCritica; }
    
    public Integer getPuntajeCompetenciasCiudadanas() { return puntajeCompetenciasCiudadanas; }
    public void setPuntajeCompetenciasCiudadanas(Integer puntajeCompetenciasCiudadanas) { this.puntajeCompetenciasCiudadanas = puntajeCompetenciasCiudadanas; }
    
    public String getNivelCompetenciasCiudadanas() { return nivelCompetenciasCiudadanas; }
    public void setNivelCompetenciasCiudadanas(String nivelCompetenciasCiudadanas) { this.nivelCompetenciasCiudadanas = nivelCompetenciasCiudadanas; }
    
    public Integer getPuntajeIngles() { return puntajeIngles; }
    public void setPuntajeIngles(Integer puntajeIngles) { this.puntajeIngles = puntajeIngles; }
    
    public String getNivelIngles() { return nivelIngles; }
    public void setNivelIngles(String nivelIngles) { this.nivelIngles = nivelIngles; }
    
    public Integer getPuntajeFormulacionProyectos() { return puntajeFormulacionProyectos; }
    public void setPuntajeFormulacionProyectos(Integer puntajeFormulacionProyectos) { this.puntajeFormulacionProyectos = puntajeFormulacionProyectos; }
    
    public String getNivelFormulacionProyectos() { return nivelFormulacionProyectos; }
    public void setNivelFormulacionProyectos(String nivelFormulacionProyectos) { this.nivelFormulacionProyectos = nivelFormulacionProyectos; }
    
    public Integer getPuntajePensamientoCientifico() { return puntajePensamientoCientifico; }
    public void setPuntajePensamientoCientifico(Integer puntajePensamientoCientifico) { this.puntajePensamientoCientifico = puntajePensamientoCientifico; }
    
    public String getNivelPensamientoCientifico() { return nivelPensamientoCientifico; }
    public void setNivelPensamientoCientifico(String nivelPensamientoCientifico) { this.nivelPensamientoCientifico = nivelPensamientoCientifico; }
    
    public Integer getPuntajeDisenoSoftware() { return puntajeDisenoSoftware; }
    public void setPuntajeDisenoSoftware(Integer puntajeDisenoSoftware) { this.puntajeDisenoSoftware = puntajeDisenoSoftware; }
    
    public String getNivelDisenoSoftware() { return nivelDisenoSoftware; }
    public void setNivelDisenoSoftware(String nivelDisenoSoftware) { this.nivelDisenoSoftware = nivelDisenoSoftware; }
    
    public String getBeneficioAplicable() { return beneficioAplicable; }
    public void setBeneficioAplicable(String beneficioAplicable) { this.beneficioAplicable = beneficioAplicable; }
    
    public Integer getDescuentoGrado() { return descuentoGrado; }
    public void setDescuentoGrado(Integer descuentoGrado) { this.descuentoGrado = descuentoGrado; }
    
    public Boolean getCumpleGraduacion() { return cumpleGraduacion; }
    public void setCumpleGraduacion(Boolean cumpleGraduacion) { this.cumpleGraduacion = cumpleGraduacion; }
    
    public Boolean getPagoAprobado() { return pagoAprobado; }
    public void setPagoAprobado(Boolean pagoAprobado) { this.pagoAprobado = pagoAprobado; }
    
    public Boolean getResultadosPublicados() { return resultadosPublicados; }
    public void setResultadosPublicados(Boolean resultadosPublicados) { this.resultadosPublicados = resultadosPublicados; }
    
    public LocalDate getFechaResultados() { return fechaResultados; }
    public void setFechaResultados(LocalDate fechaResultados) { this.fechaResultados = fechaResultados; }
}