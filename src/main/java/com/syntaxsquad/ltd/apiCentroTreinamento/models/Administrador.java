package com.syntaxsquad.ltd.apiCentroTreinamento.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Random;

@Data
@Entity
@Table(name = "administradores")
public class Administrador {
    @Id
    @Column(unique = true, nullable = false)
    private String matricula;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String sobrenome;

    @Column(nullable = false)
    private String email; 

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private LocalDate dataCadastro = LocalDate.now();

    @Column(nullable = false)
    private int idade;

    @OneToOne
    @JoinColumn(name = "email_user", referencedColumnName = "email", unique = true)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.matricula = generateMatricula();
    }

    private String generateMatricula() {
        LocalDate date = LocalDate.now();
        int randomDigits = new Random().nextInt(9000) + 1000; // generates a 4-digit number
        return date.toString().replace("-", "") + randomDigits;
    }

    public Administrador(String nome, String sobrenome, String email, String telefone, LocalDate dataNascimento,
         int idade, User user) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = LocalDate.now();
        this.idade = idade;
        this.user = user;
    }
    public Administrador() {
    }
    
}