package com.uts_saberpro_platform.app.service;

import com.uts_saberpro_platform.app.model.*;
import com.uts_saberpro_platform.app.enums.Rol;
import com.uts_saberpro_platform.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    
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
    private SedeRepository sedeRepository;
    
    @Autowired
    private CarreraRepository carreraRepository;
    
    @Autowired
    private FacultadRepository facultadRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // Registrar estudiante
    public Estudiante registrarEstudiante(String email, String password, String nombre, String apellido,
            String cedula, String telefono, Long carreraId, Long sedeId, Integer semestre) {

// Crear usuario base (Usuario NO es abstracto, tiene constructor)
Usuario usuario = new Usuario();
usuario.setEmail(email);
usuario.setPassword(passwordEncoder.encode(password));
usuario.setNombre(nombre);
usuario.setApellido(apellido);
usuario.setCedula(cedula);
usuario.setTelefono(telefono);
usuario.setRol(Rol.ESTUDIANTE);
usuario.setActivo(true);

if (sedeId != null) {
usuario.setSede(sedeRepository.findById(sedeId).orElse(null));
}

Usuario usuarioGuardado = usuarioRepository.save(usuario);

// Crear estudiante asociado
Estudiante estudiante = new Estudiante();
estudiante.setUsuario(usuarioGuardado);
estudiante.setSemestre(semestre);
estudiante.setPagoAprobado(false);
estudiante.setResultadosPublicados(false);
estudiante.setCumpleGraduacion(false);

if (carreraId != null) {
estudiante.setCarrera(carreraRepository.findById(carreraId).orElse(null));
}

return estudianteRepository.save(estudiante);
}
    
    // Crear docente (solo administrador)
    public Docente crearDocente(String email, String password, String nombre, String apellido, 
            String cedula, String telefono, Long sedeId, String especialidad) {

// Primero crear el usuario base
Usuario usuario = new Usuario() {};
usuario.setEmail(email);
usuario.setPassword(passwordEncoder.encode(password));
usuario.setNombre(nombre);
usuario.setApellido(apellido);
usuario.setCedula(cedula);
usuario.setTelefono(telefono);
usuario.setRol(Rol.DOCENTE);
usuario.setActivo(true);

if (sedeId != null) {
usuario.setSede(sedeRepository.findById(sedeId).orElse(null));
}

Usuario usuarioGuardado = usuarioRepository.save(usuario);

// Luego crear el docente asociado
Docente docente = new Docente();
docente.setUsuario(usuarioGuardado);
docente.setEspecialidad(especialidad);

return docenteRepository.save(docente);
}
    
    // Crear coordinador (solo administrador)
    public Coordinador crearCoordinador(String email, String password, String nombre, String apellido,
            String cedula, String telefono, Long sedeId, Long facultadId) {

// Primero crear el usuario base
Usuario usuario = new Usuario() {};
usuario.setEmail(email);
usuario.setPassword(passwordEncoder.encode(password));
usuario.setNombre(nombre);
usuario.setApellido(apellido);
usuario.setCedula(cedula);
usuario.setTelefono(telefono);
usuario.setRol(Rol.COORDINACION);
usuario.setActivo(true);

if (sedeId != null) {
usuario.setSede(sedeRepository.findById(sedeId).orElse(null));
}

Usuario usuarioGuardado = usuarioRepository.save(usuario);

// Luego crear el coordinador asociado
Coordinador coordinador = new Coordinador();
coordinador.setUsuario(usuarioGuardado);
if (facultadId != null) {
coordinador.setFacultad(facultadRepository.findById(facultadId).orElse(null));
}

return coordinadorRepository.save(coordinador);
}
    
    // Buscar por email
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    // Buscar por cédula
    public Optional<Usuario> findByCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula);
    }
    
    // Listar usuarios por rol
    public List<Usuario> findByRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }
    
    // Verificar si email existe
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    
    // Activar/desactivar usuario
    public void toggleActivo(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setActivo(!usuario.getActivo());
            usuarioRepository.save(usuario);
        }
    }
}