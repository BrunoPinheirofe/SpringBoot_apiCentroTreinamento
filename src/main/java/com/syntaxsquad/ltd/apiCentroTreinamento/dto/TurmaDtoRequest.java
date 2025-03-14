package com.syntaxsquad.ltd.apiCentroTreinamento.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
@Getter
@Setter
public class TurmaDtoRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Horário é obrigatório")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Horário deve estar no formato HH:mm")
    private String horario;

    @NotBlank(message = "Dia da semana é obrigatório")
    @Pattern(regexp = "^(Segunda|Terça|Quarta|Quinta|Sexta|Sábado|Domingo)$", message = "Dia da semana inválido")
    private String diaSemana;

    @NotNull(message = "Instrutor é obrigatório")
    private Long instrutorId;

    private List<String>sIds; // Lista de alunos que podem ser adicionados à turma
}
