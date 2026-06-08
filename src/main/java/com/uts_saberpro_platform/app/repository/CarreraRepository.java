package com.uts_saberpro_platform.app.repository;

import com.uts_saberpro_platform.app.model.Carrera;
import com.uts_saberpro_platform.app.enums.TipoCarrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    List<Carrera> findByActivoTrue();
    List<Carrera> findByTipo(TipoCarrera tipo);
    List<Carrera> findByFacultadId(Long facultadId);
    List<Carrera> findByNombreContainingIgnoreCase(String nombre);
}