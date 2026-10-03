package br.com.socialconnect.api;

import br.com.socialconnect.api.TestcontainersConfiguration;
import br.com.socialconnect.api.doacoes.dto.DoacaoRequestDTO;
import br.com.socialconnect.api.doacoes.model.TipoDoacao;
import br.com.socialconnect.api.doacoes.repository.DoacaoRepository;
import br.com.socialconnect.api.doadores.model.Doador;
import br.com.socialconnect.api.doadores.model.TipoDoador;
import br.com.socialconnect.api.doadores.repository.DoadorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.flyway.enabled=false"
        }
)
@Import(TestcontainersConfiguration.class) // Conecta ao Postgres do Testcontainers
class DoacaoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private DoacaoRepository doacaoRepository;

    @Autowired
    private DoadorRepository doadorRepository;

    private Long idDoadorExistente;

    @BeforeEach
    void setUp() {
        doacaoRepository.deleteAll();
        doadorRepository.deleteAll();

        // Cria e salva um doador real para satisfazer a FK e a validação do service
        Doador doador = Doador.builder()
                .nome("Doador Integração")
                .email("doador@teste.com")
                .telefone("11999999999")
                .tipo(TipoDoador.PESSOA_FISICA)
                .build();
        Doador salvo = doadorRepository.save(doador);
        this.idDoadorExistente = salvo.getIdDoador();
    }

    @Test
    @DisplayName("Deve registrar doação com sucesso via POST /api/doacoes")
    void deveRegistrarDoacaoComSucesso() throws Exception {
        DoacaoRequestDTO request = new DoacaoRequestDTO(
                idDoadorExistente,
                LocalDate.now(),
                new BigDecimal("150.00"),
                TipoDoacao.FINANCEIRA,
                "Doação via teste de integração"
        );

        mockMvc.perform(post("/api/v1/doacoes") // <-- adicionado /v1
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/v1/doacoes/")))
                .andExpect(jsonPath("$.idDoacao").isNumber())
                .andExpect(jsonPath("$.idDoador").value(idDoadorExistente))
                .andExpect(jsonPath("$.valor").value(150.00))
                .andExpect(jsonPath("$.tipo").value("FINANCEIRA"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar registrar doação para doador inexistente")
    void deveRetornar404QuandoDoadorNaoExistir() throws Exception {
        DoacaoRequestDTO request = new DoacaoRequestDTO(
                999999L, // Doador inexistente
                LocalDate.now(),
                new BigDecimal("50.00"),
                TipoDoacao.ALIMENTO,
                "Doador fantasma"
        );

        mockMvc.perform(post("/api/v1/doacoes") // <-- adicionado /v1
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve listar doações via GET")
    void deveListarDoacoes() throws Exception {
        mockMvc.perform(get("/api/v1/doacoes") // <-- adicionado /v1
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}