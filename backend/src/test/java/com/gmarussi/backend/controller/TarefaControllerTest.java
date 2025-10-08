package com.gmarussi.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmarussi.backend.dto.TarefaRequest;
import com.gmarussi.backend.dto.TarefaResponse;
import com.gmarussi.backend.service.TarefaService;
import com.gmarussi.backend.exception.GlobalExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TarefaController.class)
@Import(GlobalExceptionHandler.class)
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TarefaService tarefaService;

    @Test
    @DisplayName("POST /api/v1/tarefas: retorna 201 com Location e body")
    void criar_deveRetornar201() throws Exception {
        var instante = LocalDateTime.now();
        var response = new TarefaResponse(99L, "Titulo", "Desc", "ABERTA", instante, 1L, "Projeto A");
        given(tarefaService.salvar(any(TarefaRequest.class))).willReturn(response);

        var request = new TarefaRequest(1L, "Titulo", "Desc", "ABERTA");

        mockMvc.perform(post("/api/v1/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/tarefas/99")))
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.titulo").value("Titulo"))
                .andExpect(jsonPath("$.status").value("ABERTA"));

        ArgumentCaptor<TarefaRequest> captor = ArgumentCaptor.forClass(TarefaRequest.class);
        verify(tarefaService).salvar(captor.capture());
        assertThat(captor.getValue().titulo()).isEqualTo("Titulo");
        assertThat(captor.getValue().idProjeto()).isEqualTo(1L);
    }

    @Test
    @DisplayName("POST /api/v1/tarefas: retorna 400 quando payload inválido (Bean Validation)")
    void criar_deveRetornar400_quandoPayloadInvalido() throws Exception {
        // idProjeto nulo e titulo em branco violam Bean Validation
        var json = "{\n" +
                "  \"idProjeto\": null,\n" +
                "  \"titulo\": \"\",\n" +
                "  \"descricao\": \"texto\",\n" +
                "  \"status\": \"ABERTA\"\n" +
                "}";

        mockMvc.perform(post("/api/v1/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/v1/tarefas"))
                .andExpect(jsonPath("$.erros[*].campo", org.hamcrest.Matchers.hasItems("idProjeto", "titulo")));
    }

    @Test
    @DisplayName("POST /api/v1/tarefas: retorna 404 quando EntityNotFoundException no service")
    void criar_deveRetornar404_quandoProjetoNaoExiste() throws Exception {
        given(tarefaService.salvar(any(TarefaRequest.class)))
                .willThrow(new EntityNotFoundException("Projeto não encontrado"));

        var request = new TarefaRequest(999L, "Titulo", "Desc", "ABERTA");

        mockMvc.perform(post("/api/v1/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem", org.hamcrest.Matchers.containsString("Projeto")))
                .andExpect(jsonPath("$.path").value("/api/v1/tarefas"));
    }

    @Test
    @DisplayName("POST /api/v1/tarefas: retorna 409 quando DataIntegrityViolationException no service")
    void criar_deveRetornar409_quandoViolacaoIntegridade() throws Exception {
        var cause = new RuntimeException("duplicate key value violates unique constraint");
        var dive = new DataIntegrityViolationException("constraint", cause);
        given(tarefaService.salvar(any(TarefaRequest.class))).willThrow(dive);

        var request = new TarefaRequest(1L, "Titulo", "Desc", "ABERTA");

        mockMvc.perform(post("/api/v1/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.detalhe", org.hamcrest.Matchers.containsString("duplicate key")))
                .andExpect(jsonPath("$.path").value("/api/v1/tarefas"));
    }

    @Test
    @DisplayName("POST /api/v1/tarefas: retorna 500 quando erro genérico no service")
    void criar_deveRetornar500_quandoErroGenerico() throws Exception {
        given(tarefaService.salvar(any(TarefaRequest.class))).willThrow(new RuntimeException("boom"));

        var request = new TarefaRequest(1L, "Titulo", "Desc", "ABERTA");

        mockMvc.perform(post("/api/v1/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.mensagem").value("Erro interno no servidor"))
                .andExpect(jsonPath("$.detalhe", org.hamcrest.Matchers.containsString("boom")))
                .andExpect(jsonPath("$.path").value("/api/v1/tarefas"));
    }

    @Test
    @DisplayName("GET /api/v1/tarefas: retorna página de tarefas e delega ao service")
    void listar_deveRetornarPagina() throws Exception {
        Page<TarefaResponse> page = new PageImpl<>(List.of(
                new TarefaResponse(1L, "T1", "D1", "ABERTA", LocalDateTime.now(), 1L, "P1")
        ));
        given(tarefaService.listar(any(), anyInt(), anyInt(), anyString(), anyString())).willReturn(page);

        mockMvc.perform(get("/api/v1/tarefas")
                        .param("page", "1")
                        .param("size", "2")
                        .param("idProjeto", "5")
                        .param("sortBy", "titulo")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].titulo").value("T1"));

        verify(tarefaService).listar(eq(5L), eq(1), eq(2), eq("titulo"), eq("asc"));
    }

    @Test
    @DisplayName("DELETE /api/v1/tarefas/{id}: retorna 204 e delega ao service")
    void excluir_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/v1/tarefas/{id}", 10))
                .andExpect(status().isNoContent());

        verify(tarefaService).excluir(10L);
    }

    @Test
    @DisplayName("DELETE /api/v1/tarefas/{id}: retorna 404 quando tarefa não existe")
    void excluir_deveRetornar404_quandoNaoExiste() throws Exception {
        doThrow(new EntityNotFoundException("Tarefa não encontrada")).when(tarefaService).excluir(123L);

        mockMvc.perform(delete("/api/v1/tarefas/{id}", 123))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem", org.hamcrest.Matchers.containsString("Tarefa")))
                .andExpect(jsonPath("$.path").value("/api/v1/tarefas/123"));
    }
}
