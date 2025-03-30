package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoResponse {

    private String paymentId;
    private String ticketUrl;
    private String qrCode;
    private String qrCode64;

    // // Getters and setters

    // public PagamentoResponse() {
    //     //TODO Auto-generated constructor stub
    // }

    // public PagamentoResponse(String string, Object object, Object object2) {
    //     //TODO Auto-generated constructor stub
    // }

    // public String getPaymentId() {
    //     return paymentId;
    // }

    // public void setPaymentId(String paymentId) {
    //     this.paymentId = paymentId;
    // }

    // public String getTicketUrl() {
    //     return ticketUrl;
    // }

    // public void setTicketUrl(String ticketUrl) {
    //     this.ticketUrl = ticketUrl;
    // }

    // public String getQrCode() {
    //     return qrCode;
    // }

    // public void setQrCode(String qrCode) {
    //     this.qrCode = qrCode;
    // }
    // public String getQrCode64() {
    //     return qrCode64;
    // }

    // public void setQrCode64(String qrCode64) {
    //     this.qrCode64 = qrCode64;
    // }
}
/*************  ✨ Codeium Command ⭐  *************/
/******  3b1f44c4-07ee-4a98-a15f-12df892db8ad  *******/    /**

     * Description of the payment (e.g. "Course subscription")

     * @return the description of the payment

     */
