package com.syntaxsquad.ltd.apiCentroTreinamento.services;

import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class DecodificarQrCode {

    /**
     * Decodifica um QR Code em Base64 e salva como uma imagem.
     *
     * @param qrCodeBase64 O QR Code codificado em Base64.
     * @param filePath     O caminho onde a imagem será salva.
     * @return Uma mensagem de sucesso ou erro.
     */
    public String qrcode(String qrCodeBase64, String filePath) {
        try {
            // Decodificar o Base64 para bytes
            byte[] imageBytes = Base64.getDecoder().decode(qrCodeBase64);

            // Salvar a imagem em um arquivo
            Files.write(Paths.get(filePath), imageBytes);

            return "QR Code salvo com sucesso em: " + filePath;
        } catch (IOException e) {
            return "Erro ao salvar o QR Code: " + e.getMessage();
        }
    }
}