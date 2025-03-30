package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import com.syntaxsquad.ltd.apiCentroTreinamento.dto.PlanoDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Plano;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.PlanoRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/planos")
public class PlanoController {

    @Autowired
    private PlanoRepository planoRepository;

    // Lista todos os planos
    @GetMapping
    public List<Plano> listarPlanos() {
        return planoRepository.findAll();
    }

    // Busca plano por ID
    @GetMapping("/{id}")
    public ResponseEntity<Plano> buscarPlanoPorId(@PathVariable Long id) {
        return planoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cria novo plano
    @PostMapping
    public ResponseEntity<Plano> criarPlano(@Valid @RequestBody PlanoDtoRequest planoDtoRequest) {
        Plano plano = new Plano();
        plano.setNome(planoDtoRequest.getNome());
        plano.setValor(planoDtoRequest.getValor());
        plano.setDuracao(planoDtoRequest.getDuracao());
        plano.setTipoPlano(planoDtoRequest.getTipoPlano());
        plano.setDescricao(planoDtoRequest.getDescricao());
        
        // Salva o novo plano
        Plano planoSalvo = planoRepository.save(plano);
        
        // Retorna resposta com o plano criado
        return ResponseEntity.ok(planoSalvo);
    }
    
    // Atualiza plano
    @PutMapping("/{id}")
    public ResponseEntity<Plano> atualizarPlano(@PathVariable Long id, @RequestBody Plano planoAtualizado) {
        if (!planoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        planoAtualizado.setId(id);
        return ResponseEntity.ok(planoRepository.save(planoAtualizado));
    }

    // Busca planos por faixa de valor
    @GetMapping("/valor")
    public List<Plano> buscarPlanosPorValor(
            @RequestParam BigDecimal valorMinimo,
            @RequestParam BigDecimal valorMaximo) {
        return planoRepository.findByValorBetween(valorMinimo, valorMaximo);
    }

    // Busca planos por duração
    @GetMapping("/duracao/{duracao}")
    public List<Plano> buscarPlanosPorDuracao(@PathVariable Integer duracao) {
        return planoRepository.findByDuracao(duracao);
    }

    // Deleta plano
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPlano(@PathVariable Long id) {
        if (!planoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        planoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 