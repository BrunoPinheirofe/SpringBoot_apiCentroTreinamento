package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import com.syntaxsquad.ltd.apiCentroTreinamento.enums.UserRole;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter

public class UsersDtoRequest {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;
    @NotBlank(message = "Senha é obrigatória")
    private String password;
    
    @NotBlank(message = "Role é obrigatório deve ser válido (ADMIN,TREINADOR,ALUNO)")
    @Enumerated(EnumType.STRING)
    private UserRole role;


}
