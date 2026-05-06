package com.example.controller;

import com.example.dto.LoginDTO;
import com.example.dto.LoginResponseDTO;
import com.example.dto.UserDTO;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("usuario")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }

    @PostMapping("/criar")
    public ResponseEntity<User> criarUsuario(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.criarUsuario(userDTO));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable UUID id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok("Usuário deletado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(userService.login(dto));
    }
}