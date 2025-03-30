package com.syntaxsquad.ltd.apiCentroTreinamento;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.syntaxsquad.ltd.apiCentroTreinamento.enums.UserRole;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.Administrador;
import com.syntaxsquad.ltd.apiCentroTreinamento.models.User;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.AdministradorRepository;
import com.syntaxsquad.ltd.apiCentroTreinamento.repositories.UserRepository;


@Component
public class Setup implements ApplicationRunner{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private static final String DEFAULT_ADMIN_EMAIL = "admin@gmail.com";
    private static final String DEFAULT_ADMIN_NAME = "ADMIN";
    private static final String DEFAULT_ADMIN_PASSWORD = "adminpassword";


    @Override
    public void run(ApplicationArguments args){
        System.out.println("Iniciando configuração...");
        createAdminDefaut();
        System.out.println("usuario admin padrao criado...");
    }

    private void createAdminDefaut(){
        boolean adminExists = userRepository.findAll().stream()
        .anyMatch(user->UserRole.ADMIN.equals(user.getRole()));
        if(!adminExists){
            User defaultAdminUser = new User();
            defaultAdminUser.setEmail(DEFAULT_ADMIN_EMAIL);
            defaultAdminUser.setRole(UserRole.ADMIN);
            defaultAdminUser.setPassword(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD));
            Administrador defaultAdmin = new Administrador();
            defaultAdmin.setDataNascimento(LocalDate.parse("1999-01-01"));
            defaultAdmin.setEmail(DEFAULT_ADMIN_EMAIL);
            defaultAdmin.setNome(DEFAULT_ADMIN_NAME);
            defaultAdmin.setSobrenome("teste");
            defaultAdmin.setTelefone("999999999");
            userRepository.save(defaultAdminUser);
            defaultAdmin.setUser(defaultAdminUser);
            administradorRepository.save(defaultAdmin);

        }
    }



}
