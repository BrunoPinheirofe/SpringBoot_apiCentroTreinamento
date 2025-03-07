package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Anamnese;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AnamneseRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/anamneses")
public class AnamneseController {

    @Autowired
    private AnamneseRepository anamneseRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    // Lista todas as anamneses
    @GetMapping
    public List<Anamnese> listarAnamneses() {
        return anamneseRepository.findAll();
    }

    // Busca anamnese por ID
    @GetMapping("/{id}")
    public ResponseEntity<Anamnese> buscarAnamnesePorId(@PathVariable Long id) {
        return anamneseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cria nova anamnese
    @PostMapping("/alunos/{alunoId}")
    public ResponseEntity<?> criarAnamnese(
            @PathVariable String alunoMatricula,
            @RequestBody Anamnese anamnese) {
        
        // Verifica se o aluno existe
        Aluno aluno = alunoRepository.findByMatricula(alunoMatricula).orElse(null);
        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        // Verifica se já existe anamnese para este aluno
        if (anamneseRepository.findByAluno(aluno).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Aluno já possui anamnese cadastrada");
        }

        anamnese.setAluno(aluno);
        anamnese.setDataCadastro(LocalDate.now());
        return ResponseEntity.ok(anamneseRepository.save(anamnese));
    }

    // Atualiza anamnese
    @PutMapping("/{id}")
    public ResponseEntity<Anamnese> atualizarAnamnese(
            @PathVariable Long id,
            @RequestBody Anamnese anamneseAtualizada) {
        
        if (!anamneseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        anamneseAtualizada.setId(id);
        return ResponseEntity.ok(anamneseRepository.save(anamneseAtualizada));
    }

    // Busca anamnese por aluno
    @GetMapping("/alunos/{alunoMatricula}")
    public ResponseEntity<Anamnese> buscarAnamnesePorAluno(@PathVariable String alunoMatricula) {
        Aluno aluno = alunoRepository.findByMatricula(alunoMatricula).orElse(null);
        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        return anamneseRepository.findByAluno(aluno)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Lista anamneses por data de cadastro
    @GetMapping("/data/{data}")
    public List<Anamnese> listarAnamnesesPorData(@PathVariable LocalDate data) {
        return anamneseRepository.findByDataCadastro(data);
    }
} 