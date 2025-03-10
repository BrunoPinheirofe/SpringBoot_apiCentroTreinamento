package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InstrutorDtoRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Especialidade é obrigatória")
    @Size(min = 2, max = 100, message = "Especialidade deve ter entre 2 e 100 caracteres")
    private String especialidade;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

}
