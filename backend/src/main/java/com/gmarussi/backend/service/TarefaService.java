package com.gmarussi.backend.service;

import com.gmarussi.backend.dto.TarefaRequest;
import com.gmarussi.backend.dto.TarefaResponse;
import com.gmarussi.backend.model.Projeto;
import com.gmarussi.backend.model.Tarefa;
import com.gmarussi.backend.repository.ProjetoRepository;
import com.gmarussi.backend.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final ProjetoRepository projetoRepository;

    @Transactional
    public TarefaResponse salvar(TarefaRequest request) {
        Projeto projeto = projetoRepository.findById(request.idProjeto())
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado"));

        Tarefa tarefa = Tarefa.builder()
                .titulo(request.titulo())
                .descricao(request.descricao())
                .status(request.status())
                .projeto(projeto)
                .build();

        Tarefa salva = tarefaRepository.save(tarefa);
        return toResponse(salva);
    }

    @Transactional
    public void excluir(Long id) {
        if (!tarefaRepository.existsById(id)) {
            throw new EntityNotFoundException("Tarefa não encontrada");
        }
        tarefaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<TarefaResponse> listar(Long idProjeto, int page, int size, String sortBy, String sortDir) {
        String prop = (sortBy == null || sortBy.isBlank()) ? "dataCriacao" : sortBy;
        if (!(prop.equals("dataCriacao") || prop.equals("titulo") || prop.equals("status") || prop.equals("id"))) {
            prop = "dataCriacao";
        }
        Sort.Direction direction = ("asc".equalsIgnoreCase(sortDir)) ? Sort.Direction.ASC : Sort.Direction.DESC;
        var pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        var tarefas = (idProjeto != null)
                ? tarefaRepository.findByProjeto_Id(idProjeto, pageable)
                : tarefaRepository.findAll(pageable);

        return tarefas.map(this::toResponse);
    }

    private TarefaResponse toResponse(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getDataCriacao(),
                tarefa.getProjeto() != null ? tarefa.getProjeto().getId() : null,
                tarefa.getProjeto() != null ? tarefa.getProjeto().getNome() : null
        );
    }
}
