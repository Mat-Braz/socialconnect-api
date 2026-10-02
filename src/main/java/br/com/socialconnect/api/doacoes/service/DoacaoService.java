package br.com.socialconnect.api.doacoes.service;

import br.com.socialconnect.api.doacoes.dto.DoacaoPatchDTO;
import br.com.socialconnect.api.doacoes.dto.DoacaoRequestDTO;
import br.com.socialconnect.api.doacoes.dto.DoacaoResponseDTO;
import br.com.socialconnect.api.doacoes.model.Doacao;
import br.com.socialconnect.api.doacoes.model.TipoDoacao;
import br.com.socialconnect.api.doacoes.repository.DoacaoRepository;
import br.com.socialconnect.api.doadores.repository.DoadorRepository;
import br.com.socialconnect.api.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DoacaoService {

    private final DoacaoRepository repository;
    private final DoadorRepository doadorRepository;

    public Page<DoacaoResponseDTO> listar(
            LocalDate dataInicio, LocalDate dataFim, TipoDoacao tipo, Pageable pageable) {

        Page<Doacao> page;

        if (dataInicio != null && dataFim != null && tipo != null) {
            page = repository.findByDataDoacaoBetweenAndTipo(dataInicio, dataFim, tipo, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toDTO);
    }

    public DoacaoResponseDTO buscarPorId(Long idDoacao) {
        return repository.findById(idDoacao)
                .map(this::toDTO)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Doação não encontrada com ID: " + idDoacao));
    }

    @Transactional
    public DoacaoResponseDTO criar(DoacaoRequestDTO dto) {
        validarExistenciaDoador(dto.idDoador());

        Doacao entity = Doacao.builder()
                .idDoador(dto.idDoador())
                .dataDoacao(dto.dataDoacao())
                .valor(dto.valor())
                .tipo(dto.tipo())
                .descricao(dto.descricao())
                .build();

        return toDTO(repository.save(entity));
    }

    @Transactional
    public DoacaoResponseDTO atualizar(Long idDoacao, DoacaoRequestDTO dto) {
        Doacao entity = repository.findById(idDoacao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Doação não encontrada com ID: " + idDoacao));

        validarExistenciaDoador(dto.idDoador());

        entity.setIdDoador(dto.idDoador());
        entity.setDataDoacao(dto.dataDoacao());
        entity.setValor(dto.valor());
        entity.setTipo(dto.tipo());
        entity.setDescricao(dto.descricao());

        return toDTO(repository.save(entity));
    }

    @Transactional
    public DoacaoResponseDTO atualizarParcial(Long idDoacao, DoacaoPatchDTO dto) {
        Doacao entity = repository.findById(idDoacao)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Doação não encontrada com ID: " + idDoacao));

        if (dto.idDoador() != null) {
            validarExistenciaDoador(dto.idDoador());
            entity.setIdDoador(dto.idDoador());
        }

        if (dto.dataDoacao() != null) entity.setDataDoacao(dto.dataDoacao());
        if (dto.valor() != null) entity.setValor(dto.valor());
        if (dto.tipo() != null) entity.setTipo(dto.tipo());
        if (dto.descricao() != null) entity.setDescricao(dto.descricao());

        return toDTO(repository.save(entity));
    }

    @Transactional
    public void deletar(Long idDoacao) {
        if (!repository.existsById(idDoacao)) {
            throw new RecursoNaoEncontradoException("Doação não encontrada com ID: " + idDoacao);
        }
        repository.deleteById(idDoacao);
    }

    private void validarExistenciaDoador(Long idDoador) {
        if (!doadorRepository.existsById(idDoador)) {
            throw new RecursoNaoEncontradoException("Doador não encontrado com ID: " + idDoador);
        }
    }

    private DoacaoResponseDTO toDTO(Doacao entity) {
        return new DoacaoResponseDTO(
                entity.getIdDoacao(),
                entity.getIdDoador(),
                entity.getDataDoacao(),
                entity.getValor(),
                entity.getTipo(),
                entity.getDescricao()
        );
    }
}