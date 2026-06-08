package com.uts_saberpro_platform.app.controller;

import com.uts_saberpro_platform.app.model.*;
import com.uts_saberpro_platform.app.enums.EstadoPago;
import com.uts_saberpro_platform.app.enums.Rol;
import com.uts_saberpro_platform.app.enums.TipoCarrera;
import com.uts_saberpro_platform.app.repository.*;
import com.uts_saberpro_platform.app.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    @Autowired
    private DocenteRepository docenteRepository;
    
    @Autowired
    private CoordinadorRepository coordinadorRepository;
    
    @Autowired
    private AdministradorRepository administradorRepository;
    
    @Autowired
    private CarreraRepository carreraRepository;
    
    @Autowired
    private FacultadRepository facultadRepository;
    
    @Autowired
    private SedeRepository sedeRepository;
    
    @Autowired
    private ReciboPagoRepository reciboPagoRepository;
    
    @Autowired
    private ResolucionRepository resolucionRepository;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    // ==================== DASHBOARD ====================
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("recibosPendientes", reciboPagoRepository.findByEstado(EstadoPago.PENDIENTE).size());
        model.addAttribute("pagosAprobados", reciboPagoRepository.findByEstado(EstadoPago.APROBADO).size());
        model.addAttribute("recibos", reciboPagoRepository.findByEstado(EstadoPago.PENDIENTE));
        return "admin/dashboard";
    }
    
    // ==================== USUARIOS ====================
    @GetMapping("/usuarios")
    public String listarUsuarios(@RequestParam(required = false) String rol, Model model) {
        List<Usuario> usuarios;
        if (rol != null && !rol.isEmpty()) {
            usuarios = usuarioRepository.findByRol(Rol.valueOf(rol));
            model.addAttribute("filtro", rol);
        } else {
            usuarios = usuarioRepository.findAll();
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("sedes", sedeRepository.findAll());
        model.addAttribute("facultades", facultadRepository.findAll());
        return "admin/usuarios";
    }
    
    @GetMapping("/prueba")
    @ResponseBody
    public String prueba() {
        return "El controlador de admin funciona correctamente!";
    }
    
    @GetMapping("/usuario/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        model.addAttribute("usuario", usuario);
        model.addAttribute("sedes", sedeRepository.findAll());
        model.addAttribute("facultades", facultadRepository.findAll());
        return "admin/editar-usuario";
    }
    
    @PostMapping("/usuario/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id, 
                                    @RequestParam String nombre,
                                    @RequestParam String apellido,
                                    @RequestParam String telefono,
                                    @RequestParam(required = false) Long sedeId,
                                    @RequestParam(required = false) String especialidad,
                                    @RequestParam(required = false) Long facultadId) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setTelefono(telefono);
            if (sedeId != null) {
                usuario.setSede(sedeRepository.findById(sedeId).orElse(null));
            }
            usuarioRepository.save(usuario);
        }
        return "redirect:/admin/usuarios";
    }
    
    @GetMapping("/usuario/toggle/{id}")
    public String toggleUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setActivo(!usuario.getActivo());
            usuarioRepository.save(usuario);
        }
        return "redirect:/admin/usuarios";
    }
    
    @GetMapping("/usuario/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/admin/usuarios";
    }
    
    // ==================== CREAR USUARIO ====================
    @GetMapping("/crear-usuario")
    public String mostrarCrearUsuario(Model model) {
        model.addAttribute("sedes", sedeRepository.findAll());
        model.addAttribute("facultades", facultadRepository.findAll());
        return "admin/crear-usuario";
    }
    
    @PostMapping("/crear-usuario")
    public String crearUsuario(@RequestParam String email,
                               @RequestParam String nombre,
                               @RequestParam String apellido,
                               @RequestParam String cedula,
                               @RequestParam String telefono,
                               @RequestParam String rol,
                               @RequestParam(required = false) Long sedeId,
                               @RequestParam(required = false) String especialidad,
                               @RequestParam(required = false) Long facultadId) {
        
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword("$2a$10$8VvRkZi5BUpBxV5wV5wV5eV5eV5eV5eV5eV5eV5eV5eV5eV5eV5e");
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCedula(cedula);
        usuario.setTelefono(telefono);
        usuario.setRol(Rol.valueOf(rol));
        usuario.setActivo(true);
        
        if (sedeId != null) {
            usuario.setSede(sedeRepository.findById(sedeId).orElse(null));
        }
        
        usuarioRepository.save(usuario);
        return "redirect:/admin/usuarios";
    }
    
    // ==================== CARRERAS ====================
    @GetMapping("/carreras")
    public String gestionCarreras(Model model) {
        model.addAttribute("carrerasList", carreraRepository.findAll());
        model.addAttribute("facultades", facultadRepository.findAll());
        return "admin/carreras";
    }
    
    @PostMapping("/carreras/guardar")
    public String guardarCarrera(@ModelAttribute Carrera carrera) {
        carreraRepository.save(carrera);
        return "redirect:/admin/carreras";
    }
    
    @PostMapping("/carreras/actualizar/{id}")
    public String actualizarCarrera(@PathVariable Long id, 
                                    @RequestParam String nombre,
                                    @RequestParam String tipo,
                                    @RequestParam Integer duracionSemestres,
                                    @RequestParam Integer puntajeMinimoGraduacion,
                                    @RequestParam(required = false) Long facultadId) {
        Carrera carrera = carreraRepository.findById(id).orElse(null);
        if (carrera != null) {
            carrera.setNombre(nombre);
            carrera.setTipo(TipoCarrera.valueOf(tipo));
            carrera.setDuracionSemestres(duracionSemestres);
            carrera.setPuntajeMinimoGraduacion(puntajeMinimoGraduacion);
            if (facultadId != null) {
                carrera.setFacultad(facultadRepository.findById(facultadId).orElse(null));
            }
            carreraRepository.save(carrera);
        }
        return "redirect:/admin/carreras";
    }
    
    @GetMapping("/carreras/eliminar/{id}")
    public String eliminarCarrera(@PathVariable Long id) {
        carreraRepository.deleteById(id);
        return "redirect:/admin/carreras";
    }
    
 // ==================== FACULTADES ====================
    @GetMapping("/facultades")
    public String gestionFacultades(Model model) {
        model.addAttribute("facultadesList", facultadRepository.findAll());
        return "admin/facultades";
    }

    @PostMapping("/facultades/actualizar/{id}")
    public String actualizarFacultad(@PathVariable Long id,
                                     @RequestParam String nombre,
                                     @RequestParam(required = false) String decano,
                                     @RequestParam(required = false) String coordinadorNombre,
                                     @RequestParam(required = false) String coordinadorEmail) {
        Facultad facultad = facultadRepository.findById(id).orElse(null);
        if (facultad != null) {
            facultad.setNombre(nombre);
            facultad.setDecano(decano);
            facultad.setCoordinadorNombre(coordinadorNombre);
            facultad.setCoordinadorEmail(coordinadorEmail);
            facultadRepository.save(facultad);
        }
        return "redirect:/admin/facultades";
    }

    @GetMapping("/facultades/eliminar/{id}")
    public String eliminarFacultad(@PathVariable Long id) {
        facultadRepository.deleteById(id);
        return "redirect:/admin/facultades";
    }

    // ==================== SEDES ====================
    @GetMapping("/sedes")
    public String gestionSedes(Model model) {
        model.addAttribute("sedesList", sedeRepository.findAll());
        return "admin/sedes";
    }
    // ==================== RECIBOS ====================
    @GetMapping("/recibos")
    public String listarRecibos(Model model) {
        model.addAttribute("recibos", reciboPagoRepository.findByEstado(EstadoPago.PENDIENTE));
        return "admin/recibos";
    }
    
    @PostMapping("/recibos/aprobar/{id}")
    public String aprobarRecibo(@PathVariable Long id) {
        ReciboPago recibo = reciboPagoRepository.findById(id).orElse(null);
        if (recibo != null) {
            recibo.setEstado(EstadoPago.APROBADO);
            recibo.setComentarioAdmin("Pago aprobado por administrador");
            reciboPagoRepository.save(recibo);
            
            Estudiante estudiante = recibo.getEstudiante();
            if (estudiante != null) {
                estudiante.setPagoAprobado(true);
                estudianteRepository.save(estudiante);
            }
        }
        return "redirect:/admin/recibos";
    }
    
    @PostMapping("/recibos/rechazar/{id}")
    public String rechazarRecibo(@PathVariable Long id, @RequestParam String comentario) {
        ReciboPago recibo = reciboPagoRepository.findById(id).orElse(null);
        if (recibo != null) {
            recibo.setEstado(EstadoPago.RECHAZADO);
            recibo.setComentarioAdmin(comentario);
            reciboPagoRepository.save(recibo);
        }
        return "redirect:/admin/recibos";
    }
    
    // ==================== RESOLUCIONES ====================
    @GetMapping("/resoluciones")
    public String gestionResoluciones(Model model) {
        model.addAttribute("resoluciones", resolucionRepository.findAll());
        return "admin/resoluciones";
    }
    
    @PostMapping("/resoluciones/guardar")
    public String subirResolucion(@RequestParam String nombre, 
                                  @RequestParam("archivo") MultipartFile archivo,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (archivo.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Por favor seleccione un archivo");
                return "redirect:/admin/resoluciones";
            }
            
            // Limpiar el nombre del archivo (reemplazar espacios por guiones)
            String nombreLimpio = archivo.getOriginalFilename().replace(" ", "-");
            String nombreArchivo = UUID.randomUUID().toString() + "_" + nombreLimpio;
            
            // Guardar el archivo físicamente
            Path ruta = Paths.get("uploads/resoluciones/");
            if (!Files.exists(ruta)) {
                Files.createDirectories(ruta);
            }
            
            Path rutaCompleta = ruta.resolve(nombreArchivo);
            Files.write(rutaCompleta, archivo.getBytes());
            
            // Guardar en base de datos con la ruta completa
            Resolucion resolucion = new Resolucion();
            resolucion.setNombre(nombre);
            resolucion.setArchivoUrl("/uploads/resoluciones/" + nombreArchivo); // Ruta completa
            resolucion.setFechaPublicacion(LocalDate.now());
            resolucion.setActivo(true);
            resolucionRepository.save(resolucion);
            
            redirectAttributes.addFlashAttribute("success", "Resolución subida correctamente");
            
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al guardar el archivo: " + e.getMessage());
        }
        return "redirect:/admin/resoluciones";
    }
    
    @GetMapping("/resoluciones/ver/{id}")
    public String verResolucion(@PathVariable Long id) {
        Resolucion resolucion = resolucionRepository.findById(id).orElse(null);
        if (resolucion != null && resolucion.getArchivoUrl() != null) {
            return "redirect:" + resolucion.getArchivoUrl();
        }
        return "redirect:/admin/resoluciones";
    }
    
    @GetMapping("/resoluciones/eliminar/{id}")
    public String eliminarResolucion(@PathVariable Long id) {
        resolucionRepository.deleteById(id);
        return "redirect:/admin/resoluciones";
    }
    
    @GetMapping("/uploads/resoluciones/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> servirArchivo(@PathVariable String filename) {
        try {
            Path file = Paths.get("uploads/resoluciones/" + filename);
            Resource resource = new UrlResource(file.toUri());
            
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("No se pudo leer el archivo");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    
    
}