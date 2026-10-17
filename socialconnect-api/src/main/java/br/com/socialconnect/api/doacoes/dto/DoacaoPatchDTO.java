package br.com.socialconnect.api.doacoes.dto;

import br.com.socialconnect.api.doacoes.model.TipoDoacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DoacaoPatchDTO(

        @Schema(description = "Identificador do doador", example = "1")
        @Positive(message = "O ID do doador deve ser um número positivo")
        Long idDoador,

        @Schema(description = "Data em que a doação foi realizada (formato AAAA-MM-DD)", example = "2026-03-15")
        @PastOrPresent(message = "A data da doação não pode ser uma data futura")
        LocalDate dataDoacao,

        @Schema(description = "Valor da doação", example = "150.00")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
        @Digits(integer = 10, fraction = 2, message = "Formato de valor inválido. Máximo de 10 dígitos inteiros e 2 casas decimais")
        BigDecimal valor,

        @Schema(description = "Tipo da doação realizada", example = "FINANCEIRA")
        TipoDoacao tipo,

        @Schema(description = "Descrição detalhada da doação", example = "Doação financeira destinada ao projeto de alimentação")
        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        String descricao

) {}