package com.syntaxsquad.ltd.apiCentroTreinamento.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mercadopago.net.HttpStatus;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.ErrorDto;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.PagamentoDtoRequest;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.PagamentoStatusResponse;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Pagamento;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Plano;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.PagamentoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.PlanoRepository;
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
        pagamento.getPlano().setValor(plano.getValor());
        pagamento.setVencimento(pagamentoDtoRequest.getDataVencimento());
        pagamento.setFormaPagamento(pagamentoDtoRequest.getFormaPagamento());
        pagamento.setStatus("pending");

        pagamentoRepository.save(pagamento);

        // Processa o pagamento no Mercado Pago

        return ResponseEntity.ok( mercadoPagoService.processarPagamento(pagamento));
    }



 
}
