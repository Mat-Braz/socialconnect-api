package br.com.socialconnect.api.produtos.dto;

import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.validation.EstoqueNaoNegativo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProdutoRequestDTO(

        @Schema(description = "Nome do produto", example = "Arroz Tio João 5kg")
        @NotBlank(message = "O nome do produto é obrigatório.")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
        String nome,

        @Schema(description = "Categoria do produto", example = "ALIMENTOS")
        @NotNull(message = "A categoria do produto é obrigatória.")
        CategoriaProduto categoria,

        @Schema(description = "Estoque atual", example = "10")
        @NotNull(message = "O estoque atual é obrigatório.")
        @EstoqueNaoNegativo(message = "O estoque atual deve ser maior ou igual a zero.")
        Integer estoqueAtual,

        @Schema(description = "Estoque mínimo", example = "2")
        @NotNull(message = "O estoque mínimo é obrigatório.")
        @EstoqueNaoNegativo(message = "O estoque mínimo deve ser maior ou igual a zero.")
        Integer estoqueMinimo,

        @Schema(description = "Unidade de medida", example = "kg")
        @NotBlank(message = "A unidade de medida é obrigatória.")
        @Size(max = 20, message = "A unidade de medida deve ter no máximo 20 caracteres.")
        String unidadeMedida

) {}