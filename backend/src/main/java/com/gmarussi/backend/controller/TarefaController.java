package com.gmarussi.backend.controller;

import com.gmarussi.backend.dto.TarefaRequest;
import com.gmarussi.backend.dto.TarefaResponse;
import com.gmarussi.backend.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/tarefas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaResponse> criar(
            @Valid @RequestBody TarefaRequest request,
            UriComponentsBuilder uriBuilder) {

        var tarefaCriada = tarefaService.salvar(request);
        URI location = uriBuilder.path("/api/v1/tarefas/{id}")
                .buildAndExpand(tarefaCriada.id())
                .toUri();

        return ResponseEntity.created(location).body(tarefaCriada);
    }

    /**
     * Lista tarefas com paginação, filtro por projeto e ordenação decrescente por data.
     */
    @GetMapping
    public ResponseEntity<Page<TarefaResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long idProjeto) {

        var tarefas = tarefaService.listar(idProjeto, page, size);
        return ResponseEntity.ok(tarefas);
    }

    /**
     * Exclui uma tarefa existente pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        tarefaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
