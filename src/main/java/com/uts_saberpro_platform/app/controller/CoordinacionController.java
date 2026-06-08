package com.uts_saberpro_platform.app.controller;

import com.uts_saberpro_platform.app.dto.ResultadosDTO;
import com.uts_saberpro_platform.app.model.Estudiante;
import com.uts_saberpro_platform.app.model.Carrera;
import com.uts_saberpro_platform.app.model.Resolucion;
import com.uts_saberpro_platform.app.model.ReciboPago;
import com.uts_saberpro_platform.app.enums.EstadoPago;
import com.uts_saberpro_platform.app.enums.TipoCarrera;
import com.uts_saberpro_platform.app.repository.CarreraRepository;
import com.uts_saberpro_platform.app.repository.EstudianteRepository;
import com.uts_saberpro_platform.app.repository.FacultadRepository;
import com.uts_saberpro_platform.app.repository.ResolucionRepository;
import com.uts_saberpro_platform.app.repository.ReciboPagoRepository;
import com.uts_saberpro_platform.app.service.EstudianteService;
import com.uts_saberpro_platform.app.service.ExcelExportService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/coordinacion")
public class CoordinacionController {
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private CarreraRepository carreraRepository;
    
    @Autowired
    private FacultadRepository facultadRepository;
    
    @Autowired
    private EstudianteService estudianteService;
    
    @Autowired
    private ResolucionRepository resolucionRepository;
    
    @Autowired
    private ReciboPagoRepository reciboPagoRepository;
    
    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/estudiantes/exportar-excel")
    public void exportarEstudiantesExcel(HttpServletResponse response) throws IOException {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        excelExportService.exportarEstudiantesExcel(estudiantes, response);
    }

    @GetMapping("/informe-general/exportar-excel")
    public void exportarInformeGeneralExcel(HttpServletResponse response) throws IOException {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        long total = estudiantes.size();
        long cumplen = estudiantes.stream().filter(Estudiante::getCumpleGraduacion).count();
        long conBeneficio = estudiantes.stream().filter(e -> e.getBeneficioAplicable() != null && !e.getBeneficioAplicable().contains("Sin beneficio")).count();
        long pagosAprobados = estudiantes.stream().filter(Estudiante::getPagoAprobado).count();
        
        List<Carrera> carreras = carreraRepository.findAll();
        List<Map<String, Object>> carrerasData = new ArrayList<>();
        
        for (Carrera c : carreras) {
            Map<String, Object> data = new HashMap<>();
            data.put("nombre", c.getNombre());
            List<Estudiante> estXCarrera = estudianteRepository.findByCarreraId(c.getId());
            data.put("total", estXCarrera.size());
            data.put("cumplen", estXCarrera.stream().filter(Estudiante::getCumpleGraduacion).count());
            data.put("beneficios", estXCarrera.stream().filter(e -> e.getBeneficioAplicable() != null && !e.getBeneficioAplicable().contains("Sin beneficio")).count());
            carrerasData.add(data);
        }
        
        excelExportService.exportarInformeGeneralExcel(carrerasData, total, cumplen, conBeneficio, pagosAprobados, response);
    }
    
    // ==================== RECIBOS DE PAGO ====================
    @GetMapping("/recibos")
    public String gestionRecibos(Model model) {
        model.addAttribute("recibosPendientes", reciboPagoRepository.findByEstado(EstadoPago.PENDIENTE));
        model.addAttribute("recibosHistorial", reciboPagoRepository.findByEstadoNot(EstadoPago.PENDIENTE));
        return "coordinacion/recibos";
    }

    @PostMapping("/recibos/aprobar/{id}")
    public String aprobarRecibo(@PathVariable Long id) {
        ReciboPago recibo = reciboPagoRepository.findById(id).orElse(null);
        if (recibo != null) {
            recibo.setEstado(EstadoPago.APROBADO);
            recibo.setComentarioAdmin("Pago aprobado por coordinación");
            reciboPagoRepository.save(recibo);
            
            Estudiante estudiante = recibo.getEstudiante();
            if (estudiante != null) {
                estudiante.setPagoAprobado(true);
                estudianteRepository.save(estudiante);
            }
        }
        return "redirect:/coordinacion/recibos";
    }

