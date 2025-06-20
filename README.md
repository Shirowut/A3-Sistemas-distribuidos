# A3-Sistemas-distribuidos
Projeto da A3 da UC de sistemas distribuídos e mobile

### Grupo:
- Gabriel Barros Lucena, **RA:** 12524111262
- Juan Julio Deodato, **RA:** 12522213811
- Rafael Stender Rocha, **RA:** 12524117225
- Renan Gabriel de Abreu, **RA:** 12524145023
- Sérgio Borges da Silva, **RA:** 12522210037

# Projeto A3 - Sistema Bancário Simples com Alertas de Segurança

Este projeto foi desenvolvido como trabalho acadêmico para a disciplina de **Sistemas Distribuídos e Mobile**.  
Ele simula as operações básicas de um banco com foco especial em segurança por meio de **notificações imediatas**.

## Mitigando o Golpe do Presente

Fraudes como o *Golpe do Presente* exploram a distração ou o desconhecimento técnico da vítima:  
a pessoa é induzida a autorizar uma transação (Pix, QR Code etc.) acreditando estar apenas confirmando dados ou aceitando um benefício — quando percebe, o dinheiro já foi enviado.

## A Solução

Para cada operação sensível na conta — criação de nova conta ou transferência — o sistema dispara um **e‑mail de alerta**.  
Mesmo que a vítima tenha autorizado a transação por engano, ela é avisada em um canal diferente (e‑mail) e pode agir rapidamente para contatar a instituição e denunciar a fraude.

---

## Como Rodar o Projeto

O projeto é dividido em duas partes:

1. **Servidor (backend)** ‑ contém toda a lógica da aplicação.  
2. **Cliente (frontend de linha de comando)** ‑ interface que o usuário executa.

### 1. Configuração do Ambiente

Edite `src/main/resources/application.properties`.

#### a) Definir o Banco de Dados (profile)

Escolha entre H2 (teste) ou MySQL (produção) alterando **uma única linha**:

```properties
# Para usar o banco H2 (ideal para testes)
spring.profiles.active=h2

# Para usar o banco MySQL (descomente a linha abaixo e configure as credenciais)
# spring.profiles.active=mysql
```

Se optar por MySQL, preencha suas credenciais em `application-mysql.properties`.

#### b) Definir o Segredo do JWT

Tokens JWT são assinados com um segredo: defina o seu próprio.

```properties
# Troque o valor abaixo por qualquer texto longo e aleatório
jwt.secret=StringComChaveParaAssinaturaDeTokensComPeloMenos75bytes
```

#### c) Configurar o Serviço de E‑mail

Para que as notificações funcionem, informe as credenciais de um servidor SMTP.  
Para testes, o [Mailtrap.io](https://mailtrap.io/) fornece uma caixa de entrada falsa.

```properties
# Exemplo de configuração usando o Mailtrap
spring.mail.host=sandbox.smtp.mailtrap.io
spring.mail.port=587
spring.mail.username=SEU_USUARIO_MAILTRAP
spring.mail.password=SUA_SENHA_MAILTRAP

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 2. Rodando o Servidor (Backend)

1. Abra o projeto (no VS Code, IntelliJ etc.).  
2. Encontre a classe com `A3GolpeBancoApplication`.  
3. Execute o método `main`.  
4. O servidor estará disponível em **http://localhost:8080**.

### 3. Rodando o Cliente

1. Com o servidor em execução, localize `BancoCliente.java` no pacote `com.banco.a3.cliente`.  
2. Execute o método `main`.  
3. O menu interativo aparecerá no console da IDE.

---

## Detalhes Técnicos e Diferenciais

- **Spring Boot** – base de todo o projeto, simplifica configuração.  
- **Spring Web** – criação da API REST.  
- **Spring Data JPA** – camada de acesso a dados, abstraindo SQL.  
- **Spring Security** – autenticação e proteção de rotas.  
- **Lombok** – redução de boilerplate (getters, setters, construtores).  
- **H2/MySQL** – suporte a banco em memória para testes e a banco real para produção.  
- **JJWT (Java JWT)** – criação e validação de tokens.  
- **Spring Boot Starter Mail** – envio de e‑mails simplificado.  
- **Arquitetura Cliente‑Servidor** – responsabilidades bem separadas.  
- **Divisão em Camadas** – *Controller* ➔ *Service* ➔ *Repository*.  
- **Senhas com Hash (BCrypt)** – nenhuma senha salva em texto puro.  
- **Autenticação *Stateless* com JWT** – não guarda sessão do usuário.  
- **Uso de Profiles** – troca de ambiente alterando apenas uma linha.  
- **Notificações de Segurança** – principal diferencial: alertas por e‑mail em operações críticas.
