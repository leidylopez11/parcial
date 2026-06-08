package com.uts_saberpro_platform.app.controller;

import com.uts_saberpro_platform.app.model.Estudiante;
import com.uts_saberpro_platform.app.model.ReciboPago;
import com.uts_saberpro_platform.app.model.Resolucion;
import com.uts_saberpro_platform.app.model.Usuario;
import com.uts_saberpro_platform.app.repository.EstudianteRepository;
import com.uts_saberpro_platform.app.repository.ReciboPagoRepository;
import com.uts_saberpro_platform.app.repository.ResolucionRepository;
import com.uts_saberpro_platform.app.repository.UsuarioRepository;
import com.uts_saberpro_platform.app.service.EstudianteService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private ReciboPagoRepository reciboPagoRepository;
    
    @Autowired
    private EstudianteService estudianteService;
    
    @Autowired
    private ResolucionRepository resolucionRepository;
    
    private final String UPLOAD_DIR = "uploads/recibos/";
    
    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        System.out.println("=== EstudianteController.dashboard ===");
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        System.out.println("Usuario en sesión: " + (usuario != null ? usuario.getEmail() : "null"));
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Estudiante estudiante = estudianteRepository.findByUsuarioId(usuario.getId()).orElse(null);
        model.addAttribute("estudiante", estudiante);
        return "estudiante/dashboard";
    }
    
    @GetMapping("/cargar-pago")
    public String mostrarCargarPago(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        return "estudiante/cargar-pago";
    }
    
    @PostMapping("/cargar-pago")
    public String cargarPago(@RequestParam("archivo") MultipartFile archivo, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        
        Estudiante estudiante = estudianteRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (estudiante != null) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
                Path filePath = uploadPath.resolve(nombreArchivo);
                Files.write(filePath, archivo.getBytes());
                
                String archivoUrl = "/" + UPLOAD_DIR + nombreArchivo;
                estudianteService.subirReciboPago(estudiante.getId(), archivoUrl);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/estudiante/dashboard?pago=subido";
    }
    
    @GetMapping("/mis-resultados")
    public String verResultados(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        Estudiante estudiante = estudianteRepository.findByUsuarioId(usuario.getId()).orElse(null);
        model.addAttribute("estudiante", estudiante);
        return "estudiante/mis-resultados";
    }
    
    @GetMapping("/mis-beneficios")
    public String verBeneficios(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        Estudiante estudiante = estudianteRepository.findByUsuarioId(usuario.getId()).orElse(null);
        model.addAttribute("estudiante", estudiante);
        return "estudiante/mis-beneficios";
    }
    
    @GetMapping("/historial-pagos")
    public String verHistorialPagos(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }
        Estudiante estudiante = estudianteRepository.findByUsuarioId(usuario.getId()).orElse(null);
        List<ReciboPago> recibos = reciboPagoRepository.findByEstudianteId(estudiante.getId());
        model.addAttribute("recibos", recibos);
        return "estudiante/historial-pagos";
    }
    
    @GetMapping("/resoluciones")
    public String verResoluciones(Model model) {
        List<Resolucion> resoluciones = resolucionRepository.findAll();
        model.addAttribute("resoluciones", resoluciones);
        return "estudiante/resoluciones";
    }
}