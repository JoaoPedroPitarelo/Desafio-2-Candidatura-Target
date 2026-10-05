package org.jopitarelo.desafio_target_1;

import org.jopitarelo.desafio_target_1.models.Movimentacao;
import org.jopitarelo.desafio_target_1.models.Produto;
import org.jopitarelo.desafio_target_1.models.TipoMovimentacao;
import org.jopitarelo.desafio_target_1.services.EstoqueService;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        EstoqueService service = new EstoqueService();
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            exibirMenu();
            try {
                opcao = lerInt(scanner, "Opção: ");
                switch (opcao) {
                    case 1 -> listarProdutos(service);
                    case 2 -> lancar(scanner, service, TipoMovimentacao.ENTRADA);
                    case 3 -> lancar(scanner, service, TipoMovimentacao.SAIDA);
                    case 4 -> listarHistorico(service);
                    case 0 -> System.out.println("Encerrando.");
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n=== Controle de Estoque ===");
        System.out.println("1 - Listar produtos");
        System.out.println("2 - Lançar entrada");
        System.out.println("3 - Lançar saída");
        System.out.println("4 - Histórico de movimentações");
        System.out.println("0 - Sair");
    }

    private static void listarProdutos(EstoqueService service) {
        for (Produto p : service.listarProdutos()) {
            System.out.printf("%d - %s (estoque: %d)%n",
                    p.getCodigoProduto(), p.getDescricaoProduto(), p.getEstoque());
        }
    }

    private static void lancar(Scanner scanner, EstoqueService service, TipoMovimentacao tipo) {
        int codigo = lerInt(scanner, "Código do produto: ");
        int quantidade = lerInt(scanner, "Quantidade: ");
        System.out.print("Descrição da movimentação: ");
        String descricao = scanner.nextLine();

        Movimentacao mov = service.movimentar(codigo, tipo, quantidade, descricao);
        System.out.printf("Movimentação #%d registrada (%s). Estoque final do produto %d: %d%n",
                mov.id(), mov.tipo(), mov.codigoProduto(), mov.estoqueFinal());
    }

    private static void listarHistorico(EstoqueService service) {
        if (service.listarHistorico().isEmpty()) {
            System.out.println("Nenhuma movimentação registrada.");
            return;
        }
        for (Movimentacao m : service.listarHistorico()) {
            System.out.printf("#%d | %s | produto %d | qtd %d | %s | estoque final %d%n",
                    m.id(), m.tipo(), m.codigoProduto(), m.quantidade(),
                    m.descricao(), m.estoqueFinal());
        }
    }

    private static int lerInt(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Digite um número inteiro válido.");
        }
    }
}