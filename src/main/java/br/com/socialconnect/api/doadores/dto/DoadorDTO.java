package br.com.socialconnect.api.doadores.dto;

import br.com.socialconnect.api.doadores.model.TipoDoador;

public record DoadorDTO(
        Long idDoador,
        String nome,
        TipoDoador tipo,
        String email,
        String telefone
) {}