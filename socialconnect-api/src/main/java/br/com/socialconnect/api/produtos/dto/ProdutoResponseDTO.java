package br.com.socialconnect.api.produtos.dto;

import br.com.socialconnect.api.produtos.model.CategoriaProduto;

import java.time.LocalDate;

public record ProdutoResponseDTO (
        Long idProduto,
        String nome,
        CategoriaProduto categoria,
        Integer estoqueAtual,
        Integer estoqueMinimo,
        String unidadeMedida,
        LocalDate dataCadastro,
        boolean estoqueBaixo
){}


