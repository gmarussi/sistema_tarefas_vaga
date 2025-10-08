package com.gmarussi.backend.service;

import com.gmarussi.backend.model.Projeto;
import com.gmarussi.backend.repository.ProjetoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProjetoServiceTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private ProjetoService projetoService;

    @Test
    @DisplayName("listarTodos: retorna lista do repositório")
    void listarTodos_retornaLista() {
        var lista = List.of(
                Projeto.builder().id(1L).nome("P1").build(),
                Projeto.builder().id(2L).nome("P2").build()
        );
        given(projetoRepository.findAll()).willReturn(lista);

        var result = projetoService.listarTodos();
        assertThat(result).hasSize(2).extracting(Projeto::getNome).containsExactly("P1", "P2");
    }
}

