package com.uts_saberpro_platform.app.repository;

import com.uts_saberpro_platform.app.model.Resolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResolucionRepository extends JpaRepository<Resolucion, Long> {
}