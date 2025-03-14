package com.syntaxsquad.ltd.apiCentroTreinamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import jakarta.persistence.Cacheable;



@SpringBootApplication
@EnableCaching
public class ApiCentroTreinamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCentroTreinamentoApplication.class, args);
	}


}
