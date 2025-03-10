
package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExercicioDto {
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Min(value = 1, message = "Número de repetições deve ser maior que zero")
    @Max(value = 100, message = "Número de repetições não pode ser maior que 100")
    private Integer repeticoes;

    @Min(value = 1, message = "Número de séries deve ser maior que zero")
    @Max(value = 10, message = "Número de séries não pode ser maior que 10")
    private Integer series;

    @PositiveOrZero(message = "Carga não pode ser negativa")
    private Float carga;

    @NotNull(message = "ID do treino é obrigatório")
    private Long treinoId;


}
