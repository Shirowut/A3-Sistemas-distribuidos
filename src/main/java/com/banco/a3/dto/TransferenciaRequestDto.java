package com.banco.a3.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransferenciaRequestDto {
    private String contaDestino;
    private BigDecimal valor;
}