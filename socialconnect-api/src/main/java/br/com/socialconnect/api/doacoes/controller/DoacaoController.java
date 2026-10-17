package br.com.socialconnect.api.doacoes.controller;

import br.com.socialconnect.api.doacoes.dto.DoacaoPatchDTO;
import br.com.socialconnect.api.doacoes.dto.DoacaoRequestDTO;
import br.com.socialconnect.api.doacoes.dto.DoacaoResponseDTO;
import br.com.socialconnect.api.doacoes.model.TipoDoacao;
import br.com.socialconnect.api.doacoes.service.DoacaoService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/doacoes")
@RequiredArgsConstructor
@Tag(name = "Doações", description = "Endpoints para gerenciamento de doações")
public class DoacaoController {

    private final DoacaoService service;

    @PostMapping
    @Operation(
            summary = "Cria uma nova doação",
            description = "Cadastra um novo registro de doação no sistema"
    )
    @ApiResponse(responseCode = "201", description = "Doação criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<DoacaoResponseDTO> criar(
            @RequestBody @Valid DoacaoRequestDTO dto) {
        DoacaoResponseDTO salvo = service.criar(dto);
        URI location = URI.create("/api/v1/doacoes/" + salvo.idDoacao());
        return ResponseEntity.created(location).body(salvo);
    }

    @GetMapping
    @Operation(
            summary = "Lista todas as doações",
            description = "Retorna uma lista paginada de doações com filtros opcionais por período de data e tipo de doação"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    public ResponseEntity<Page<DoacaoResponseDTO>> listar(
            @Parameter(description = "Data inicial para filtro (AAAA-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,

            @Parameter(description = "Data final para filtro (AAAA-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,

            @Parameter(description = "Tipo da doação")
            @RequestParam(required = false) TipoDoacao tipo,

            @PageableDefault(size = 10, sort = "dataDoacao", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(service.listar(dataInicio, dataFim, tipo, pageable));
    }

    @PutMapping("/{idDoacao}")
    @Operation(
            summary = "Atualiza uma doação (substituição total)",
            description = "Atualiza todos os dados de uma doação existente"
    )
    @ApiResponse(responseCode = "200", description = "Doação atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Doação não encontrada")
    public ResponseEntity<DoacaoResponseDTO> atualizar(
            @Parameter(description = "ID da doação")
            @PathVariable Long idDoacao,
            @Valid @RequestBody DoacaoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(idDoacao, dto));
    }

    @PatchMapping("/{idDoacao}")
    @Operation(
            summary = "Atualiza uma doação parcialmente",
            description = "Atualiza campos específicos de uma doação existente"
    )
    @ApiResponse(responseCode = "200", description = "Doação atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Doação não encontrada")
    public ResponseEntity<DoacaoResponseDTO> atualizarParcial(
            @Parameter(description = "ID da doação")
            @PathVariable Long idDoacao,
            @Valid @RequestBody DoacaoPatchDTO dto) {
        return ResponseEntity.ok(service.atualizarParcial(idDoacao, dto));
    }

    @DeleteMapping("/{idDoacao}")
    @Operation(
            summary = "Remove uma doação",
            description = "Exclui uma doação do sistema pelo ID"
    )
    @ApiResponse(responseCode = "204", description = "Doação removida com sucesso")
    @ApiResponse(responseCode = "404", description = "Doação não encontrada")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da doação")
            @PathVariable Long idDoacao) {
        service.deletar(idDoacao);
        return ResponseEntity.noContent().build();
    }
}