package com.uts_saberpro_platform.app.repository;

import com.uts_saberpro_platform.app.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    
    List<Docente> findByEspecialidad(String especialidad);
    
    // ❌ ELIMINAR ESTE (no funciona porque Docente no tiene activo)
    // List<Docente> findByActivoTrue();
    
    // ✅ USAR ESTE
    @Query("SELECT d FROM Docente d WHERE d.usuario.activo = true")
    List<Docente> findByActivoTrue();
    
    @Query("SELECT d FROM Docente d WHERE d.usuario.email = :email")
    Optional<Docente> findByEmail(@Param("email") String email);
    
    @Query("SELECT d FROM Docente d WHERE d.usuario.id = :usuarioId")
    Optional<Docente> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}