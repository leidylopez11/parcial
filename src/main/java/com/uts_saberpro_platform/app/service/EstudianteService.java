package com.uts_saberpro_platform.app.service;

import com.uts_saberpro_platform.app.dto.ResultadosDTO;
import com.uts_saberpro_platform.app.model.Estudiante;
import com.uts_saberpro_platform.app.model.ReciboPago;
import com.uts_saberpro_platform.app.enums.EstadoPago;
import com.uts_saberpro_platform.app.repository.EstudianteRepository;
import com.uts_saberpro_platform.app.repository.ReciboPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private ReciboPagoRepository reciboPagoRepository;
    
    @Autowired
    private BeneficioService beneficioService;
    
    public void guardarResultados(Long estudianteId, ResultadosDTO resultadosDTO) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId).orElse(null);
        if (estudiante == null) return;
        
        if (estudiante.getCarrera().getTipo() == com.uts_saberpro_platform.app.enums.TipoCarrera.TECNOLOGIA) {
            estudiante.setPuntajeTyt(resultadosDTO.getPuntajeTyt());
        } else {
            estudiante.setPuntajeSaberPro(resultadosDTO.getPuntajeSaberPro());
        }
        
        estudiante.setPuntajeComunicacionEscrita(resultadosDTO.getPuntajeComunicacionEscrita());
        estudiante.setNivelComunicacionEscrita(resultadosDTO.getNivelComunicacionEscrita());
        estudiante.setPuntajeRazonamientoCuantitativo(resultadosDTO.getPuntajeRazonamientoCuantitativo());
        estudiante.setNivelRazonamientoCuantitativo(resultadosDTO.getNivelRazonamientoCuantitativo());
        estudiante.setPuntajeLecturaCritica(resultadosDTO.getPuntajeLecturaCritica());
        estudiante.setNivelLecturaCritica(resultadosDTO.getNivelLecturaCritica());
        estudiante.setPuntajeCompetenciasCiudadanas(resultadosDTO.getPuntajeCompetenciasCiudadanas());
        estudiante.setNivelCompetenciasCiudadanas(resultadosDTO.getNivelCompetenciasCiudadanas());
        estudiante.setPuntajeIngles(resultadosDTO.getPuntajeIngles());
        estudiante.setNivelIngles(resultadosDTO.getNivelIngles());
        estudiante.setPuntajeFormulacionProyectos(resultadosDTO.getPuntajeFormulacionProyectos());
        estudiante.setNivelFormulacionProyectos(resultadosDTO.getNivelFormulacionProyectos());
        estudiante.setPuntajePensamientoCientifico(resultadosDTO.getPuntajePensamientoCientifico());
        estudiante.setNivelPensamientoCientifico(resultadosDTO.getNivelPensamientoCientifico());
        estudiante.setPuntajeDisenoSoftware(resultadosDTO.getPuntajeDisenoSoftware());
        estudiante.setNivelDisenoSoftware(resultadosDTO.getNivelDisenoSoftware());
        
        estudiante.setResultadosPublicados(true);
        estudiante.setFechaResultados(LocalDate.now());
        
        beneficioService.calcularBeneficios(estudiante);
        
        estudianteRepository.save(estudiante);
    }
    
    public ReciboPago subirReciboPago(Long estudianteId, String archivoUrl) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId).orElse(null);
        if (estudiante == null) return null;
        
        ReciboPago recibo = new ReciboPago();
        recibo.setEstudiante(estudiante);
        recibo.setArchivoUrl(archivoUrl);
        recibo.setEstado(EstadoPago.PENDIENTE);
        
        return reciboPagoRepository.save(recibo);
    }
    
    public Estudiante verMisResultados(Long estudianteId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId).orElse(null);
        if (estudiante != null && estudiante.getResultadosPublicados()) {
            return estudiante;
        }
        return null;
    }
    
    public List<Estudiante> listarPorCarrera(Long carreraId) {
        return estudianteRepository.findByCarreraId(carreraId);
    }
    
    public List<Estudiante> listarCumplenGraduacion() {
        return estudianteRepository.findByCumpleGraduacionTrue();
    }
}