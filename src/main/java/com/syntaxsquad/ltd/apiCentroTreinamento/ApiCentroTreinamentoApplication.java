package com.syntaxsquad.ltd.apiCentroTreinamento;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableSpringDataWebSupport
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "API Centro de Treinamento", version = "1.0.0", description = "API para gerenciamento de alunos, instrutores, turmas e treinos.", contact = @Contact(name = "Equipe de Desenvolvimento", email = "")))
public class ApiCentroTreinamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCentroTreinamentoApplication.class, args);

		
	}

	
}
