package com.gmarussi.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TarefaRequest(
    @NotNull(message = "O ID do projeto é obrigatório")
    Long idProjeto
    @NotBlank(message = "Título é obrigatório")
    String titulo,
    String descricao,
    String status,
) {}
