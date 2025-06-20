package com.banco.a3.services;

import com.banco.a3.models.Conta;
import com.banco.a3.models.TipoTransacao;
import com.banco.a3.models.Transacao;
import com.banco.a3.repositories.ContaRepository;
import com.banco.a3.repositories.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransacaoService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void depositar(String numeroConta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }
        Conta conta = contaRepository.findByConta(numeroConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        conta.setSaldo(conta.getSaldo().add(valor));
        contaRepository.save(conta);

        Transacao transacao = new Transacao(TipoTransacao.DEPOSITO, valor, conta, null);
        transacaoRepository.save(transacao);
    }

    @Transactional
    public void sacar(String numeroConta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser positivo.");
        }
        Conta conta = contaRepository.findByConta(numeroConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente.");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        contaRepository.save(conta);

        Transacao transacao = new Transacao(TipoTransacao.SAQUE, valor, conta, null);
        transacaoRepository.save(transacao);
    }

    @Transactional
    public void transferir(String numContaOrigem, String numContaDestino, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }
        if (numContaOrigem.equals(numContaDestino)) {
            throw new IllegalArgumentException("A conta de origem e destino não podem ser as mesmas.");
        }

        Conta contaOrigem = contaRepository.findByConta(numContaOrigem)
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada."));
        Conta contaDestino = contaRepository.findByConta(numContaDestino)
                .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada."));

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente na conta de origem.");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        Transacao transacaoOrigem = new Transacao(TipoTransacao.TRANSFERENCIA_ENVIADA, valor, contaOrigem,
                numContaDestino);
        Transacao transacaoDestino = new Transacao(TipoTransacao.TRANSFERENCIA_RECEBIDA, valor, contaDestino,
                numContaOrigem);
        transacaoRepository.save(transacaoOrigem);
        transacaoRepository.save(transacaoDestino);
        emailService.sendTransferNotificationEmail(contaOrigem.getEmail(), contaOrigem.getNome(), valor);
    }

    public List<Transacao> getExtrato(String numeroConta) {
        Conta conta = contaRepository.findByConta(numeroConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));
        return transacaoRepository.findByContaIdOrderByDataDesc(conta.getId());
    }
}