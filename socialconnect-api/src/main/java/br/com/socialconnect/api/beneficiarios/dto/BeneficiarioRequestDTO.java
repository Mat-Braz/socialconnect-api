package br.com.socialconnect.api.beneficiarios.dto;

import br.com.socialconnect.api.validation.CPF;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record BeneficiarioRequestDTO(

        @Schema(description = "Nome completo do beneficiário", example = "Maria da Silva")
        @NotBlank(message = "{NotBlank.nome}")
        @Size(max = 150, message = "{Size.nome}")
        String nome,

        @Schema(description = "CPF sem formatação", example = "12345678900")
        @NotBlank(message = "{NotBlank.cpf}")
        @Pattern(
                regexp = "\\d{11}|\\d{14}",
                message = "{Pattern.cpf}"
        )
        @CPF
        String cpf,

        @Schema(description = "Telefone com DDD", example = "11999999999")
        @Size(max = 20, message = "{Size.telefone}")
        String telefone,

        @Size(max = 255, message = "{Size.endereco}")
        String endereco,

        @Size(max = 500, message = "{Size.situacaoVulnerabilidade}")
        String situacaoVulnerabilidade

) {}