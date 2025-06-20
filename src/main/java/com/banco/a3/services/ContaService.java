package com.banco.a3.services;

import com.banco.a3.models.Conta;
import com.banco.a3.repositories.ContaRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();

    public ContaService(ContaRepository contaRepository, PasswordEncoder passwordEncoder) {
        this.contaRepository = contaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired // Injete o serviço de e-mail
    private EmailService emailService;

    public Conta criarConta(Conta conta) {
        String numeroConta;
        do {
            int numeroGerado = random.nextInt(90000) + 10000;
            numeroConta = String.valueOf(numeroGerado);
        } while (contaRepository.existsByConta(numeroConta));

        conta.setConta(numeroConta);
        conta.setSaldo(BigDecimal.ZERO);

        conta.setSenha(passwordEncoder.encode(conta.getSenha()));
        Conta contaSalva = contaRepository.save(conta);
        emailService.sendAccountCreationEmail(contaSalva.getEmail(), contaSalva.getNome());
        return contaSalva;
    }

    public Optional<Conta> buscarPorNumeroConta(String numeroConta) {
        return contaRepository.findByConta(numeroConta);
    }
}