package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.model.Produto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    // ✅ Listar com filtros opcionais por nome e categoria
    public Page<ProdutoResponseDTO> listar(String nome, CategoriaProduto categoria, Pageable pageable) {
        Page<Produto> page;

        if (nome != null && !nome.isBlank() && categoria != null) {
            page = repository.findByNomeContainingIgnoreCaseAndCategoria(nome, categoria, pageable);
        } else if (nome != null && !nome.isBlank()) {
            page = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else if (categoria != null) {
            page = repository.findByCategoria(categoria, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toDTO);
    }

    // ✅ Buscar por ID
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));
        return toDTO(produto);
    }

    // ✅ Criar (POST)
    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        if (repository.existsByNome(dto.nome())) {
            throw new RuntimeException("Já existe um produto cadastrado com este nome.");
        }

        if (dto.estoqueAtual() < 0 || dto.estoqueMinimo() < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }

        Produto entity = Produto.builder()
                .nome(dto.nome())
                .categoria(dto.categoria())
                .estoqueAtual(dto.estoqueAtual())
                .estoqueMinimo(dto.estoqueMinimo())
                .unidadeMedida(dto.unidadeMedida())
                .build();

        return toDTO(repository.save(entity));
    }

    // ✅ Atualizar total (PUT)
    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));

        // Valida nome único caso o nome tenha sido alterado
        if (!dto.nome().equals(entity.getNome()) && repository.existsByNome(dto.nome())) {
            throw new RuntimeException("Já existe um produto cadastrado com este nome.");
        }

        if (dto.estoqueAtual() < 0 || dto.estoqueMinimo() < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }

        entity.setNome(dto.nome());
        entity.setCategoria(dto.categoria());
        entity.setEstoqueAtual(dto.estoqueAtual());
        entity.setEstoqueMinimo(dto.estoqueMinimo());
        entity.setUnidadeMedida(dto.unidadeMedida());

        return toDTO(repository.save(entity));
    }

    // ✅ Deletar (DELETE)
    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    // Mapeador Entity → Response DTO (calculando o booleano 'estoqueBaixo' da Regra 2)
    private ProdutoResponseDTO toDTO(Produto entity) {
        boolean estoqueBaixo = entity.getEstoqueAtual() < entity.getEstoqueMinimo();

        return new ProdutoResponseDTO(
                entity.getIdProduto(),
                entity.getNome(),
                entity.getCategoria(),
                entity.getEstoqueAtual(),
                entity.getEstoqueMinimo(),
                entity.getUnidadeMedida(),
                entity.getDataCadastro(),
                estoqueBaixo
        );
    }
}