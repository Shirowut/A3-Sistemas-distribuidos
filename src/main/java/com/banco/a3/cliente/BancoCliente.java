package com.banco.a3.cliente;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BancoCliente {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final String SERVER_URL = "http://localhost:8080";
    private static String jwtToken = null;
    private static String nomeUsuarioLogado = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (jwtToken == null) {
                exibirMenuDeslogado(scanner);
            } else {
                exibirMenuLogado(scanner);
            }
        }
    }

    private static void exibirMenuDeslogado(Scanner scanner) {
        System.out.println("\n--- Bem-vindo ao Banco UAM ---");
        System.out.println("1. Criar Nova Conta");
        System.out.println("2. Acessar Conta (Login)");
        System.out.println("3. Sair");
        System.out.print("Escolha uma opção: ");

        String opcao = scanner.nextLine();
        switch (opcao) {
            case "1":
                criarNovaConta(scanner);
                break;
            case "2":
                login(scanner);
                break;
            case "3":
                System.exit(0);
            default:
                System.out.println("Opção inválida.");
        }
    }

    private static void exibirMenuLogado(Scanner scanner) {
        System.out.println("\n--- Olá, " + nomeUsuarioLogado + "! ---");
        System.out.println("1. Ver Saldo e Dados da Conta");
        System.out.println("2. Extrato");
        System.out.println("3. Depósito");
        System.out.println("4. Saque");
        System.out.println("5. Transferência");
        System.out.println("6. Sair (Logout)");
        System.out.print("Escolha uma opção: ");

        String opcao = scanner.nextLine();
        switch (opcao) {
            case "1":
                verMeusDados();
                break;
            case "2":
                verExtrato();
                break;
            case "3":
                realizarDeposito(scanner);
                break;
            case "4":
                realizarSaque(scanner);
                break;
            case "5":
                realizarTransferencia(scanner);
                break;
            case "6":
                logout();
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private static void realizarDeposito(Scanner scanner) {
        try {
            System.out.print("Digite o valor do depósito: ");
            BigDecimal valor = new BigDecimal(scanner.nextLine());

            Map<String, BigDecimal> requestBody = new HashMap<>();
            requestBody.put("valor", valor);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/api/contas/me/deposito"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Depósito realizado com sucesso!");
            } else {
                System.out.println("Erro no depósito: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private static void realizarSaque(Scanner scanner) {
        try {
            System.out.print("Digite o valor do saque: ");
            BigDecimal valor = new BigDecimal(scanner.nextLine());

            Map<String, BigDecimal> requestBody = new HashMap<>();
            requestBody.put("valor", valor);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/api/contas/me/saque"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Saque realizado com sucesso!");
            } else {
                System.out.println("Erro no saque: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private static void realizarTransferencia(Scanner scanner) {
        try {
            System.out.print("Digite o número da conta de destino: ");
            String contaDestino = scanner.nextLine();
            System.out.print("Digite o valor da transferência: ");
            BigDecimal valor = new BigDecimal(scanner.nextLine());

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contaDestino", contaDestino);
            requestBody.put("valor", valor);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/api/contas/me/transferencia"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Transferência realizada com sucesso!");
            } else {
                System.out.println("Erro na transferência: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private static void verExtrato() {
        if (jwtToken == null) {
            System.out.println("Faça login para ver o extrato.");
            return;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/api/contas/me/extrato"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("\n--- Extrato da Conta ---");
                List<Map<String, Object>> transacoes = objectMapper.readValue(response.body(), new TypeReference<>() {
                });
                if (transacoes.isEmpty()) {
                    System.out.println("Nenhuma transação encontrada.");
                } else {
                    for (Map<String, Object> t : transacoes) {
                        System.out.printf("Data: %s | Tipo: %s | Valor: R$ %.2f",
                                t.get("data"), t.get("tipo"), new BigDecimal(t.get("valor").toString()));
                        if (t.get("contaContraparte") != null) {
                            System.out.printf(" | Contraparte: %s", t.get("contaContraparte"));
                        }
                        System.out.println();
                    }
                }
            } else {
                System.out.println("Erro ao buscar extrato: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private static void logout() {
        jwtToken = null;
        nomeUsuarioLogado = null;
        System.out.println("\nVocê saiu da sua conta.");
    }

    private static void login(Scanner scanner) {
        try {
            System.out.print("Número da Conta: ");
            String conta = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("conta", conta);
            loginRequest.put("senha", senha);
            String requestBody = objectMapper.writeValueAsString(loginRequest);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/api/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, String> loginResponse = objectMapper.readValue(response.body(), new TypeReference<>() {
                });
                jwtToken = loginResponse.get("token");
                verMeusDados(true);
                System.out.println("\n--- Login bem-sucedido! ---");
            } else {
                System.out.println("\n--- Falha no login. ---");
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private static void verMeusDados() {
        verMeusDados(false);
    }

    private static void verMeusDados(boolean silencioso) {
        if (jwtToken == null) {
            if (!silencioso)
                System.out.println("\n--- Erro: Você precisa fazer login primeiro! ---");
            return;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/api/contas/me"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, Object> conta = objectMapper.readValue(response.body(), new TypeReference<>() {
                });
                nomeUsuarioLogado = (String) conta.get("nome");
                if (!silencioso) {
                    System.out.println("\n--- Dados da Sua Conta ---");
                    System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(conta));
                }
            } else {
                if (!silencioso)
                    System.out.println("\n--- Erro ao buscar dados. ---");
            }
        } catch (Exception e) {
            if (!silencioso)
                System.err.println("Erro: " + e.getMessage());
        }
    }

    private static void criarNovaConta(Scanner scanner) {
        try {
            System.out.println("\n--- Criação de Conta ---");
            System.out.print("Nome completo: ");
            String nome = scanner.nextLine();
            System.out.print("CPF (apenas números): ");
            String cpf = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Data de Nascimento (formato AAAA-MM-DD): ");
            String nascimento = scanner.nextLine();
            System.out.print("Crie uma senha: ");
            String senha = scanner.nextLine();

            Map<String, String> novaConta = new HashMap<>();
            novaConta.put("nome", nome);
            novaConta.put("cpf", cpf);
            novaConta.put("email", email);
            novaConta.put("nascimento", nascimento);
            novaConta.put("senha", senha);

            String requestBody = objectMapper.writeValueAsString(novaConta);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + "/api/contas"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                System.out.println("\n--- Conta criada com sucesso! ---");
                Map<String, Object> contaCriada = objectMapper.readValue(response.body(), new TypeReference<>() {
                });
                System.out.println("Titular: " + contaCriada.get("nome"));
                System.out.println("Número da Conta: " + contaCriada.get("conta")
                        + " <-- Anote este número! Você o usará para fazer login.");
                System.out.println("Saldo inicial: R$ " + contaCriada.get("saldo"));
            } else {
                System.out.println("\n--- Erro ao criar conta ---");
                System.out.println("Status: " + response.statusCode());
                System.out.println("Resposta do servidor: " + response.body());
            }

        } catch (Exception e) {
            System.err.println("Ocorreu um erro na comunicação com o servidor: " + e.getMessage());
        }
    }
}