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
@OpenAPIDefinition(
		info  = @Info(
				title = "API Centro de Treinamento",
				version = "1.0.0",
				description = "API para gerenciamento de alunos, instrutores, turmas e treinos.",
				contact = @Contact(
						name = "Equipe de Desenvolvimento",
						email = ""
		)
))
public class ApiCentroTreinamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCentroTreinamentoApplication.class, args);
		System.out.println("\n");
		System.out.println("\n");
		System.out.println(" GGGGG    AAAAA   BBBBB   RRRRR    III  EEEEE   L      ");
        System.out.println("G         A   A   B    B  R   R     I   E       L      ");
        System.out.println("G  GG     AAAAA   BBBBB   RRRRR     I   EEEE    L      ");
        System.out.println("G    G    A   A   B    B  R  R      I   E       L      ");
        System.out.println(" GGGG     A   A   BBBBB   R   R    III  EEEEE   LLLLL  ");
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("BBBBB    RRRRR     U   U    N   N     OOOOO  ");
        System.out.println("B    B   R   R     U   U    NN  N    O     O ");
        System.out.println("BBBBB    RRRRR     U   U    N N N    O     O ");
        System.out.println("B    B   R  R      U   U    N  NN    O     O ");
        System.out.println("BBBBB    R   R      UUU     N   N     OOOOO  ");
		System.out.println("\n");
		System.out.println("\n");
		
	}


}
