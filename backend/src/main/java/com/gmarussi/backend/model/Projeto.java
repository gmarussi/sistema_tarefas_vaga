package com.gmarussi.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PROJETO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;
}
