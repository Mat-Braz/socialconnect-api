package br.com.socialconnect.api.doadores.dto;

import br.com.socialconnect.api.doadores.model.TipoDoador;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record DoadorPatchDTO(

        @Schema(description = "Nome do doador", example = "Carlos Eduardo Santos")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Schema(description = "Tipo do doador", example = "PESSOA_FISICA")
        TipoDoador tipo,

        @Schema(description = "E-mail de contato", example = "carlos.santos@email.com")
        @Email(message = "E-mail com formato inválido")
        @Size(max = 80, message = "O e-mail deve ter no máximo 80 caracteres")
        String email,

        @Schema(description = "Telefone com DDD", example = "11988887777")
        @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres")
        String telefone

) {}