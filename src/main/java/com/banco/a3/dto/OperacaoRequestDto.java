package com.banco.a3.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OperacaoRequestDto {
    private BigDecimal valor;
}