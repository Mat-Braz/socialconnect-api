package br.com.socialconnect.api.doadores.dto;

import br.com.socialconnect.api.doadores.model.TipoDoador;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DoadorRequestDTO(

        @Schema(description = "Nome do doador", example = "Carlos Santos")
        @NotBlank(message = "{NotBlank.doador.nome}")
        @Size(max = 100, message = "{Size.doador.nome}")
        String nome,

        @Schema(description = "Tipo do doador", example = "PESSOA_FISICA")
        @NotNull(message = "{NotNull.doador.tipo}")
        TipoDoador tipo,

        @Schema(description = "E-mail de contato", example = "carlos.santos@email.com")
        @NotBlank(message = "{NotBlank.doador.email}")
        @Email(message = "{Email.doador.email}")
        @Size(max = 80, message = "{Size.doador.email}")
        String email,

        @Schema(description = "Telefone com DDD", example = "11988887777")
        @NotBlank(message = "{NotBlank.doador.telefone}")
        @Size(max = 15, message = "{Size.doador.telefone}")
        String telefone

) {}