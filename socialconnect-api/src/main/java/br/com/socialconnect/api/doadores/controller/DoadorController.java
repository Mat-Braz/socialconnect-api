package br.com.socialconnect.api.doadores.controller;

import br.com.socialconnect.api.doadores.dto.DoadorPatchDTO;
import br.com.socialconnect.api.doadores.dto.DoadorRequestDTO;
import br.com.socialconnect.api.doadores.dto.DoadorResponseDTO;
import br.com.socialconnect.api.doadores.model.TipoDoador;
import br.com.socialconnect.api.doadores.service.DoadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/doadores")
@RequiredArgsConstructor
@Tag(name = "Doadores", description = "Endpoints para gerenciamento de doadores")
public class DoadorController {

    private final DoadorService service;

    @PostMapping
    @Operation(
            summary = "Cria um novo doador",
            description = "Cadastra um novo registro de doador no sistema"
    )
    @ApiResponse(responseCode = "201", description = "Doador criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    public ResponseEntity<DoadorResponseDTO> criar(
            @RequestBody @Valid DoadorRequestDTO dto) {
        DoadorResponseDTO salvo = service.criar(dto);
        URI location = URI.create("/api/v1/doadores/" + salvo.idDoador());
        return ResponseEntity.created(location).body(salvo);
    }

    @GetMapping
    @Operation(
            summary = "Lista todos os doadores",
            description = "Retorna uma lista paginada de doadores com filtros opcionais por nome e tipo de doador"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    public ResponseEntity<Page<DoadorResponseDTO>> listar(
            @Parameter(description = "Nome para filtrar (parcial)")
            @RequestParam(required = false) String nome,

            @Parameter(description = "Tipo do doador (ex: FISICA, JURIDICA)")
            @RequestParam(required = false) TipoDoador tipo,

            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok(service.listar(nome, tipo, pageable));
    }

    @GetMapping("/{idDoador}")
    @Operation(
            summary = "Busca doador por ID",
            description = "Retorna os detalhes de um doador específico pelo seu identificador"
    )
    @ApiResponse(responseCode = "200", description = "Doador encontrado")
    @ApiResponse(responseCode = "404", description = "Doador não encontrado")
    public ResponseEntity<DoadorResponseDTO> buscarPorId(
            @Parameter(description = "ID do doador")
            @PathVariable Long idDoador) {
        return ResponseEntity.ok(service.buscarPorId(idDoador));
    }

    @PutMapping("/{idDoador}")
    @Operation(
            summary = "Atualiza um doador (substituição total)",
            description = "Atualiza todos os dados de um doador existente"
    )
    @ApiResponse(responseCode = "200", description = "Doador atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Doador não encontrado")
    public ResponseEntity<DoadorResponseDTO> atualizar(
            @Parameter(description = "ID do doador")
            @PathVariable Long idDoador,
            @Valid @RequestBody DoadorRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(idDoador, dto));
    }

    @PatchMapping("/{idDoador}")
    @Operation(
            summary = "Atualiza um doador parcialmente",
            description = "Atualiza campos específicos de um doador existente"
    )
    @ApiResponse(responseCode = "200", description = "Doador atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Doador não encontrado")
    public ResponseEntity<DoadorResponseDTO> atualizarParcial(
            @Parameter(description = "ID do doador")
            @PathVariable Long idDoador,
            @Valid @RequestBody DoadorPatchDTO dto) {
        return ResponseEntity.ok(service.atualizarParcial(idDoador, dto));
    }

    @DeleteMapping("/{idDoador}")
    @Operation(
            summary = "Remove um doador",
            description = "Exclui um doador do sistema pelo ID"
    )
    @ApiResponse(responseCode = "204", description = "Doador removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Doador não encontrado")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do doador")
            @PathVariable Long idDoador) {
        service.deletar(idDoador);
        return ResponseEntity.noContent().build();
    }
}