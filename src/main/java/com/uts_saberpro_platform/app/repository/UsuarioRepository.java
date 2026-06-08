package com.uts_saberpro_platform.app.repository;

import com.uts_saberpro_platform.app.model.Usuario;
import com.uts_saberpro_platform.app.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCedula(String cedula);
    List<Usuario> findByRol(Rol rol);
    List<Usuario> findByActivoTrue();
    boolean existsByEmail(String email);
}