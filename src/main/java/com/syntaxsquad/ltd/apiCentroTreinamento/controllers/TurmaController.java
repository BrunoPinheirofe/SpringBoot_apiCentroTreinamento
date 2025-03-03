package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Turma;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.TurmaRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    // Lista todas as turmas
    @GetMapping
    public List<Turma> listarTurmas() {
        return turmaRepository.findAll();
    }

    // Busca turma por ID
    @GetMapping("/{id}")
    public ResponseEntity<Turma> buscarTurmaPorId(@PathVariable Long id) {
        return turmaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cria nova turma
    @PostMapping
    public ResponseEntity<Turma> criarTurma(@RequestBody Turma turma) {
        // Verifica se já existe turma no mesmo horário
        if (!turmaRepository.findByDiaSemanaAndHorario(turma.getDiaSemana(), turma.getHorario()).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(turmaRepository.save(turma));
    }

    // Adiciona aluno à turma
    @PostMapping("/{turmaId}/alunos/{alunoId}")
    public ResponseEntity<?> adicionarAluno(@PathVariable Long turmaId, @PathVariable Long alunoId) {
        Turma turma = turmaRepository.findById(turmaId).orElse(null);
        Aluno aluno = alunoRepository.findById(alunoId).orElse(null);

        if (turma == null || aluno == null) {
            return ResponseEntity.notFound().build();
        }

        // Verifica limite de alunos (exemplo: máximo 20 por turma)
        if (turmaRepository.countAlunosByTurmaId(turmaId) >= 20) {
            return ResponseEntity.badRequest()
                    .body("Turma atingiu o limite máximo de alunos");
        }

        turma.getAlunos().add(aluno);
        return ResponseEntity.ok(turmaRepository.save(turma));
    }

    // Remove aluno da turma
    @DeleteMapping("/{turmaId}/alunos/{alunoId}")
    public ResponseEntity<?> removerAluno(@PathVariable Long turmaId, @PathVariable Long alunoId) {
        Turma turma = turmaRepository.findById(turmaId).orElse(null);
        Aluno aluno = alunoRepository.findById(alunoId).orElse(null);

        if (turma == null || aluno == null) {
            return ResponseEntity.notFound().build();
        }

        turma.getAlunos().remove(aluno);
        return ResponseEntity.ok(turmaRepository.save(turma));
    }

    // Busca turmas por dia da semana
    @GetMapping("/dia/{diaSemana}")
    public List<Turma> buscarTurmasPorDia(@PathVariable String diaSemana) {
        return turmaRepository.findByDiaSemana(diaSemana);
    }

    // Atualiza turma
    @PutMapping("/{id}")
    public ResponseEntity<Turma> atualizarTurma(@PathVariable Long id, @RequestBody Turma turmaAtualizada) {
        if (!turmaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        turmaAtualizada.setId(id);
        return ResponseEntity.ok(turmaRepository.save(turmaAtualizada));
    }

    // Deleta turma
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTurma(@PathVariable Long id) {
        if (!turmaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        turmaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 