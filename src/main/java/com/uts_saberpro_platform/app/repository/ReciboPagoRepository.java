package com.uts_saberpro_platform.app.repository;

import com.uts_saberpro_platform.app.model.ReciboPago;
import com.uts_saberpro_platform.app.enums.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReciboPagoRepository extends JpaRepository<ReciboPago, Long> {
    List<ReciboPago> findByEstudianteId(Long estudianteId);
    List<ReciboPago> findByEstado(EstadoPago estado);
    List<ReciboPago> findByEstudianteIdAndEstado(Long estudianteId, EstadoPago estado);
    
    // ✅ AGREGAR ESTE MÉTODO
    @Query("SELECT r FROM ReciboPago r WHERE r.estado != :estado")
    List<ReciboPago> findByEstadoNot(@Param("estado") EstadoPago estado);
}