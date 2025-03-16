package com.syntaxsquad.ltd.apiCentroTreinamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import com.syntaxsquad.ltd.apiCentroTreinamento.services.DecodificarQrCode;

import jakarta.persistence.Cacheable;




@SpringBootApplication
@EnableCaching
@EnableSpringDataWebSupport

public class ApiCentroTreinamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCentroTreinamentoApplication.class, args);
		DecodificarQrCode qrcode = new DecodificarQrCode();
		qrcode.qrcode("", "qrcode.png");
	}


}
