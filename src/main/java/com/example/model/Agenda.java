package com.example.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emailResponsavel;
    private String titulo;
    @Enumerated(EnumType.STRING)
    private SalaReuniao sala;
    private LocalDate data;
    private LocalTime horaFinal;
    private LocalTime horaInicio;
    @Column(nullable = true)
    private String reuniaoOnlineLink;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private User usuario;
}
