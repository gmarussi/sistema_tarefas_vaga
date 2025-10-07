package com.gmarussi.backend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo padronizado de resposta de erro REST.
 * Ideal para logs, monitoramento e comunicação com o frontend.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensagem,
        String path,
        List<CampoErro> erros,
        String detalhe
) {

    public static ErrorResponse of(HttpStatus status, String mensagem, String path) {
        return new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensagem, path, null, null);
    }

    public static ErrorResponse of(HttpStatus status, String mensagem, String path, List<CampoErro> erros) {
        return new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensagem, path, erros, null);
    }

    public static ErrorResponse of(HttpStatus status, String mensagem, String path, String detalhe) {
        return new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensagem, path, null, detalhe);
    }

    /**
     * Representa erro de campo individual, útil para validações de formulário.
     */
    public record CampoErro(String campo, String mensagem) {}
}
