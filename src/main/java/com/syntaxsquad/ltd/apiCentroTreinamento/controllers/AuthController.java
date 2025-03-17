package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import com.google.common.base.Optional;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.UsersDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.User;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.UserRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.services.UserService;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
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
    @PutMapping("/recuperar-senha/{email}")
    public ResponseEntity<String> recuperarSenha(@PathVariable String email) throws MessagingException {
        java.util.Optional<User> user = userService.recuperaSenha(email);
        if (user.isPresent()) {
            return ResponseEntity.ok("Senha redefinida com sucesso"); // Retorna status 200 se a senha for redefinida com sucesso
        } else {
            return ResponseEntity.status(404).body("Usuário nao encontrado"); // Retorna status 404 se o usuário nao for encontrado
        }
    }
}
