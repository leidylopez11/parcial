package com.uts_saberpro_platform.app.service;

import com.uts_saberpro_platform.app.model.*;
import com.uts_saberpro_platform.app.enums.EstadoPago;
import com.uts_saberpro_platform.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    
    @Autowired
    private ReciboPagoRepository reciboPagoRepository;
    
    @Autowired
    private ResolucionRepository resolucionRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private CarreraRepository carreraRepository;
    
    @Autowired
    private FacultadRepository facultadRepository;
    
    @Autowired
    private SedeRepository sedeRepository;
    
    public void aprobarReciboPago(Long reciboId, EstadoPago estado, String comentario) {
        ReciboPago recibo = reciboPagoRepository.findById(reciboId).orElse(null);
        if (recibo != null) {
            recibo.setEstado(estado);
            recibo.setComentarioAdmin(comentario);
            reciboPagoRepository.save(recibo);
            
            Estudiante estudiante = recibo.getEstudiante();
            estudiante.setPagoAprobado(estado == EstadoPago.APROBADO);
            estudianteRepository.save(estudiante);
        }
    }
    
    public void publicarResultados() {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        for (Estudiante e : estudiantes) {
            if (e.getResultadosPublicados() != null && e.getResultadosPublicados()) {
                // Ya están publicados, no hacer nada
            }
        }
    }
    
    public Resolucion subirResolucion(String nombre, String archivoUrl) {
        List<Resolucion> resoluciones = resolucionRepository.findAll();
        for (Resolucion r : resoluciones) {
            r.setActivo(false);
            resolucionRepository.save(r);
        }
        
        Resolucion nueva = new Resolucion();
        nueva.setNombre(nombre);
        nueva.setArchivoUrl(archivoUrl);
        nueva.setActivo(true);
        nueva.setFechaPublicacion(java.time.LocalDate.now());
        
        return resolucionRepository.save(nueva);
    }
    
    public Carrera crearCarrera(Carrera carrera) {
        return carreraRepository.save(carrera);
    }
    
    public Carrera actualizarCarrera(Carrera carrera) {
        return carreraRepository.save(carrera);
    }
    
    public void eliminarCarrera(Long id) {
        carreraRepository.deleteById(id);
    }
    
    public Facultad crearFacultad(Facultad facultad) {
        return facultadRepository.save(facultad);
    }
    
    public sede crearSede(sede sede) {
        return sedeRepository.save(sede);
    }
    
    public List<ReciboPago> listarRecibosPendientes() {
        return reciboPagoRepository.findByEstado(EstadoPago.PENDIENTE);
    }
}