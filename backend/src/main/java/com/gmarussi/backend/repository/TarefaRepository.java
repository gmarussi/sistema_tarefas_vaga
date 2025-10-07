package com.gmarussi.backend.repository;

import com.gmarussi.backend.model.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    Page<Tarefa> findByProjeto_IdOrderByDataCriacaoDesc(Long idProjeto, Pageable pageable);
    Page<Tarefa> findAllByOrderByDataCriacaoDesc(Pageable pageable);
}
