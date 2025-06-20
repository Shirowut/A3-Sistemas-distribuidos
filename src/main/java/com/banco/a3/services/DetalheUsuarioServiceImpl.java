package com.banco.a3.services;

import com.banco.a3.models.Conta;
import com.banco.a3.repositories.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DetalheUsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private ContaRepository contaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Conta conta = contaRepository.findByConta(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com número da conta: " + username));

        return new User(conta.getConta(), conta.getSenha(), new ArrayList<>());
    }
}