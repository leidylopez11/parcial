package com.uts_saberpro_platform.app.controller;

import com.uts_saberpro_platform.app.model.Carrera;
import com.uts_saberpro_platform.app.model.Estudiante;
import com.uts_saberpro_platform.app.model.Resolucion;
import com.uts_saberpro_platform.app.repository.CarreraRepository;
import com.uts_saberpro_platform.app.repository.EstudianteRepository;
import com.uts_saberpro_platform.app.repository.FacultadRepository;
import com.uts_saberpro_platform.app.repository.ResolucionRepository;
import com.uts_saberpro_platform.app.service.ExcelExportService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private CarreraRepository carreraRepository;
    
    @Autowired
    private FacultadRepository facultadRepository;
    
    @Autowired
    private ResolucionRepository resolucionRepository;
    
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
        return "docente/dashboard";
    }
    
    @GetMapping("/estudiantes")
    public String listarEstudiantes(Model model) {
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("facultades", facultadRepository.findAll());
        model.addAttribute("carreras", carreraRepository.findAll());
        return "docente/estudiantes";
    }
    
    @GetMapping("/estudiante/ver/{id}")
    public String verEstudiante(@PathVariable Long id, Model model) {
        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);
        model.addAttribute("estudiante", estudiante);
        return "docente/estudiante-detalle";
    }
    
    @GetMapping("/estudiante/resultados/{id}")
    public String verResultadosEstudiante(@PathVariable Long id, Model model) {
        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);
        model.addAttribute("estudiante", estudiante);
        return "docente/resultados-detalle";
    }
    
    @GetMapping("/informe-general")
    public String informeGeneral(Model model) {
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
        
        model.addAttribute("totalEstudiantes", total);
        model.addAttribute("cumplen", cumplen);
        model.addAttribute("conBeneficio", conBeneficio);
        model.addAttribute("pagosAprobados", pagosAprobados);
        model.addAttribute("carrerasData", carrerasData);
        return "docente/informe-general";
    }
    
    @GetMapping("/resoluciones")
    public String verResoluciones(Model model) {
        model.addAttribute("resoluciones", resolucionRepository.findAll());
        return "docente/resoluciones";
    }
}