package com.syntaxsquad.ltd.apiCentroTreinamento.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;

@Component
public class StringToMatriculaConverter implements Converter<String, Matricula> {
    @Override
    public Matricula convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return new Matricula(source);
    }
}