package com.syntaxsquad.ltd.apiCentroTreinamento.services;

import com.syntaxsquad.ltd.apiCentroTreinamento.models.Instrutor;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.InstrutorRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstrutorService {

    @Autowired
    private InstrutorRepository instrutorRepository;

    public Instrutor saveInstrutor(Instrutor instrutor) {
        return instrutorRepository.save(instrutor);
    }

    public Instrutor findByMatricula(Matricula matricula) {
        return instrutorRepository.findById(matricula).orElse(null);
    }
}