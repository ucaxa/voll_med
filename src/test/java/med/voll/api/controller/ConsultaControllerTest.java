package med.voll.api.controller;

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


// @SpringBooTest é a anotação pra testar o ocntroller
@SpringBootTest
//a classe @AutoconfigureMockMVC é declarada para que o spring consiga injetar a classe MockMVc
@AutoConfigureMockMvc
//o spring precisa da anotação @AutoConfigureJtesters para injetar a classe JacksonTester
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    //Classe que vai simular requisições utilizandoo padrão MVC
    @Autowired
    private MockMvc mvc;

    //JacksonTeste representa os objetos que devem ser devolvidos e chegam na api;
    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;
    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    //anotação pra injetar um mock de agendar de consultar e não injetar o um objeto real de agenda de consultas
    @MockBean
    private AgendaDeConsultas agendaDeConsultas;

    @Test
    @DisplayName("Deveria devolver htttp código 400 qunado informações inválidas")
    //a anotaçaõ @withMockUser diz pro spring liberar a reuisição simulando como se o usuário estivesse logado
    @WithMockUser
    void agendar_cenario1() throws Exception {
        var response = mvc.perform(post("/consultas"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver htttp código 200 quando informações válidas")
    //a anotaçaõ @withMockUser diz pro spring liberar a reuisição simulando como se o usuário estivesse logado
    @WithMockUser
    void agendar_cenario2() throws Exception {

        var data = LocalDateTime.now().plusHours(1);
        var especalidade = Especialidade.CARDIOLOGIA;
        var dadosDetalhamento= new DadosDetalhamentoConsulta(null,2l,5l,data);

        when(agendaDeConsultas.agendar(any())).
                thenReturn(new DadosDetalhamentoConsulta(null,2l,5l,data));

        var response = mvc.
                perform(
                        post("/consultas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dadosAgendamentoConsultaJson.write(
                                        new DadosAgendamentoConsulta(2l,5l,data,especalidade)
                                ).getJson())
                )
                .andReturn().getResponse();

           assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado= dadosDetalhamentoConsultaJson.write(
                dadosDetalhamento
        ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }


}