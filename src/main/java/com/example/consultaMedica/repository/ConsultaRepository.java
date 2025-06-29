package com.example.consultaMedica.repository;

import com.example.consultaMedica.model.Consulta;
import com.example.consultaMedica.model.EstadoConsulta;

import org.springframework.data.jpa.repository.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPaciente(UserDetails paciente);
    List<Consulta> findByDataHoraBetween(LocalDateTime start, LocalDateTime end);
    long countByEstado(EstadoConsulta estado);
}
