package com.uts_saberpro_platform.app.repository;

import com.uts_saberpro_platform.app.model.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Long> {
    List<Facultad> findByActivoTrue();
    List<Facultad> findByNombreContainingIgnoreCase(String nombre);
}