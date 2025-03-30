package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercadopago.net.HttpStatus;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.AlunoPagamentoResponse;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.ErrorDto;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.PagamentoDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.PagamentoDtoResponse;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.PagamentoResponse;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.PagamentoStatusResponse;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Pagamento;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Plano;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.PagamentoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.PlanoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;
import com.syntaxsquad.ltd.apiCentroTreinamento.services.DecodificarQrCode;
import com.syntaxsquad.ltd.apiCentroTreinamento.services.MercadoPagoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mercado-pago")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private PlanoRepository planoRepository;

    @PostMapping
    public ResponseEntity<?> processarPagamento(@Valid @RequestBody PagamentoDtoRequest pagamentoDtoRequest) {
        // Busca o aluno e o plano pelo ID
        Aluno aluno = alunoRepository.findById(pagamentoDtoRequest.getMatriculaAluno()).orElse(null);
        Plano plano = planoRepository.findById(pagamentoDtoRequest.getPlanoId()).orElse(null);
    
        if (aluno == null || plano == null) {
            return ResponseEntity.notFound().build();
        }
    
        // Cria um novo pagamento
        Pagamento pagamento = new Pagamento();
        pagamento.setAluno(aluno);
        pagamento.setPlano(plano);
        pagamento.setDataPagamento(pagamentoDtoRequest.getDataPagamento());
        pagamento.setValorPago(plano.getValor());
        pagamento.setVencimento(pagamentoDtoRequest.getDataVencimento());
        pagamento.setFormaPagamento(pagamentoDtoRequest.getFormaPagamento());
        pagamento.setStatus("pending");
    
        // Processa o pagamento no Mercado Pago
        // Isso retorna um objeto de pagamento com o PaymentId
        PagamentoResponse pagamentoProcessado = mercadoPagoService.processarPagamento(pagamento);
    
        // Após processar o pagamento, salva os dados no banco
        pagamento.setId(pagamentoProcessado.getPaymentId());  // Atualiza o ID com o valor retornado do MercadoPago
        pagamentoRepository.save(pagamento);  // Salva o pagamento no banco de dados
    
        // Retorna a resposta com o pagamento processado
        return ResponseEntity.ok(pagamentoProcessado);
    }
    
    @GetMapping("/status/{id}")
    public ResponseEntity<?> statusPagamento(@PathVariable Long id) {
    
        // Busca o pagamento pelo ID
        Pagamento pagamento = pagamentoRepository.findById(id.toString()).orElse(null);
        
        if (pagamento == null) {
            return ResponseEntity.notFound().build();  // Retorna 404 se o pagamento não for encontrado
        }
    
        // Obtém o status do pagamento através do MercadoPagoService
        PagamentoStatusResponse status = mercadoPagoService.statusPagamento(id); // Pegando o status do pagamento
    
        // Se o status do pagamento for diferente do status atual no banco, atualiza
        if (!status.equals(pagamento.getStatus())) {
            pagamento.setStatus(status.getStatus());  // Atualiza o status do pagamento
            pagamentoRepository.save(pagamento);  // Salva a alteração no banco de dados
        }
    
        // Retorna o status do pagamento
        return ResponseEntity.ok(status);
    }
    @GetMapping("/all")
public ResponseEntity<List<PagamentoDtoResponse>> getAllPagamentos() {
    List<Pagamento> pagamentos = pagamentoRepository.findAll();
    List<PagamentoDtoResponse> pagamentosDtoResponse = pagamentos.stream()
            .map(p -> new PagamentoDtoResponse(p))  // Mapear para o DTO
            .collect(Collectors.toList());
    return ResponseEntity.ok(pagamentosDtoResponse);
}


@GetMapping("/atualizar-status")
public ResponseEntity<?> atualizarStatusPagamentos() {
    // Busca todos os pagamentos
    List<Pagamento> pagamentos = pagamentoRepository.findAll();

    // Verifica se existem pagamentos
    if (pagamentos.isEmpty()) {
        return ResponseEntity.noContent().build();  // Retorna 204 caso não haja pagamentos
    }

    // Atualiza o status de cada pagamento
    for (Pagamento pagamento : pagamentos) {
        // Aqui chamamos o serviço MercadoPagoService para pegar o status atualizado do pagamento
        PagamentoStatusResponse status = mercadoPagoService.statusPagamento(Long.valueOf(pagamento.getId()));

        // Verifica se o status atual do pagamento no banco é diferente do status retornado
        if (!status.getStatus().equals(pagamento.getStatus())) {
            pagamento.setStatus(status.getStatus());  // Atualiza o status no objeto Pagamento
            pagamentoRepository.save(pagamento);  // Salva o pagamento atualizado no banco
        }
    }

    // Retorna uma resposta indicando que a operação foi realizada com sucesso
    return ResponseEntity.ok("Status de pagamentos atualizados com sucesso");
}

@GetMapping("/alunos/{matricula}")
public ResponseEntity<?> buscarAlunoPorMatricula(@PathVariable Matricula matricula) {
    try {
        // Busca o aluno pela matrícula
        Aluno aluno = alunoRepository.findByMatricula(matricula).orElse(null);

        // Verifica se o aluno existe
        if (aluno == null) {
            return ResponseEntity.notFound().build();  // Retorna "Not Found" se o aluno não for encontrado
        }

        // Buscar os pagamentos desse aluno com base na matrícula
        List<Pagamento> pagamentos = pagamentoRepository.findByAlunoMatricula(matricula);

        // Criar uma resposta com o DTO que contém o aluno e os pagamentos
        AlunoPagamentoResponse resposta = new AlunoPagamentoResponse(aluno, pagamentos);

        // Retorna o aluno e os pagamentos ou qualquer outro dado relacionado
        return ResponseEntity.ok(resposta);  // Retornando a resposta

    } catch (Exception e) {
        // Captura qualquer exceção e retorna o erro com a mensagem
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor: " + e.getMessage());
    }
}


    
}
