package com.syntaxsquad.ltd.apiCentroTreinamento.serializers;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonValue;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Matricula implements Serializable {
    
    @Column(nullable = false, unique = true)
    private String codigo;
    
    public static Matricula gerarMatricula() {
        Matricula matricula = new Matricula();
        LocalDate date = LocalDate.now();
        int randomDigits = new Random().nextInt(9000) + 1000; // generates a 4-digit number
        matricula.setCodigo(date.toString().replace("-", "") + randomDigits);
        return matricula;
    }

    public static Matricula fromString(String value){
        if (value== null)return null;
        return new Matricula(value);
    }
    
    @JsonValue
    @Override
    public String toString() {
        return codigo;
    }
}
