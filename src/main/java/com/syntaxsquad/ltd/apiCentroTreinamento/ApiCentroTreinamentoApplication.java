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
		System.out.println(" GGGGG    AAAAA   BBBBB   RRRRR    III  EEEEE  L      ");
        System.out.println("G         A   A   B    B  R   R     I   E       L      ");
        System.out.println("G  GG     AAAAA   BBBBB   RRRRR     I   EEEE    L      ");
        System.out.println("G   G     A   A   B    B  R  R      I   E       L      ");
        System.out.println(" GGGG     A   A   BBBBB   R   R    III  EEEEE   LLLLL  ");
		
	}


}
