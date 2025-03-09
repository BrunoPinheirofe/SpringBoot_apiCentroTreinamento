package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

import com.syntaxsquad.ltd.apiCentroTreinamento.enums.SexoEnum;

@Data
public class AlunoDtoRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Sobrenome é obrigatório")
    @Size(min = 2, max = 100, message = "Sobrenome deve ter entre 2 e 100 caracteres")
    private String sobrenome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "^\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}$", message = "Telefone deve estar no formato (99) 99999-9999")
    private String telefone;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @Min(value = 0, message = "Idade não pode ser negativa")
    @Max(value = 120, message = "Idade não pode ser maior que 120")
    private int idade;

    @NotNull(message = "Gênero é obrigatório de acordo com as roles (MASCULINO, FEMININO, OUTROS)")
    private SexoEnum genero;

    @Size(max = 500, message = "Observação não pode ter mais que 500 caracteres")
    private String observacao;
}