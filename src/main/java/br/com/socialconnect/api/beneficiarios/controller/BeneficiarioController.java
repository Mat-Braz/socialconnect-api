package br.com.socialconnect.api.beneficiarios.controller;

import br.com.socialconnect.api.beneficiarios.dto.*;
import br.com.socialconnect.api.beneficiarios.service.BeneficiarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/beneficiarios")
@Tag(name = "Beneficiários", description = "Endpoints para gerenciamento de beneficiários")
public class BeneficiarioController {

    private final BeneficiarioService service;

    public BeneficiarioController(BeneficiarioService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "Cria um novo beneficiário",
            description = "Cadastra um novo beneficiário no sistema"
    )
    @ApiResponse(responseCode = "201", description = "Beneficiário criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "409", description = "CPF já cadastrado")
    public ResponseEntity<BeneficiarioResponseDTO> criar(
            @RequestBody @Valid BeneficiarioRequestDTO dto) {
        BeneficiarioResponseDTO salvo = service.criar(dto);
        URI location = URI.create("/api/v1/beneficiarios/" + salvo.idBeneficiario());
        return ResponseEntity.created(location).body(salvo);
    }

    @GetMapping
    @Operation(
            summary = "Lista todos os beneficiários",
            description = "Retorna uma lista paginada de beneficiários com filtros opcionais por nome e CPF"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    public ResponseEntity<Page<BeneficiarioResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idBeneficiario,asc") String sort) {

        // Sanitiza o parâmetro sort (remove colchetes, aspas, espaços)
        String sortLimpo = sort.replaceAll("[\\[\\]\" ]", "");

        // Divide em campo e direção
        String[] sortParts = sortLimpo.split(",");
        String campo = sortParts[0];
        Sort.Direction direcao = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        // Cria o Pageable manualmente
        Pageable pageable = PageRequest.of(page, size, Sort.by(direcao, campo));

        return ResponseEntity.ok(service.listar(nome, cpf, pageable));
    }

    @GetMapping("/{idBeneficiario}")
    @Operation(
            summary = "Busca beneficiário por ID",
            description = "Retorna os dados detalhados de um beneficiário específico"
    )
    @ApiResponse(responseCode = "200", description = "Beneficiário encontrado")
    @ApiResponse(responseCode = "404", description = "Beneficiário não encontrado")
    public ResponseEntity<BeneficiarioResponseDTO> buscarPorId(
            @Parameter(description = "ID do beneficiário")
            @PathVariable Long idBeneficiario) {
        return ResponseEntity.ok(service.buscarPorId(idBeneficiario));
    }

    @PutMapping("/{idBeneficiario}")
    @Operation(
            summary = "Atualiza um beneficiário (substituição total)",
            description = "Atualiza todos os dados de um beneficiário existente"
    )
    @ApiResponse(responseCode = "200", description = "Beneficiário atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Beneficiário não encontrado")
    public ResponseEntity<BeneficiarioResponseDTO> atualizar(
            @PathVariable Long idBeneficiario,
            @Valid @RequestBody BeneficiarioRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(idBeneficiario, dto));
    }

    @PatchMapping("/{idBeneficiario}")
    @Operation(
            summary = "Atualiza um beneficiário parcialmente",
            description = "Atualiza campos específicos de um beneficiário existente"
    )
    @ApiResponse(responseCode = "200", description = "Beneficiário atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Beneficiário não encontrado")
    public ResponseEntity<BeneficiarioResponseDTO> atualizarParcial(
            @PathVariable Long idBeneficiario,
            @RequestBody BeneficiarioPatchDTO dto) {
        return ResponseEntity.ok(service.atualizarParcial(idBeneficiario, dto));
    }

    @DeleteMapping("/{idBeneficiario}")
    @Operation(
            summary = "Remove um beneficiário",
            description = "Exclui um beneficiário do sistema pelo ID"
    )
    @ApiResponse(responseCode = "204", description = "Beneficiário removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Beneficiário não encontrado")
    public ResponseEntity<Void> deletar(
            @PathVariable Long idBeneficiario) {
        service.deletar(idBeneficiario);
        return ResponseEntity.noContent().build();
    }
}