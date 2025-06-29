package com.example.consultaMedica.service;

import com.example.consultaMedica.model.Consulta;
import com.example.consultaMedica.model.EstadoConsulta;
import com.example.consultaMedica.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    public void save(Consulta consulta) {
        consultaRepository.save(consulta);
    }

    public List<Consulta> findAll() {
        return consultaRepository.findAll();
    }

    public void cancelar(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada: " + id));
        consulta.setEstado(EstadoConsulta.CANCELADA);
        consultaRepository.save(consulta);
    }

    public List<Consulta> findByPaciente(UserDetails paciente) {
        return consultaRepository.findByPaciente(paciente);
    }

    public List<Consulta> findByDataHoraBetween(LocalDateTime start, LocalDateTime end) {
        return consultaRepository.findByDataHoraBetween(start, end);
    }

    public long countByEstado(EstadoConsulta estado) {
        return consultaRepository.countByEstado(estado);
    }
    public Consulta  findById(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada: " + id));
    }
}

 
   