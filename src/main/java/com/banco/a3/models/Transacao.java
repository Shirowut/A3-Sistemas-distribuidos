package com.banco.a3.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Data
@NoArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipo;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDateTime data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    @Column(name = "conta_contraparte")
    private String contaContraparte;

    public Transacao(TipoTransacao tipo, BigDecimal valor, Conta conta, String contaContraparte) {
        this.tipo = tipo;
        this.valor = valor;
        this.data = LocalDateTime.now();
        this.conta = conta;
        this.contaContraparte = contaContraparte;
    }
}