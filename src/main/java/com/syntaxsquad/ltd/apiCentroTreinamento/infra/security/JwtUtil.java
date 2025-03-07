package com.syntaxsquad.ltd.apiCentroTreinamento.infra.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_TIME = 86400000; // 1 dia

   public SecretKey getSigningKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);  //  256-bit key
    }
    // Gera o token JWT com o email
    public String generateToken(String userName) {
        return Jwts.builder()
                   .setSubject(userName)
                   .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256))  //  256-bit key
                   .compact();
    }
    // Extrai o email do token JWT
    public String getEmailFromToken(String token) {
        try {
            return Jwts.parser()         // Usa parserBuilder() em vez de parser()
                    .setSigningKey(getSigningKey())  // Define a chave de assinatura
                    .build()                        // Constrói o JwtParser
                    .parseClaimsJws(token)          // Analisa o token JWT
                    .getBody()
                    .getSubject();                  // Retorna o "subject", que é o email
        } catch (JwtException e) {
            // Caso o token seja inválido, retorna null ou trata o erro
            return null;
        }
    }

    // Valida o token JWT
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()              // Usa parserBuilder() em vez de parser()
                    .setSigningKey(getSigningKey())  // Define a chave de assinatura
                    .build()                        // Constrói o JwtParser
                    .parseClaimsJws(token);         // Analisa o token JWT
            return true;
        } catch (JwtException e) {
            // Caso o token seja inválido, retorna false
            return false;
        }
    }
}
