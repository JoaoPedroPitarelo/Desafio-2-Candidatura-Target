package org.jopitarelo.desafio_target_2.services;

import org.jopitarelo.desafio_target_2.models.ArquivoEstoque;
import org.jopitarelo.desafio_target_2.models.Movimentacao;
import org.jopitarelo.desafio_target_2.models.Produto;
import org.jopitarelo.desafio_target_2.models.TipoMovimentacao;

import tools.jackson.databind.json.JsonMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EstoqueService {

    private final JsonMapper mapper = JsonMapper.builder().build();
    private final Map<Integer, Produto> produtos = new LinkedHashMap<>();
    private final List<Movimentacao> historico = new ArrayList<>();
    private long proximoId = 1;

    public EstoqueService() throws IOException {
        carregarProdutos();
    }

    private void carregarProdutos() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/estoque.json")) {
            if (is == null) {
                throw new FileNotFoundException("estoque.json não encontrado no classpath");
            }
            ArquivoEstoque arquivo = mapper.readValue(is, ArquivoEstoque.class);
            for (Produto p : arquivo.getEstoque()) {
                produtos.put(p.getCodigoProduto(), p);
            }
        }
    }

    public Movimentacao movimentar(int codigoProduto, TipoMovimentacao tipo,
                                   int quantidade, String descricao) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Informe uma descrição para a movimentação.");
        }

        Produto produto = produtos.get(codigoProduto);
        if (produto == null) {
            throw new IllegalArgumentException("Produto " + codigoProduto + " não encontrado.");
        }

        int novoEstoque = switch (tipo) {
            case ENTRADA -> produto.getEstoque() + quantidade;
            case SAIDA -> {
                if (quantidade > produto.getEstoque()) {
                    throw new IllegalArgumentException(
                            "Estoque insuficiente. Disponível: " + produto.getEstoque());
                }
                yield produto.getEstoque() - quantidade;
            }
        };

        produto.setEstoque(novoEstoque);

        Movimentacao mov = new Movimentacao(
                proximoId++, codigoProduto, tipo, descricao.trim(), quantidade, novoEstoque);
        historico.add(mov);
        return mov;
    }

    public List<Produto> listarProdutos() {
        return List.copyOf(produtos.values());
    }

    public List<Movimentacao> listarHistorico() {
        return List.copyOf(historico);
    }
}
