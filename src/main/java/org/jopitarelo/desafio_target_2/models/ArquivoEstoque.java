package org.jopitarelo.desafio_target_2.models;

import java.util.List;

public class ArquivoEstoque {
    private List<Produto> estoque;

    public ArquivoEstoque() {}

    public List<Produto> getEstoque() { return estoque; }
    public void setEstoque(List<Produto> estoque) { this.estoque = estoque; }
}
