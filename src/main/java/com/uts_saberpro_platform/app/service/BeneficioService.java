package com.uts_saberpro_platform.app.service;

import com.uts_saberpro_platform.app.model.Carrera;
import com.uts_saberpro_platform.app.model.Estudiante;
import com.uts_saberpro_platform.app.enums.TipoCarrera;
import org.springframework.stereotype.Service;

@Service
public class BeneficioService {

    public void calcularBeneficios(Estudiante estudiante) {
        Carrera carrera = estudiante.getCarrera();
        
        if (carrera == null) {
            estudiante.setBeneficioAplicable("Carrera no asignada");
            estudiante.setDescuentoGrado(0);
            estudiante.setCumpleGraduacion(false);
            return;
        }
        
        if (carrera.getTipo() == TipoCarrera.TECNOLOGIA) {
            calcularBeneficiosTecnologia(estudiante);
        } else {
            calcularBeneficiosIngenieria(estudiante);
        }
        
        // Verificar si cumple con el puntaje mínimo para graduarse
        verificarCumpleGraduacion(estudiante);
    }
    
    private void calcularBeneficiosTecnologia(Estudiante estudiante) {
        Integer puntaje = estudiante.getPuntajeTyt();
        
        if (puntaje == null) {
            estudiante.setBeneficioAplicable("Sin resultados disponibles");
            estudiante.setDescuentoGrado(0);
            return;
        }
        
        if (puntaje >= 171) {
            estudiante.setBeneficioAplicable("Exención trabajo de grado (nota 5.0) + 100% descuento derecho de grado");
            estudiante.setDescuentoGrado(100);
        } else if (puntaje >= 151) {
            estudiante.setBeneficioAplicable("Exención trabajo de grado (nota 4.7) + 50% descuento derecho de grado");
            estudiante.setDescuentoGrado(50);
        } else if (puntaje >= 120) {
            estudiante.setBeneficioAplicable("Exención trabajo de grado (nota 4.5)");
            estudiante.setDescuentoGrado(0);
        } else {
            estudiante.setBeneficioAplicable("Sin beneficio - No alcanzó el puntaje mínimo");
            estudiante.setDescuentoGrado(0);
        }
    }
    
    private void calcularBeneficiosIngenieria(Estudiante estudiante) {
        Integer puntaje = estudiante.getPuntajeSaberPro();
        
        if (puntaje == null) {
            estudiante.setBeneficioAplicable("Sin resultados disponibles");
            estudiante.setDescuentoGrado(0);
            return;
        }
        
        if (puntaje >= 241) {
            estudiante.setBeneficioAplicable("Exención trabajo de grado (nota 5.0) + 100% descuento derecho de grado");
            estudiante.setDescuentoGrado(100);
        } else if (puntaje >= 211) {
            estudiante.setBeneficioAplicable("Exención trabajo de grado (nota 4.7) + 50% descuento derecho de grado");
            estudiante.setDescuentoGrado(50);
        } else if (puntaje >= 180) {
            estudiante.setBeneficioAplicable("Exención trabajo de grado (nota 4.5)");
            estudiante.setDescuentoGrado(0);
        } else {
            estudiante.setBeneficioAplicable("Sin beneficio - No alcanzó el puntaje mínimo");
            estudiante.setDescuentoGrado(0);
        }
    }
    
    private void verificarCumpleGraduacion(Estudiante estudiante) {
        Carrera carrera = estudiante.getCarrera();
        if (carrera == null) {
            estudiante.setCumpleGraduacion(false);
            return;
        }
        
        Integer puntaje = null;
        
        if (carrera.getTipo() == TipoCarrera.TECNOLOGIA) {
            puntaje = estudiante.getPuntajeTyt();
        } else {
            puntaje = estudiante.getPuntajeSaberPro();
        }
        
        if (puntaje != null && puntaje >= carrera.getPuntajeMinimoGraduacion()) {
            estudiante.setCumpleGraduacion(true);
        } else {
            estudiante.setCumpleGraduacion(false);
        }
    }
}