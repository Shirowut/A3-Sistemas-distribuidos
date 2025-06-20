package com.banco.a3.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String conta;
    private String senha;
}