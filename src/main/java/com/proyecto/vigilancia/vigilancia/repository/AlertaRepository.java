package com.proyecto.vigilancia.vigilancia.repository;

import com.proyecto.vigilancia.vigilancia.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Integer> {

    // Últimas 10 alertas ordenadas por fecha (para tabla en frontend)
    List<Alerta> findTop10ByOrderByFechaAlertaDesc();

    // Conteo de alertas por día de la semana (MySQL: 1=domingo, 2=lunes, ... 7=sábado)
    @Query("SELECT FUNCTION('DAYOFWEEK', a.fechaAlerta) AS dia, COUNT(a) " +
           "FROM Alerta a " +
           "WHERE a.fechaAlerta >= :desde " +
           "GROUP BY FUNCTION('DAYOFWEEK', a.fechaAlerta)")
    List<Object[]> contarAlertasPorDia(@Param("desde") LocalDateTime desde);
}