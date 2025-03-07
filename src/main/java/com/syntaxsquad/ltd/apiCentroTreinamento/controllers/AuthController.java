package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import com.syntaxsquad.ltd.apiCentroTreinamento.dto.UsersDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.User;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.UserRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Endpoint para registrar um novo usuário
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UsersDtoRequest userRequest) {
        User user = new User(userRequest.getEmail(), userRequest.getPassword(), userRequest.getRole());
        userService.register(user);
        User newUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        return ResponseEntity.ok(newUser); // Retorna o usuário registrado
    }

    // Endpoint para login e geração de token
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsersDtoRequest user) {
        // Tenta autenticar o usuário e obter o token
        String token = userService.authenticate(user.getEmail(), user.getPassword());

        // Se o token for null, significa que a autenticação falhou
        if (token == null) {
            return ResponseEntity.status(401).body("Credenciais inválidas");  // Retorna status 401 se as credenciais forem inválidas
        }

        return ResponseEntity.ok(token);  // Retorna o token caso a autenticação seja bem-sucedida
    }
}
