package com.gmarussi.backend.dto;

import java.time.LocalDateTime;

public record TarefaResponse(
        Long id,
        String titulo,
        String descricao,
        String status,
        LocalDateTime dataCriacao,
        Long idProjeto,
        String nomeProjeto
) {}
