package com.syntaxsquad.ltd.apiCentroTreinamento.services;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.User;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AdministradorRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.InstrutorRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.UserRepository;
import jakarta.mail.MessagingException;

import com.syntaxsquad.ltd.apiCentroTreinamento.dto.UsersDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.enums.UserRole;
import com.syntaxsquad.ltd.apiCentroTreinamento.infra.security.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Autowired
    private AdministradorRepository adminRepository;
    @Autowired
    private InstrutorRepository instructorRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    /**
     * Autentica um usuário e retorna um token JWT.
     */
    public String authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Senha inválida");
        }

        // Converte UserRole único para uma lista de String
        List<String> roles = (user.getRole() != null)
                ? List.of(user.getRole().name()) 
                : List.of();

        return jwtUtil.generateToken(user.getEmail(), roles);
    }

    /**
     * Registra um novo usuário no sistema.
     */
    public void register(UsersDtoRequest usersDtoRequest) {
        // Verifica se o email já está cadastrado

        if (userRepository.findByEmail(usersDtoRequest.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("Email já cadastrado");
        }

        UserRole role = null;
    
    if (adminRepository.findByMatricula(usersDtoRequest.getMatricula()).isPresent()) {
        role = UserRole.ADMIN;
    } else if (instructorRepository.findByMatricula(usersDtoRequest.getMatricula()).isPresent()) {
        role = UserRole.TREINADOR;
    } else if (alunoRepository.findByMatricula(usersDtoRequest.getMatricula()).isPresent()) {
        role = UserRole.ALUNO;
    } else {
        throw new IllegalArgumentException("Matrícula informada não existe em nenhum cadastro");
    }
        User user = new User();
        // Cria o objeto user a partir do DTO
        user.setEmail(usersDtoRequest.getEmail());
        // Encripta a senha antes de salvar
        user.setPassword(passwordEncoder.encode(usersDtoRequest.getPassword()));
        user.setRole(role);
        userRepository.save(user);
    }

    /**
     * Recupera a senha de um usuário e envia um e-mail com a nova senha.
     * @throws MessagingException 
     */
    public Optional<User> recuperaSenha(String email) throws MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(email);
    
        if (userOptional.isPresent()) {
            User user = userOptional.get();
    
            // Gerar uma nova senha aleatória
            String novaSenha = gerarNovaSenha();
            System.out.println("Nova senha gerada: " + novaSenha);
    
            // Atualizar a senha do usuário no banco
            user.setPassword(passwordEncoder.encode(novaSenha));
            userRepository.save(user);
    
            // Enviar email com a nova senha
            System.out.println("Enviando e-mail para: " + user.getEmail());
            emailService.enviarEmail(user.getEmail(), "Redefinição de Senha", "Sua nova senha é: " + novaSenha);
    
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Gera uma senha aleatória segura usando SecureRandom.
     */
    private String gerarNovaSenha() {
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder(8);
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        
        for (int i = 0; i < 8; i++) {
            int indice = random.nextInt(caracteres.length());
            senha.append(caracteres.charAt(indice));
        }

        return senha.toString(); // Retorna uma senha segura de 8 caracteres
    }
}
