package com.example.controller;

import com.example.dto.AgendaDTO;
import com.example.model.Agenda;
import com.example.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @GetMapping
    public List<Agenda> findAll() {
        return agendaService.findAll();
    }

    @PostMapping("/criarAgenda")
    public ResponseEntity<Agenda> criarAgenda(@RequestBody AgendaDTO agendaDTO) {
        return ResponseEntity.ok(agendaService.create(agendaDTO));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarAgenda(@PathVariable Long id) {
        agendaService.deletarAgenda(id);
        return ResponseEntity.ok("Agenda deletada com sucesso!");
    }
}