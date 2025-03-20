package com.syntaxsquad.ltd.apiCentroTreinamento.dto;
import com.syntaxsquad.ltd.apiCentroTreinamento.serializers.Matricula;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDtoRequest {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;
    @NotBlank(message = "Senha é obrigatória")
    private String password;
    @NotBlank(message = "Matricula é obrigatória")
    private Matricula matricula;


}
