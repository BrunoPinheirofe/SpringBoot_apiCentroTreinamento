package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.UserDtoResponse;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.UsersDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.enums.UserRole;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Administrador;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Instrutor;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.User;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AdministradorRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.InstrutorRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.UserRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.services.UserService;

import jakarta.mail.MessagingException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdministradorRepository adminRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    // Endpoint para registrar um novo usuário
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsersDtoRequest userRequest) {

        try{
            userService.register(userRequest);
            Optional<User> newUser = userRepository.findByEmail(userRequest.getEmail());

            String token = userService.authenticate(userRequest.getEmail(), userRequest.getPassword());


            if (newUser.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("email", newUser.get().getEmail());
                response.put("role", newUser.get().getRole());
                if(newUser.get().getRole().equals("ALUNO")){
                    Optional<Aluno> aluno = alunoRepository.findByEmail(userRequest.getEmail());
                    if(aluno.isPresent()){
                        response.put("nome", aluno.get().getNome());
                        response.put("sobrenome", aluno.get().getSobrenome());
                        response.put("dataNascimento", aluno.get().getDataNascimento());
                    }
                } else if(newUser.get().getRole().equals(UserRole.INSTRUTOR)){
                    Optional<Instrutor> instrutor = instrutorRepository.findByEmail(userRequest.getEmail());
                    if(instrutor.isPresent()){
                        response.put("nome", instrutor.get().getNome());
                        response.put("sobrenome", instrutor.get().getSobrenome());
                        response.put("dataNascimento", instrutor.get().getDataNascimento());
                    }
                } else if(newUser.get().getRole().equals("ADMINISTRADOR")){
                    Optional<Administrador> admin = adminRepository.findByEmail(userRequest.getEmail());
                    if(admin.isPresent()){
                        response.put("nome", admin.get().getNome());
                        response.put("sobrenome", admin.get().getSobrenome());
                        response.put("dataNascimento", admin.get().getDataNascimento());
                    }
                }



                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar usuário após registro");
            }
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao registrar usuário: " + e.getMessage());
        }
    }

    // Endpoint para login e geração de token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsersDtoRequest user) {
        try {
            // Tenta autenticar o usuário e obter o token
            String token = userService.authenticate(user.getEmail(), user.getPassword());

            // Busca as informações adicionais do usuário
            Optional<User> userOptional = userRepository.findByEmail(user.getEmail());


            if (userOptional.isPresent()) {
                User loggedUser = userOptional.get();

                // Criando objeto de resposta
                Map<String, Object> response = new HashMap<>();

                response.put("token", token);
                response.put("email", loggedUser.getEmail());
                response.put("role", loggedUser.getRole());

                if(loggedUser.getRole().equals(UserRole.ALUNO)){
                    Optional<Aluno> aluno = alunoRepository.findByEmail(user.getEmail());
                    if(aluno.isPresent()){
                        response.put("nome", aluno.get().getNome());
                        response.put("sobrenome", aluno.get().getSobrenome());
                        response.put("dataNascimento", aluno.get().getDataNascimento());
                    }
                } else if(loggedUser.getRole().equals(UserRole.INSTRUTOR)){
                    Optional<Instrutor> instrutor = instrutorRepository.findByEmail(user.getEmail());
                    if(instrutor.isPresent()){
                        response.put("nome", instrutor.get().getNome());
                        response.put("sobrenome", instrutor.get().getSobrenome());
                        response.put("dataNascimento", instrutor.get().getDataNascimento());
                    }
                } else if(loggedUser.getRole().equals(UserRole.ADMIN)){
                    System.out.println("Entrou aqui");
                    Optional<Administrador> admin = adminRepository.findByEmail(user.getEmail());
                    if(admin.isPresent()){
                        response.put("nome", admin.get().getNome());
                        response.put("sobrenome", admin.get().getSobrenome());
                        response.put("dataNascimento", admin.get().getDataNascimento());
                    }
                }

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao realizar login: " + e.getMessage());
        } // Retorna o token caso a autenticação seja bem-sucedida
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