    @PostMapping("/recibos/rechazar/{id}")
    public String rechazarRecibo(@PathVariable Long id, @RequestParam String comentario) {
        ReciboPago recibo = reciboPagoRepository.findById(id).orElse(null);
        if (recibo != null) {
            recibo.setEstado(EstadoPago.RECHAZADO);
            recibo.setComentarioAdmin(comentario);
            reciboPagoRepository.save(recibo);
        }
        return "redirect:/coordinacion/recibos";
    }
    
    // ==================== DASHBOARD ====================
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        long total = estudiantes.size();
        long cumplen = estudiantes.stream().filter(Estudiante::getCumpleGraduacion).count();
        long conBeneficio = estudiantes.stream().filter(e -> e.getBeneficioAplicable() != null && !e.getBeneficioAplicable().contains("Sin beneficio")).count();
        
        model.addAttribute("totalEstudiantes", total);
        model.addAttribute("cumplen", cumplen);
        model.addAttribute("conBeneficio", conBeneficio);
        model.addAttribute("ultimosEstudiantes", estudiantes.stream().limit(5).collect(Collectors.toList()));
        return "coordinacion/dashboard";
    }
    
    // ==================== ESTUDIANTES ====================
    @GetMapping("/estudiantes")
    public String listarEstudiantes(Model model) {
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("facultades", facultadRepository.findAll());
        model.addAttribute("carreras", carreraRepository.findAll());
        return "coordinacion/estudiantes";
    }
    
    @GetMapping("/estudiante/ver/{id}")
    public String verEstudiante(@PathVariable Long id, Model model) {
        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);
        model.addAttribute("estudiante", estudiante);
        return "coordinacion/estudiante-detalle";
    }
    
    @GetMapping("/estudiante/resultados/{id}")
    public String verResultadosEstudiante(@PathVariable Long id, Model model) {
        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);
        model.addAttribute("estudiante", estudiante);
        return "coordinacion/resultados-detalle";
    }
    
    // ==================== RESULTADOS ====================
    @GetMapping("/resultados")
    public String gestionResultados(Model model) {
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("facultades", facultadRepository.findAll());
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("resultadosDTO", new ResultadosDTO());
        return "coordinacion/resultados";
    }
    
    @GetMapping("/resultados/subir/{id}")
    public String mostrarFormularioResultados(@PathVariable Long id, Model model) {
        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("resultadosDTO", new ResultadosDTO());
        return "coordinacion/resultados-subir";
    }
    
    @PostMapping("/resultados/subir/{id}")
    public String subirResultados(@PathVariable Long id, @ModelAttribute ResultadosDTO dto) {
        dto.setEstudianteId(id);
        estudianteService.guardarResultados(id, dto);
        return "redirect:/coordinacion/resultados?exito";
    }
    
    @PostMapping("/resultados/guardar")
    public String guardarResultados(@ModelAttribute ResultadosDTO dto) {
        estudianteService.guardarResultados(dto.getEstudianteId(), dto);
        return "redirect:/coordinacion/resultados?exito";
    }
    
    // ==================== BENEFICIOS ====================
    @GetMapping("/beneficios")
    public String gestionBeneficios(Model model) {
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        return "coordinacion/beneficios";
    }
    
    // Método de edición mediante modal (ya no se usa página separada)
    // @GetMapping("/beneficio/editar/{id}")
    // public String editarBeneficio(@PathVariable Long id, Model model) {
    //     Estudiante estudiante = estudianteRepository.findById(id).orElse(null);
    //     model.addAttribute("estudiante", estudiante);
    //     return "coordinacion/beneficio-editar";
    // }
    
    @PostMapping("/beneficio/actualizar/{id}")
    public String actualizarBeneficio(@PathVariable Long id, 
                                      @RequestParam String beneficioAplicable,
                                      @RequestParam Integer descuentoGrado,
                                      RedirectAttributes redirectAttributes) {
        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);
        if (estudiante != null) {
            // Obtener el puntaje real del estudiante según su carrera
            Integer puntajeReal = null;
            if (estudiante.getCarrera().getTipo() == TipoCarrera.TECNOLOGIA) {
                puntajeReal = estudiante.getPuntajeTyt();
            } else {
                puntajeReal = estudiante.getPuntajeSaberPro();
            }
            
            // Validar si el beneficio es válido según el puntaje
            boolean beneficioValido = validarBeneficioSegunPuntaje(beneficioAplicable, puntajeReal, estudiante.getCarrera().getTipo());
            
            if (!beneficioValido) {
                redirectAttributes.addFlashAttribute("error", "No se puede asignar este beneficio porque el estudiante no alcanzó el puntaje requerido. Puntaje actual: " + puntajeReal);
                return "redirect:/coordinacion/beneficios";
            }
            
            estudiante.setBeneficioAplicable(beneficioAplicable);
            estudiante.setDescuentoGrado(descuentoGrado);
            estudianteRepository.save(estudiante);
            
            redirectAttributes.addFlashAttribute("success", "Beneficio actualizado correctamente");
        }
        return "redirect:/coordinacion/beneficios";
    }

    private boolean validarBeneficioSegunPuntaje(String beneficio, Integer puntaje, TipoCarrera tipo) {
        if (puntaje == null) return false;
        
        if (tipo == TipoCarrera.TECNOLOGIA) {
            if (beneficio.contains("100% descuento") && puntaje < 171) return false;
            if (beneficio.contains("50% descuento") && puntaje < 151) return false;
            if (beneficio.contains("Exención trabajo de grado (nota 4.5)") && puntaje < 120) return false;
        } else {
            if (beneficio.contains("100% descuento") && puntaje < 241) return false;
            if (beneficio.contains("50% descuento") && puntaje < 211) return false;
            if (beneficio.contains("Exención trabajo de grado (nota 4.5)") && puntaje < 180) return false;
        }
        return true;
    }
    
    // ==================== INFORME GENERAL ====================
    @GetMapping("/informe-general")
    public String informeGeneral(Model model) {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        long total = estudiantes.size();
        long cumplen = estudiantes.stream().filter(Estudiante::getCumpleGraduacion).count();
        long conBeneficio = estudiantes.stream().filter(e -> e.getBeneficioAplicable() != null && !e.getBeneficioAplicable().contains("Sin beneficio")).count();
        long pagosAprobados = estudiantes.stream().filter(Estudiante::getPagoAprobado).count();
        
        // Datos por carrera
        List<Carrera> carreras = carreraRepository.findAll();
        List<Map<String, Object>> carrerasData = new ArrayList<>();
        
        for (Carrera c : carreras) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", c.getId());
            data.put("nombre", c.getNombre());
            data.put("tipo", c.getTipo().toString());
            
            List<Estudiante> estXCarrera = estudianteRepository.findByCarreraId(c.getId());
            long totalCarrera = estXCarrera.size();
            long cumplenCarrera = estXCarrera.stream().filter(Estudiante::getCumpleGraduacion).count();
            long beneficiosCarrera = estXCarrera.stream().filter(e -> e.getBeneficioAplicable() != null && !e.getBeneficioAplicable().contains("Sin beneficio")).count();
            
            data.put("total", totalCarrera);
            data.put("cumplen", cumplenCarrera);
            data.put("beneficios", beneficiosCarrera);
            
            carrerasData.add(data);
        }
        
        model.addAttribute("totalEstudiantes", total);
        model.addAttribute("cumplen", cumplen);
        model.addAttribute("conBeneficio", conBeneficio);
        model.addAttribute("pagosAprobados", pagosAprobados);
        model.addAttribute("carrerasData", carrerasData);
        
        return "coordinacion/informe-general";
    }
    
    // ==================== RESOLUCIONES ====================
    @GetMapping("/resoluciones")
    public String verResoluciones(Model model) {
        model.addAttribute("resoluciones", resolucionRepository.findAll());
        return "coordinacion/resoluciones";
    }
}