package com.uts_saberpro_platform.app.repository;

import com.uts_saberpro_platform.app.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    
    @Query("SELECT a FROM Administrador a WHERE a.usuario.email = :email")
    Optional<Administrador> findByEmail(@Param("email") String email);
    
    @Query("SELECT a FROM Administrador a WHERE a.usuario.id = :usuarioId")
    Optional<Administrador> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}