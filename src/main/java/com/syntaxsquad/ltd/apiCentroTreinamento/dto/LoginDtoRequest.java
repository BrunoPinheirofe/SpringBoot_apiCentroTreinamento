package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class LoginDtoRequest {

    private String email;
    private String senha;

}
