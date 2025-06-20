package com.banco.a3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendAccountCreationEmail(String to, String nome) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("banco@uam.com");
        message.setTo(to);
        message.setSubject("Banco UAM - Conta Criada com Sucesso");
        message.setText("Olá, " + nome + ".\n\n"
                + "Uma nova conta em seu nome foi criada em nossa instituição.\n\n"
                + "Se você não reconhece esta atividade, por favor, entre em contato com nosso suporte urgentemente.\n\n"
                + "Atenciosamente,\nEquipe de Segurança do Banco UAM.");
        
        mailSender.send(message);
    }


    public void sendTransferNotificationEmail(String to, String nome, BigDecimal valor) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("banco@uam.com");
        message.setTo(to);
        message.setSubject("Banco UAM - Notificação de Transferência Enviada");
        message.setText("Olá, " + nome + ".\n\n"
                + String.format("Uma transferência no valor de R$ %.2f foi enviada da sua conta.\n\n", valor)
                + "Se você não reconhece esta atividade, por favor, entre em contato com nosso suporte urgentemente.\n\n"
                + "Atenciosamente,\nEquipe de Segurança do Banco UAM.");

        mailSender.send(message);
    }
}