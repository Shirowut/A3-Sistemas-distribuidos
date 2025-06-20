package com.banco.a3.controllers;

import com.banco.a3.dto.OperacaoRequestDto;
import com.banco.a3.dto.TransferenciaRequestDto;
import com.banco.a3.models.Conta;
import com.banco.a3.models.Transacao;
import com.banco.a3.services.ContaService;
import com.banco.a3.services.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<Conta> criarConta(@RequestBody Conta novaConta) {
        Conta contaCriada = contaService.criarConta(novaConta);
        return new ResponseEntity<>(contaCriada, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<Conta> getContaLogada() {
        String numeroConta = getNumeroContaLogada();
        return contaService.buscarPorNumeroConta(numeroConta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private String getNumeroContaLogada() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/me/deposito")
    public ResponseEntity<Void> depositar(@RequestBody OperacaoRequestDto request) {
        try {
            transacaoService.depositar(getNumeroContaLogada(), request.getValor());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().header("error-message", e.getMessage()).build();
        }
    }

    @PostMapping("/me/saque")
    public ResponseEntity<Void> sacar(@RequestBody OperacaoRequestDto request) {
        try {
            transacaoService.sacar(getNumeroContaLogada(), request.getValor());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().header("error-message", e.getMessage()).build();
        }
    }

    @PostMapping("/me/transferencia")
    public ResponseEntity<Void> transferir(@RequestBody TransferenciaRequestDto request) {
        try {
            transacaoService.transferir(getNumeroContaLogada(), request.getContaDestino(), request.getValor());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().header("error-message", e.getMessage()).build();
        }
    }

    @GetMapping("/me/extrato")
    public ResponseEntity<List<Transacao>> extrato() {
        List<Transacao> extrato = transacaoService.getExtrato(getNumeroContaLogada());
        return ResponseEntity.ok(extrato);
    }

}