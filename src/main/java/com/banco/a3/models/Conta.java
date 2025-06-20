package com.banco.a3.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "contas")
@Data
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    @Column(unique = true, nullable = false, length = 5)
    private String conta;

    @Column(nullable = false)
    private LocalDate nascimento;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Column(unique = true, nullable = false)
    private String email;


    @Column(unique = true, nullable = false)
    private String cpf;
}