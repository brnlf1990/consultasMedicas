package com.example.consultaMedica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    
    @NotBlank  
    @Column(nullable = false)
    private String descricao;
    
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private EstadoConsulta estado;

    @ManyToOne
    private User paciente;
    @ManyToOne
    private User medico;

    public Consulta() {
        this.estado = EstadoConsulta.PENDENTE; // Estado inicial
    }

    public Consulta(String descricao, LocalDateTime dataHora, User paciente) {
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.paciente = paciente;
        this.estado = EstadoConsulta.PENDENTE;
    }
    public void marcarComoRealizada() {
        this.estado = EstadoConsulta.CONCLUIDA;
    }
    public void marcarComoCancelada() {
        this.estado = EstadoConsulta.CANCELADA;
    }
    public void marcarComoPendente() {
        this.estado = EstadoConsulta.PENDENTE;
    }
    public String getEstado() {
        return estado.name();
    }
    public void setEstado(EstadoConsulta estado) {
        this.estado = estado;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    public User getPaciente() {
        return paciente;
    }
    public void setPaciente(User paciente) {
        this.paciente = paciente;
    }
    public User getMedico() {
        return medico; 
    }
    public void setMedico(User medico) {
        this.medico = medico;
    }

    public Consulta orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }

}
