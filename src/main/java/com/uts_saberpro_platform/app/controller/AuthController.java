package com.uts_saberpro_platform.app.controller;

import com.uts_saberpro_platform.app.dto.RegistroEstudianteDTO;
import com.uts_saberpro_platform.app.model.Usuario;
import com.uts_saberpro_platform.app.repository.CarreraRepository;
import com.uts_saberpro_platform.app.repository.SedeRepository;
import com.uts_saberpro_platform.app.repository.UsuarioRepository;
import com.uts_saberpro_platform.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private SedeRepository sedeRepository;      // ← AGREGAR
    
    @Autowired
    private CarreraRepository carreraRepository;  // ← AGREGAR
    
    @GetMapping("/")
    public String root() {
        return "redirect:/landing";
    }
    
    @GetMapping("/landing")
    public String landingPage() {
        return "landing";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username, 
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        System.out.println("=== PROCESANDO LOGIN MANUAL ===");
        System.out.println("Email: " + username);
        System.out.println("Password: " + password);
        
        Usuario usuario = usuarioRepository.findByEmail(username).orElse(null);
        
        if (usuario == null) {
            System.out.println("Usuario no encontrado");
            model.addAttribute("error", "Usuario no encontrado");
            return "login";
        }
        
        System.out.println("Usuario encontrado: " + usuario.getEmail());
        System.out.println("Rol: " + usuario.getRol());
        
        session.setAttribute("usuario", usuario);
        
        switch (usuario.getRol()) {
            case ADMIN:
                return "redirect:/admin/dashboard";
            case COORDINACION:
                return "redirect:/coordinacion/dashboard";
            case DOCENTE:
                return "redirect:/docente/dashboard";
            case ESTUDIANTE:
                return "redirect:/estudiante/dashboard";
            default:
                return "redirect:/dashboard";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("estudianteDTO", new RegistroEstudianteDTO());
        model.addAttribute("sedes", sedeRepository.findAll());        // ← AGREGAR
        model.addAttribute("carreras", carreraRepository.findAll());  // ← AGREGAR
        return "registro";
    }
    
    @PostMapping("/registro")
    public String registrarEstudiante(@ModelAttribute RegistroEstudianteDTO dto) {
        if (usuarioService.existsByEmail(dto.getEmail())) {
            return "redirect:/registro?error=email";
        }
        
        usuarioService.registrarEstudiante(
            dto.getEmail(), dto.getPassword(), dto.getNombre(), dto.getApellido(),
            dto.getCedula(), dto.getTelefono(), dto.getCarreraId(), dto.getSedeId(), dto.getSemestre()
        );
        
        return "redirect:/registro?exito";
    }
}