package br.com.socialconnect.api.doadores.service;

import br.com.socialconnect.api.doadores.dto.DoadorPatchDTO;
import br.com.socialconnect.api.doadores.dto.DoadorRequestDTO;
import br.com.socialconnect.api.doadores.dto.DoadorResponseDTO;
import br.com.socialconnect.api.doadores.model.Doador;
import br.com.socialconnect.api.doadores.model.TipoDoador;
import br.com.socialconnect.api.doadores.repository.DoadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoadorService {

    private final DoadorRepository repository;

    // ✅ Listar com filtros opcionais por nome e tipo
    public Page<DoadorResponseDTO> listar(String nome, TipoDoador tipo, Pageable pageable) {
        Page<Doador> page;

        if (nome != null && !nome.isBlank() && tipo != null) {
            page = repository.findByNomeContainingIgnoreCaseAndTipo(nome, tipo, pageable);
        } else if (nome != null && !nome.isBlank()) {
            page = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else if (tipo != null) {
            page = repository.findByTipo(tipo, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toDTO);
    }

    // ✅ Buscar por ID
    public DoadorResponseDTO buscarPorId(Long idDoador) {
        return repository.findById(idDoador)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Doador não encontrado: " + idDoador));
    }

    // ✅ Criar (POST)
    @Transactional
    public DoadorResponseDTO criar(DoadorRequestDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        Doador entity = Doador.builder()
                .nome(dto.nome())
                .tipo(dto.tipo())
                .email(dto.email())
                .telefone(dto.telefone())
                .build();

        return toDTO(repository.save(entity));
    }

    // ✅ Atualizar parcial (PATCH)
    @Transactional
    public DoadorResponseDTO atualizarParcial(Long idDoador, DoadorPatchDTO dto) {
        Doador entity = repository.findById(idDoador)
                .orElseThrow(() -> new RuntimeException("Doador não encontrado: " + idDoador));

        if (dto.nome() != null) entity.setNome(dto.nome());
        if (dto.tipo() != null) entity.setTipo(dto.tipo());
        if (dto.email() != null) {
            if (!dto.email().equals(entity.getEmail()) && repository.existsByEmail(dto.email())) {
                throw new RuntimeException("E-mail já cadastrado");
            }
            entity.setEmail(dto.email());
        }
        if (dto.telefone() != null) entity.setTelefone(dto.telefone());

        return toDTO(repository.save(entity));
    }

    // ✅ Atualizar total (PUT)
    @Transactional
    public DoadorResponseDTO atualizar(Long idDoador, DoadorRequestDTO dto) {
        Doador entity = repository.findById(idDoador)
                .orElseThrow(() -> new RuntimeException("Doador não encontrado: " + idDoador));

        if (!dto.email().equals(entity.getEmail()) && repository.existsByEmail(dto.email())) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        entity.setNome(dto.nome());
        entity.setTipo(dto.tipo());
        entity.setEmail(dto.email());
        entity.setTelefone(dto.telefone());

        return toDTO(repository.save(entity));
    }

    // ✅ Deletar (DELETE)
    @Transactional
    public void deletar(Long idDoador) {
        if (!repository.existsById(idDoador)) {
            throw new RuntimeException("Doador não encontrado: " + idDoador);
        }
        repository.deleteById(idDoador);
    }

    // Mapeador Entity → Response DTO
    private DoadorResponseDTO toDTO(Doador entity) {
        return new DoadorResponseDTO(
                entity.getIdDoador(),
                entity.getNome(),
                entity.getTipo(),
                entity.getEmail(),
                entity.getTelefone()
        );
    }
}