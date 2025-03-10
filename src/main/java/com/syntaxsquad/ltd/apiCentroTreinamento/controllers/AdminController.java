package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.syntaxsquad.ltd.apiCentroTreinamento.dto.AdministradorDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Administrador;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Instrutor;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.User;
import com.syntaxsquad.ltd.apiCentroTreinamento.enums.UserRole;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AdministradorRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.UserRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.InstrutorRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;

@RestController
@RequestMapping("/api/administradores")
public class AdminController {

    @Autowired
    private AdministradorRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    // Criar administrador
    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody AdministradorDtoRequest admin) {

         // Verifica se já existe um usuário com o mesmo email nos repositórios de Aluno, Instrutor ou Administrador
    
    Optional<Aluno> existingAluno = alunoRepository.findByEmail(admin.getEmail());
    Optional<Instrutor> existingInstrutor = instrutorRepository.findByEmail(admin.getEmail());
    Optional<Administrador> existingAdmin = adminRepository.findByEmail(admin.getEmail());

    if (existingAluno.isPresent() || existingInstrutor.isPresent() || existingAdmin.isPresent()) {
        return ResponseEntity.badRequest().body("Já existe um usuário com este email.");
    }


        // Verifica se o usuário com o email existe no banco de Users
        Optional<User> user = userRepository.findByEmail(admin.getEmail());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Usuário não encontrado com este email.");
        }

        // Verifica se o usuário tem o role de ADMIN
        if (user.get().getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body("O usuário não tem permissão para criar administradores.");
        }

        // Cria o objeto Administrador
        Administrador administrador = new Administrador(admin.getNome(), admin.getSobrenome(), admin.getEmail(), admin.getTelefone(), admin.getDataNascimento(), admin.getIdade(), user.get());

        // Salva o administrador no banco de dados
        adminRepository.save(administrador);

        // Retorna o administrador criado com um status 200 OK
        return ResponseEntity.ok(administrador);
    }

    // Atualizar administrador
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable String id, @RequestBody AdministradorDtoRequest admin) {

        // Verifica se o administrador existe
        Optional<Administrador> existingAdmin = adminRepository.findById(id);
        if (!existingAdmin.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: Administrador não encontrado.");
        }

        // Atualiza as informações do administrador
        Administrador administrador = existingAdmin.get();
        administrador.setNome(admin.getNome());
        administrador.setSobrenome(admin.getSobrenome());
        administrador.setEmail(admin.getEmail());
        administrador.setTelefone(admin.getTelefone());
        administrador.setDataNascimento(admin.getDataNascimento());
        administrador.setIdade(admin.getIdade());

        // Salva o administrador atualizado
        adminRepository.save(administrador);

        // Retorna o administrador atualizado com um status 200 OK
        return ResponseEntity.ok(administrador);
    }

    // Remover administrador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable String id) {

        // Verifica se o administrador existe
        Optional<Administrador> existingAdmin = adminRepository.findById(id);
        if (!existingAdmin.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Remove o administrador
        adminRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Buscar administrador por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable String id) {

        // Verifica se o administrador existe
        Optional<Administrador> admin = adminRepository.findById(id);
        if (!admin.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: Administrador não encontrado.");
        }

        // Retorna o administrador encontrado
        return ResponseEntity.ok(admin.get());
    }

    // Listar todos os administradores
    @GetMapping
    public ResponseEntity<Iterable<Administrador>> getAllAdmins() {
        Iterable<Administrador> admins = adminRepository.findAll();
        return ResponseEntity.ok(admins);
    }
}
