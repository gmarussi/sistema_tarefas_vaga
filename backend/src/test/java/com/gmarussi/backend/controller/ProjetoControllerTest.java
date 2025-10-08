package com.gmarussi.backend.controller;

import com.gmarussi.backend.model.Projeto;
import com.gmarussi.backend.service.ProjetoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjetoController.class)
class ProjetoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjetoService projetoService;

    @Test
    @DisplayName("GET /api/v1/projetos: retorna lista de projetos")
    void listar_deveRetornarLista() throws Exception {
        var lista = List.of(
                Projeto.builder().id(1L).nome("P1").build(),
                Projeto.builder().id(2L).nome("P2").build()
        );
        given(projetoService.listarTodos()).willReturn(lista);

        mockMvc.perform(get("/api/v1/projetos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("P1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("P2"));
    }
}

