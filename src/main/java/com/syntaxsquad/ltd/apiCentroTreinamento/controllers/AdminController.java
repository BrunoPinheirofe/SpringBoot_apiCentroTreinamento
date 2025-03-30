package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.syntaxsquad.ltd.apiCentroTreinamento.dto.AdminDtoResponse;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.AdministradorDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.ErrorDto;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Administrador;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.User;
import com.syntaxsquad.ltd.apiCentroTreinamento.enums.UserRole;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AdministradorRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.UserRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;

@RestController
@RequestMapping("/api/administradores")
public class AdminController {

    @Autowired
    private AdministradorRepository adminRepository;

    @Autowired
    private UserRepository userRepository;
    @CacheEvict(value = "admins_all", allEntries = true)
    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody AdministradorDtoRequest admin) {
        Optional<User> user = userRepository.findByEmail(admin.getEmail());
        if (user.isEmpty() || user.get().getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorDto("O usuário não tem permissão para criar administradores."));
        }

        Administrador administrador = new Administrador(
                admin.getNome(), admin.getSobrenome(), admin.getEmail(),
                admin.getTelefone(), admin.getDataNascimento(), admin.getIdade(), user.get());

        administrador = adminRepository.save(administrador);

        AdminDtoResponse adminDto = new AdminDtoResponse(
                administrador.getMatricula(), administrador.getNome(), administrador.getSobrenome(),
                administrador.getEmail(), administrador.getTelefone(), administrador.getDataNascimento(),
                administrador.getDataCadastro());

        return ResponseEntity.ok(adminDto);
    }
    @CacheEvict(value = "admins_all", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Matricula id, @RequestBody AdministradorDtoRequest admin) {
        Optional<Administrador> existingAdmin = adminRepository.findByMatricula(id);
        if (existingAdmin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorDto("Erro: Administrador não encontrado."));
        }

        Administrador administrador = existingAdmin.get();
        administrador.setNome(admin.getNome());
        administrador.setSobrenome(admin.getSobrenome());
        administrador.setEmail(admin.getEmail());
        administrador.setTelefone(admin.getTelefone());
        administrador.setDataNascimento(admin.getDataNascimento());

        administrador = adminRepository.save(administrador);

        AdminDtoResponse adminDto = new AdminDtoResponse(
                administrador.getMatricula(), administrador.getNome(), administrador.getSobrenome(),
                administrador.getEmail(), administrador.getTelefone(), administrador.getDataNascimento(),
                administrador.getDataCadastro());

        return ResponseEntity.ok(adminDto);
    }
    @CacheEvict(value = "admins_all", allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Matricula id) {
        Optional<Administrador> existingAdmin = adminRepository.findById(id);
        if (existingAdmin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorDto("Erro: Administrador não encontrado."));
        }

        adminRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Matricula id) {
        Optional<Administrador> admin = adminRepository.findById(id);
        if (admin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorDto("Erro: Administrador não encontrado."));
        }

        Administrador administrador = admin.get();
        AdminDtoResponse adminDto = new AdminDtoResponse(
                administrador.getMatricula(), administrador.getNome(), administrador.getSobrenome(),
                administrador.getEmail(), administrador.getTelefone(), administrador.getDataNascimento(),
                administrador.getDataCadastro());

        return ResponseEntity.ok(adminDto);
    }

    @GetMapping
    @Cacheable(value = "admins_all")
    public ResponseEntity<Iterable<AdminDtoResponse>> getAllAdmins() {
        Iterable<Administrador> admins = adminRepository.findAll();
        Iterable<AdminDtoResponse> adminsDto = StreamSupport.stream(admins.spliterator(), false)
                .map(admin -> new AdminDtoResponse(
                        admin.getMatricula(), admin.getNome(), admin.getSobrenome(),
                        admin.getEmail(), admin.getTelefone(), admin.getDataNascimento(),
                        admin.getDataCadastro()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(adminsDto);
    }
}
