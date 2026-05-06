package com.example.repository;

import com.example.model.Agenda;
import com.example.model.SalaReuniao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;


public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    Long id(Long id);

    Boolean existsBySalaAndDataAndHoraInicioAndHoraFinal(String sala, LocalDate data, LocalTime horaInicio, LocalTime horaFinal);

    List<Agenda> findBySalaAndData(SalaReuniao sala, LocalDate data);
}
