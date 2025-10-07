package com.gmarussi.backend.repository;

import com.gmarussi.backend.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
