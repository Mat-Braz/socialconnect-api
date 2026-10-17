package br.com.socialconnect.api;

import br.com.socialconnect.api.beneficiarios.repository.BeneficiarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BeneficiarioIntegrationTest {
    private static final String ORIGIN = "http://localhost:5173";
    private static final String BODY = """
            {"nome":"Maria da Silva","cpf":"52998224725", "telefone":"11999999999",
             "endereco":"Rua das Flores, 10"}
            """;
    @Autowired MockMvc mvc;
    @Autowired BeneficiarioRepository repository;

    @BeforeEach
    void limpar() { repository.deleteAll(); }

    @Test
    void cadastroRetorna201EListagemPaginadaContemNovoBeneficiario() throws Exception {
        mvc.perform(post("/api/v1/beneficiarios").header("Origin", ORIGIN)
                        .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Access-Control-Allow-Origin", ORIGIN))
                .andExpect(header().string("Location", containsString("/api/v1/beneficiarios/")))
                .andExpect(jsonPath("$.idBeneficiario").isNumber())
                .andExpect(jsonPath("$.dataCadastro").exists());
        mvc.perform(get("/api/v1/beneficiarios").param("page", "0").param("size", "10")
                        .header("Origin", ORIGIN))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", ORIGIN))
                .andExpect(jsonPath("$.content[0].nome").value("Maria da Silva"))
                .andExpect(jsonPath("$.content[0].cpf").value("52998224725"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void rejeitaCpfDuplicadoCom409() throws Exception {
        mvc.perform(post("/api/v1/beneficiarios").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/v1/beneficiarios").header("Origin", ORIGIN)
                        .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isConflict())
                .andExpect(header().string("Access-Control-Allow-Origin", ORIGIN))
                .andExpect(jsonPath("$.detail").value(containsString("CPF já cadastrado")));
    }

    @Test
    void retornaErrosPorCampoParaCadastroInvalido() throws Exception {
        mvc.perform(post("/api/v1/beneficiarios").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\",\"cpf\":\"123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    void paginaVaziaMantemContratoDoFrontend() throws Exception {
        mvc.perform(get("/api/v1/beneficiarios").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void preflightPermitePostJsonDoFrontend() throws Exception {
        mvc.perform(options("/api/v1/beneficiarios").header("Origin", ORIGIN)
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", ORIGIN))
                .andExpect(header().string("Access-Control-Allow-Methods", containsString("POST")))
                .andExpect(header().string("Access-Control-Allow-Headers", containsString("content-type")))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }

    @Test
    void naoPermiteOrigemDesconhecida() throws Exception {
        mvc.perform(options("/api/v1/beneficiarios").header("Origin", "http://example.invalid")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }
}
