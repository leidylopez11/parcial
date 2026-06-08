package com.uts_saberpro_platform.app.repository;

import com.uts_saberpro_platform.app.model.Coordinador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoordinadorRepository extends JpaRepository<Coordinador, Long> {
    
    List<Coordinador> findByFacultadId(Long facultadId);
    
    // ❌ ELIMINAR ESTE
    // List<Coordinador> findByActivoTrue();
    
    // ✅ USAR ESTE
    @Query("SELECT c FROM Coordinador c WHERE c.usuario.activo = true")
    List<Coordinador> findByActivoTrue();
    
    @Query("SELECT c FROM Coordinador c WHERE c.usuario.email = :email")
    Optional<Coordinador> findByEmail(@Param("email") String email);
    
    @Query("SELECT c FROM Coordinador c WHERE c.usuario.id = :usuarioId")
    Optional<Coordinador> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}