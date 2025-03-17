package com.syntaxsquad.ltd.apiCentroTreinamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;





@SpringBootApplication
@EnableCaching
@EnableSpringDataWebSupport
@EnableScheduling

public class ApiCentroTreinamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCentroTreinamentoApplication.class, args);
		
	}


}
