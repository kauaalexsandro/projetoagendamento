package com.example.dto;

import com.example.model.SalaReuniao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AgendaDTO(String email,String titulo, String reuniaoOnlineLink, SalaReuniao sala, LocalDate data, LocalTime horaInicial, LocalTime horaFinal){}
