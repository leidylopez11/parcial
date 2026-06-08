package com.uts_saberpro_platform.app.repository;

import com.uts_saberpro_platform.app.model.sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SedeRepository extends JpaRepository<sede, Long> {
    List<sede> findByActivoTrue();
    List<sede> findByCiudad(String ciudad);
}