package org.jopitarelo.desafio_target_1.models;

public record Movimentacao(
        long id,
        int codigoProduto,
        TipoMovimentacao tipo,
        String descricao,
        int quantidade,
        int estoqueFinal
) {}