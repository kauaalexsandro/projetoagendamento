package com.example.service;

import com.example.dto.AgendaDTO;
import com.example.exception.AgendaException;
import com.example.model.Agenda;
import com.example.model.User;
import com.example.repository.AgendaRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AgendaService {

    private static final ZoneId ZONE_ID = ZoneId.of("America/Cuiaba");

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private UserRepository userRepository;

    public Agenda create(AgendaDTO agendaDTO) {
        User usuarioLogado = buscarUsuarioLogado();

        validarHorarioAtual(agendaDTO);
        validarHoraFinal(agendaDTO);
        validarConflitoDeHorario(agendaDTO);

        Agenda agenda = new Agenda();
        agenda.setEmailResponsavel(agendaDTO.email());
        agenda.setTitulo(agendaDTO.titulo());
        agenda.setSala(agendaDTO.sala());
        agenda.setData(agendaDTO.data());
        agenda.setHoraInicio(agendaDTO.horaInicial());
        agenda.setHoraFinal(agendaDTO.horaFinal());
        agenda.setReuniaoOnlineLink(agendaDTO.reuniaoOnlineLink());
        agenda.setUsuario(usuarioLogado);

        return agendaRepository.save(agenda);
    }

    public List<Agenda> findAll() {
        return agendaRepository.findAll();
    }

    @Scheduled(fixedRate = 60000)
    public void deletarReunioesExpiradas() {
        LocalDate dataAtual = LocalDate.now(ZONE_ID);
        LocalTime horaAtual = LocalTime.now(ZONE_ID);

        List<Agenda> agendas = agendaRepository.findAll();

        for (Agenda agenda : agendas) {
            boolean dataJaPassou = agenda.getData().isBefore(dataAtual);
            boolean terminouHoje = agenda.getData().isEqual(dataAtual)
                    && agenda.getHoraFinal().isBefore(horaAtual);

            if (dataJaPassou || terminouHoje) {
                agendaRepository.delete(agenda);
            }
        }
    }

    public void deletarAgenda(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda não encontrada"));

        User usuarioLogado = buscarUsuarioLogado();

        if (!agenda.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new AgendaException("Você não tem permissão para deletar esta agenda.");
        }

        agendaRepository.delete(agenda);
    }

    private User buscarUsuarioLogado() {
        String emailUsuarioLogado = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));
    }

    private void validarHorarioAtual(AgendaDTO agendaDTO) {
        LocalDate dataAtual = LocalDate.now(ZONE_ID);
        LocalTime horaAtual = LocalTime.now(ZONE_ID);

        if (agendaDTO.data().isEqual(dataAtual) && agendaDTO.horaInicial().isBefore(horaAtual)) {
            throw new AgendaException("A hora agendada não pode ser inferior à hora atual!");
        }
    }

    private void validarHoraFinal(AgendaDTO agendaDTO) {
        if (!agendaDTO.horaFinal().isAfter(agendaDTO.horaInicial())) {
            throw new AgendaException("A hora final deve ser maior que a hora inicial!");
        }
    }

    private void validarConflitoDeHorario(AgendaDTO agendaDTO) {
        List<Agenda> agendasDaMesmaSalaEData = agendaRepository.findBySalaAndData(
                agendaDTO.sala(),
                agendaDTO.data()
        );

        for (Agenda agendaExistente : agendasDaMesmaSalaEData) {
            boolean conflito = agendaDTO.horaInicial().isBefore(agendaExistente.getHoraFinal())
                    && agendaDTO.horaFinal().isAfter(agendaExistente.getHoraInicio());

            if (conflito) {
                throw new AgendaException("Já existe uma reunião nessa sala nesse intervalo de horário.");
            }
        }
    }
}