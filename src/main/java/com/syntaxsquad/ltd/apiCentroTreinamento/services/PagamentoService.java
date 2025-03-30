package com.syntaxsquad.ltd.apiCentroTreinamento.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Plano;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Pagamento;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Aluno;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.PagamentoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AlunoRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.PagamentoStatusResponse;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AlunoRepository alunoRepository;
    private final MercadoPagoService mercadoPagoService;

    public PagamentoService(PagamentoRepository pagamentoRepository, AlunoRepository alunoRepository, MercadoPagoService mercadoPagoService) {
        this.pagamentoRepository = pagamentoRepository;
        this.alunoRepository = alunoRepository;
        this.mercadoPagoService = mercadoPagoService;
    }

    // Método agendado para rodar ao final de cada dia (meia-noite)
    @Scheduled(cron = "0 0 0 * * *")  // Esse cron dispara à meia-noite todos os dias
    public void verificarEGerarPagamentos() {
        // Buscar todos os alunos com pagamentos existentes
        List<Aluno> alunos = alunoRepository.findAll();

        for (Aluno aluno : alunos) {
            // Verificar se há algum pagamento vencido
            Pagamento pagamento = pagamentoRepository.findByAluno(aluno);

            if (pagamento != null && pagamento.getVencimento().isBefore(LocalDate.now())) {  // Verifica se o pagamento venceu
                // O pagamento venceu, então cria um novo pagamento
                gerarNovoPagamento(aluno, pagamento.getPlano());
            }
        }
    }

    // Método para gerar um novo pagamento para o aluno
    private void gerarNovoPagamento(Aluno aluno, Plano plano) {
        // Criação do novo pagamento
        Pagamento novoPagamento = new Pagamento();
        novoPagamento.setAluno(aluno);
        novoPagamento.setPlano(plano);

        // Calcular o valor do pagamento com base no plano
        BigDecimal valorPagamento = calcularValorPagamento(plano);
        novoPagamento.setValorPago(valorPagamento);

        // Definir o vencimento conforme o tipo de plano
        novoPagamento.setVencimento(calcularDataVencimento(plano));

        // Definir status do pagamento como "pending"
        novoPagamento.setStatus("pending");

        // Salvar o novo pagamento no banco de dados
        pagamentoRepository.save(novoPagamento);

        // Atualizar status do pagamento com o Mercado Pago
        PagamentoStatusResponse status = mercadoPagoService.statusPagamento(Long.valueOf(novoPagamento.getId()));
        if (status != null && !status.getStatus().equals(novoPagamento.getStatus())) {
            novoPagamento.setStatus(status.getStatus());
            pagamentoRepository.save(novoPagamento);  // Atualiza o status no banco
        }
    }

    // Método para calcular o valor do pagamento com base no plano
    private BigDecimal calcularValorPagamento(Plano plano) {
        if ("SEMANAL".equals(plano.getTipoPlano())) {
            return plano.getValor();  // Pagamento semanal
        } else if ("MENSAL".equals(plano.getTipoPlano())) {
            return plano.getValor().multiply(BigDecimal.valueOf(4));  // Supondo que o plano mensal é 4 vezes o valor semanal
        } else {
            return plano.getValor();  // Valor padrão, se o tipo for outro
        }
    }

    // Método para calcular a data de vencimento com base no tipo do plano
    private LocalDate calcularDataVencimento(Plano plano) {
        LocalDate hoje = LocalDate.now();
        if ("SEMANAL".equals(plano.getTipoPlano())) {
            return hoje.plusWeeks(1);  // Adiciona 1 semana
        } else if ("MENSAL".equals(plano.getTipoPlano())) {
            return hoje.plusMonths(1);  // Adiciona 1 mês
        } else {
            return hoje.plusMonths(1);  // Padrão: 1 mês
        }
    }
    @Scheduled(fixedRate = 3600000)  // Executar a cada 1 hora (3600000 ms)
    public void atualizarStatusPagamentos() {
        // Busca todos os pagamentos
        List<Pagamento> pagamentos = pagamentoRepository.findAll();

        // Verifica se existem pagamentos
        if (pagamentos.isEmpty()) {
            System.out.println("Não há pagamentos para atualizar.");
            return;  // Se não houver pagamentos, apenas retorna
        }

        // Atualiza o status de cada pagamento
        for (Pagamento pagamento : pagamentos) {
            // Aqui chamamos o serviço MercadoPagoService para pegar o status atualizado do pagamento
            PagamentoStatusResponse status = mercadoPagoService.statusPagamento(Long.valueOf(pagamento.getId()));

            // Verifica se o status atual do pagamento no banco é diferente do status retornado
            if (!status.getStatus().equals(pagamento.getStatus())) {
                pagamento.setStatus(status.getStatus());  // Atualiza o status no objeto Pagamento
                pagamentoRepository.save(pagamento);  // Salva o pagamento atualizado no banco
                System.out.println("Status do pagamento com ID " + pagamento.getId() + " atualizado para: " + status.getStatus());
            }
        }

        // Após a execução do método, uma mensagem de sucesso é exibida no log
        System.out.println("Status dos pagamentos atualizados com sucesso.");
    }
}
