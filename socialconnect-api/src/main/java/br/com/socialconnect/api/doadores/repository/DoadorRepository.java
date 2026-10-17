package br.com.socialconnect.api.doadores.repository;

import br.com.socialconnect.api.doadores.model.Doador;
import br.com.socialconnect.api.doadores.model.TipoDoador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoadorRepository extends JpaRepository<Doador, Long> {

    // Verifica duplicação de e-mail no cadastro e atualização
    boolean existsByEmail(String email);

    // Filtro por nome parcial (ignorando maiúsculas/minúsculas)
    Page<Doador> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    // Filtro exato por tipo de doador
    Page<Doador> findByTipo(TipoDoador tipo, Pageable pageable);

    // Filtro combinado: nome parcial e tipo de doador
    Page<Doador> findByNomeContainingIgnoreCaseAndTipo(String nome, TipoDoador tipo, Pageable pageable);
}