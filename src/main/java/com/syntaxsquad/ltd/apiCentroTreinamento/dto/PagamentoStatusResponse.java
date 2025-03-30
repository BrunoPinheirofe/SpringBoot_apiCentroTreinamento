package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

public class PagamentoStatusResponse {

    private String paymentId;
    private String status;
    private String message;

    public PagamentoStatusResponse() {}

    public PagamentoStatusResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    // Getters and setters

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
