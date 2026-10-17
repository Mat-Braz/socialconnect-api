package br.com.socialconnect.api.doacoes.dto;

import br.com.socialconnect.api.doacoes.model.TipoDoacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DoacaoRequestDTO(

        @Schema(description = "Identificador do doador", example = "1")
        @NotNull(message = "{NotNull.doacao.idDoador}")
        @Positive(message = "{Positive.doacao.idDoador}")
        Long idDoador,

        @Schema(description = "Data da doação", example = "2026-03-15")
        @NotNull(message = "{NotNull.doacao.dataDoacao}")
        @PastOrPresent(message = "{PastOrPresent.doacao.dataDoacao}")
        LocalDate dataDoacao,

        @Schema(description = "Valor da doação", example = "150.00")
        @NotNull(message = "{NotNull.doacao.valor}")
        @DecimalMin(value = "0.01", message = "{DecimalMin.doacao.valor}")
        @Digits(integer = 10, fraction = 2, message = "{Digits.doacao.valor}")
        BigDecimal valor,

        @Schema(description = "Tipo da doação", example = "FINANCEIRA")
        @NotNull(message = "{NotNull.doacao.tipo}")
        TipoDoacao tipo,

        @Schema(description = "Descrição detalhada", example = "Doação para projeto de alimentação")
        @NotBlank(message = "{NotBlank.doacao.descricao}")
        @Size(max = 500, message = "{Size.doacao.descricao}")
        String descricao

) {}