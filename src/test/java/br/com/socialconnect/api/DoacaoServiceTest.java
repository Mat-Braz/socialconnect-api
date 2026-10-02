package br.com.socialconnect.api;

import br.com.socialconnect.api.doacoes.dto.DoacaoRequestDTO;
import br.com.socialconnect.api.doacoes.dto.DoacaoResponseDTO;
import br.com.socialconnect.api.doacoes.model.Doacao;
import br.com.socialconnect.api.doacoes.model.TipoDoacao;
import br.com.socialconnect.api.doacoes.repository.DoacaoRepository;
import br.com.socialconnect.api.doacoes.service.DoacaoService;
import br.com.socialconnect.api.doadores.repository.DoadorRepository;
import br.com.socialconnect.api.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoacaoServiceTest {

    @Mock
    private DoacaoRepository doacaoRepository;

    @Mock
    private DoadorRepository doadorRepository;

    @InjectMocks
    private DoacaoService doacaoService;

    @Test
    @DisplayName("Deve criar doação quando dados válidos")
    void deveCriarDoacaoQuandoDadosValidos() {
        // ARRANGE
        DoacaoRequestDTO dto = new DoacaoRequestDTO(
                1L,
                LocalDate.of(2026, 9, 11),
                new BigDecimal("100.00"),
                TipoDoacao.ALIMENTO,
                "Doação de teste"
        );

        Doacao doacaoSalva = Doacao.builder()
                .idDoacao(1L)
                .idDoador(dto.idDoador())
                .dataDoacao(dto.dataDoacao())
                .valor(dto.valor())
                .tipo(dto.tipo())
                .descricao(dto.descricao())
                .build();

        when(doadorRepository.existsById(1L)).thenReturn(true);
        when(doacaoRepository.save(any(Doacao.class))).thenReturn(doacaoSalva);

        // ACT
        DoacaoResponseDTO resultado = doacaoService.criar(dto);

        // ASSERT
        assertNotNull(resultado.idDoacao(), "ID da doação não deve ser nulo");
        assertEquals(new BigDecimal("100.00"), resultado.valor());
        assertEquals(1L, resultado.idDoador());
        verify(doadorRepository, times(1)).existsById(1L);
        verify(doacaoRepository, times(1)).save(any(Doacao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando doador não existir")
    void deveLancarExcecaoQuandoDoadorNaoExistir() {
        // ARRANGE
        DoacaoRequestDTO dto = new DoacaoRequestDTO(
                99L,
                LocalDate.of(2026, 9, 11),
                new BigDecimal("50.00"),
                TipoDoacao.FINANCEIRA,
                "Tentativa sem doador"
        );

        when(doadorRepository.existsById(anyLong())).thenReturn(false);

        // ACT & ASSERT
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            doacaoService.criar(dto);
        });

        verify(doadorRepository, times(1)).existsById(99L);
        verify(doacaoRepository, never()).save(any());
    }
}