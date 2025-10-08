package com.gmarussi.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TarefaRequest(
    @NotNull(message = "O ID do projeto é obrigatório")
    Long idProjeto,
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
    String titulo,
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    String descricao,
    @Size(max = 30, message = "Status deve ter no máximo 30 caracteres")
    String status
) {}

