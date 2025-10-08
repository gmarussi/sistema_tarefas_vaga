package com.gmarussi.backend.service;

import com.gmarussi.backend.dto.TarefaRequest;
import com.gmarussi.backend.dto.TarefaResponse;
import com.gmarussi.backend.model.Projeto;
import com.gmarussi.backend.model.Tarefa;
import com.gmarussi.backend.repository.ProjetoRepository;
import com.gmarussi.backend.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private TarefaService tarefaService;

    private Projeto projeto;

    @BeforeEach
    void setUp() {
        projeto = Projeto.builder().id(1L).nome("Projeto A").build();
    }

    @Test
    @DisplayName("salvar: deve persistir tarefa e retornar response mapeado")
    void salvar_devePersistirERetornarResponse() {
        var request = new TarefaRequest(1L, "Titulo", "Desc", "ABERTA");
        var instante = LocalDateTime.now();
        var entidadeSalva = Tarefa.builder()
                .id(10L)
                .titulo("Titulo")
                .descricao("Desc")
                .status("ABERTA")
                .dataCriacao(instante)
                .projeto(projeto)
                .build();

        given(projetoRepository.findById(1L)).willReturn(Optional.of(projeto));
        given(tarefaRepository.save(any(Tarefa.class))).willReturn(entidadeSalva);

        TarefaResponse resp = tarefaService.salvar(request);

        assertThat(resp.id()).isEqualTo(10L);
        assertThat(resp.titulo()).isEqualTo("Titulo");
        assertThat(resp.descricao()).isEqualTo("Desc");
        assertThat(resp.status()).isEqualTo("ABERTA");
        assertThat(resp.dataCriacao()).isEqualTo(instante);
        assertThat(resp.idProjeto()).isEqualTo(1L);
        assertThat(resp.nomeProjeto()).isEqualTo("Projeto A");

        verify(projetoRepository).findById(1L);
        verify(tarefaRepository).save(any(Tarefa.class));
    }

    @Test
    @DisplayName("salvar: deve lançar EntityNotFoundException quando projeto não existe")
    void salvar_deveLancarQuandoProjetoNaoExiste() {
        var request = new TarefaRequest(99L, "Titulo", "Desc", "ABERTA");
        given(projetoRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> tarefaService.salvar(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Projeto");
        verify(tarefaRepository, never()).save(any());
    }

    @Test
    @DisplayName("excluir: deve excluir quando existir")
    void excluir_deveExcluirQuandoExiste() {
        given(tarefaRepository.existsById(5L)).willReturn(true);

        tarefaService.excluir(5L);

        verify(tarefaRepository).deleteById(5L);
    }

    @Test
    @DisplayName("excluir: deve lançar EntityNotFoundException quando não existir")
    void excluir_deveLancarQuandoNaoExiste() {
        given(tarefaRepository.existsById(5L)).willReturn(false);

        assertThatThrownBy(() -> tarefaService.excluir(5L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Tarefa");

        verify(tarefaRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("listar: sem filtro chama findAll e mapeia página")
    void listar_semFiltro_findAll() {
        var tarefa = Tarefa.builder()
                .id(1L).titulo("T1").status("ABERTA").projeto(projeto)
                .build();
        Page<Tarefa> page = new PageImpl<>(List.of(tarefa));
        given(tarefaRepository.findAll(any(Pageable.class))).willReturn(page);

        var result = tarefaService.listar(null, 0, 10, "dataCriacao", "desc");

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(1L);
        verify(tarefaRepository).findAll(any(Pageable.class));
        verify(tarefaRepository, never()).findByProjeto_Id(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("listar: com idProjeto chama findByProjeto_Id")
    void listar_comFiltroPorProjeto() {
        var tarefa = Tarefa.builder().id(2L).titulo("T2").projeto(projeto).build();
        Page<Tarefa> page = new PageImpl<>(List.of(tarefa));
        given(tarefaRepository.findByProjeto_Id(eq(1L), any(Pageable.class))).willReturn(page);

        var result = tarefaService.listar(1L, 1, 5, "titulo", "asc");

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(tarefaRepository).findByProjeto_Id(eq(1L), any(Pageable.class));
    }

    @Test
    @DisplayName("listar: sort inválido deve cair no padrão 'dataCriacao' desc")
    void listar_sortInvalido_usaPadrao() {
        Page<Tarefa> page = new PageImpl<>(List.of());
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        given(tarefaRepository.findAll(any(Pageable.class))).willReturn(page);

        tarefaService.listar(null, 0, 20, "foo", "desc");

        verify(tarefaRepository).findAll(pageableCaptor.capture());
        var pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageSize()).isEqualTo(20);
        assertThat(pageable.getSort().get().findFirst().map(s -> s.getProperty()).orElse(""))
                .isEqualTo("dataCriacao");
    }
}

