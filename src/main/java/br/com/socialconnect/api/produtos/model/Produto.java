package br.com.socialconnect.api.produtos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long idProduto;

    @Column(nullable = false, unique = true, length = 150)
    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_produto", nullable = false, length = 20)
    @NotNull(message = "A categoria é obrigatória.")
    private CategoriaProduto categoria;

    @Column(nullable = false)
    @NotNull(message = "O estoque atual é obrigatório.")
    @Min(value = 0, message = "O valor deve ser maior ou igual a zero.")
    private Integer estoqueAtual;

    @Column(nullable = false)
    @NotNull(message = "O estoque mínimo é obrigatório.")
    @Min(value = 0, message = "O valor deve ser maior ou igual a zero.")
    private Integer estoqueMinimo;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "A unidade de medida é obrigatória.")
    @Size(max = 20, message = "A unidade de medida deve ter no máximo 20 caracteres.")
    private String unidadeMedida;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;
}
