package com.uts_saberpro_platform.app.repository;

import com.uts_saberpro_platform.app.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    
    List<Estudiante> findByCarreraId(Long carreraId);
    List<Estudiante> findByCumpleGraduacionTrue();
    List<Estudiante> findByResultadosPublicadosTrue();
    
    @Query("SELECT e FROM Estudiante e WHERE e.usuario.cedula = :cedula")
    Optional<Estudiante> findByCedula(@Param("cedula") String cedula);
    
    @Query("SELECT e FROM Estudiante e WHERE e.usuario.email = :email")
    Optional<Estudiante> findByEmail(@Param("email") String email);
    
    @Query("SELECT e FROM Estudiante e WHERE e.carrera.id = :carreraId AND e.semestre = :semestre")
    List<Estudiante> findByCarreraAndSemestre(@Param("carreraId") Long carreraId, @Param("semestre") Integer semestre);
    
    @Query("SELECT e FROM Estudiante e WHERE e.usuario.id = :usuarioId")
    Optional<Estudiante> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}