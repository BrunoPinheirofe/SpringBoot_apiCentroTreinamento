package com.syntaxsquad.ltd.apiCentroTreinamento.services;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Base64;
import java.io.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.resources.payment.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.syntaxsquad.ltd.apiCentroTreinamento.dto.PagamentoResponse;
import com.syntaxsquad.ltd.apiCentroTreinamento.dto.PagamentoStatusResponse;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Pagamento;

import io.jsonwebtoken.io.IOException;

// Importações para ZXing
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class MercadoPagoService {

    @Value("${mercado.pago.access.token}")
    private String ACCESS_TOKEN;

    private static final Logger logger = LoggerFactory.getLogger(MercadoPagoService.class);
    private static final int QR_CODE_WIDTH = 200;
    private static final int QR_CODE_HEIGHT = 200;

    // Método para criar a requisição de pagamento com Pix
    private PaymentCreateRequest criarPagamentoComPix(Pagamento pagamento) {
        return PaymentCreateRequest.builder()
                .transactionAmount(pagamento.getPlano().getValor()) // Valor da transação
                .description("Pagamento do Plano: " + pagamento.getPlano().getDescricao()) // Descrição do plano
                .paymentMethodId("pix") // Método de pagamento Pix
                .dateOfExpiration(OffsetDateTime.now().plusDays(1)) // Data de expiração do pagamento (24 horas)
                .payer(
                        PaymentPayerRequest.builder()
                                .email(pagamento.getAluno().getEmail()) // E-mail do comprador
                                .firstName(pagamento.getAluno().getNome()) // Nome do comprador
                                .lastName(pagamento.getAluno().getSobrenome()) // Sobrenome do comprador
                                .build())
                .build();
    }

    // Método para processar o pagamento
    public PagamentoResponse processarPagamento(Pagamento pagamento) {
        if (pagamento == null || pagamento.getPlano() == null || pagamento.getAluno() == null) {
            logger.error("Dados do pagamento estão incompletos.");
            return new PagamentoResponse("Dados do pagamento estão incompletos.", null, null);
        }

        // Configurar o Mercado Pago
        MercadoPagoConfig.setAccessToken(ACCESS_TOKEN);

        // Gerar chave única de idempotência para evitar duplicidade de pagamentos
        String idempotencyKey = UUID.randomUUID().toString();
        logger.info("Chave Idempotente gerada: {}", idempotencyKey);

        // Criar headers customizados com o idempotency key
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("x-idempotency-key", idempotencyKey);

        // Criar opções da requisição
        MPRequestOptions requestOptions = MPRequestOptions.builder()
                .customHeaders(customHeaders)
                .build();

        // Criar cliente de pagamento
        PaymentClient client = new PaymentClient();

        // Criar requisição de pagamento
        PaymentCreateRequest paymentCreateRequest = criarPagamentoComPix(pagamento);

        // Log de requisição de pagamento
        logger.info("Requisição de pagamento: {}", paymentCreateRequest);

        try {
            // Enviar pagamento
            Payment response = client.create(paymentCreateRequest, requestOptions);

            // Verificar a resposta
            if (response != null && response.getId() != null) {
                logger.info("Pagamento processado com sucesso! ID do pagamento: {}", response.getId());

                // Verificar e logar detalhes de interação (QR Code, ticket URL)
                if (response.getTransactionDetails() != null 
                    && response.getPointOfInteraction() != null 
                    && response.getPointOfInteraction().getTransactionData() != null) {
                    String ticketUrl = response.getCallbackUrl(); 
                    String qrCode = response.getPointOfInteraction().getTransactionData().getQrCode();

                    if (ticketUrl == null) {
                        logger.warn("Link do pagamento não disponível.");
                    }

                    // Convertendo o PIX code para QR Code em base64
                    String qrCodeBase64 = convertQrCodeToBase64(qrCode);

                    if (qrCodeBase64 == null) {
                        return new PagamentoResponse("Erro ao gerar o QR Code.", null, null);
                    }

                    // Log do ticket URL e QR Code
                    logger.info("Link do pagamento: {}", ticketUrl);
                    logger.info("QR Code para pagamento (base64): {}", qrCodeBase64);

                    // Definindo esses valores na resposta
                    PagamentoResponse pagamentoResponse = new PagamentoResponse();
                    pagamentoResponse.setPaymentId(response.getId().toString());
                    pagamentoResponse.setTicketUrl(ticketUrl);
                    pagamentoResponse.setQrCode(qrCode);
                    pagamentoResponse.setQrCode64(qrCodeBase64);

                    return pagamentoResponse; // Retorna o objeto com os dados do pagamento
                }

            } else {
                logger.error("Erro ao processar pagamento. Resposta da API vazia.");
                return new PagamentoResponse("Erro ao processar pagamento. Resposta da API vazia.", null, null);
            }
        } catch (Exception e) {
            // Log de erro usando logger
            logger.error("Erro ao processar pagamento: {}", e.getMessage(), e);
            return new PagamentoResponse("Erro ao processar pagamento: " + e.getMessage(), null, null);
        }

        return new PagamentoResponse("Erro desconhecido ao processar o pagamento.", null, null);
    }

    // Método para converter o PIX code em QR Code e retornar como base64
  
    
        public String convertQrCodeToBase64(String qrCodeText) throws WriterException, IOException, java.io.IOException {
            // Cria o QR Code Writer
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
    
            // Gera a matriz do QR Code
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 200, 200);
    
            // Converte a matriz em uma imagem PNG
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
    
            // Converte a imagem para Base64
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    

    // Método para consultar o status do pagamento
    public PagamentoStatusResponse statusPagamento(Long id) {
        MercadoPagoConfig.setAccessToken(ACCESS_TOKEN);

        PaymentClient client = new PaymentClient();
        
        try {
            // Obter o pagamento pelo ID
            Payment payment = client.get(id);

            if (payment != null) {
                PagamentoStatusResponse statusResponse = new PagamentoStatusResponse();
                statusResponse.setPaymentId(payment.getId().toString());
                statusResponse.setStatus(payment.getStatus());
                return statusResponse;
            } else {
                logger.error("Pagamento não encontrado.");
                return new PagamentoStatusResponse("Pagamento não encontrado", null);
            }
        } catch (Exception e) {
            logger.error("Erro ao processar status do pagamento: {}", e.getMessage(), e);
            return new PagamentoStatusResponse("Erro ao consultar status", null);
        }
    }
    
}