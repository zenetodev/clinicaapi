package com.clinicasc.api.agendamento.infrastructure.rest.controller;

import com.clinicasc.api.agendamento.application.dto.ConsultaOutput;
import com.clinicasc.api.agendamento.application.dto.ConsultaPageOutput;
import com.clinicasc.api.agendamento.application.usecase.AgendarConsultaUseCase;
import com.clinicasc.api.agendamento.application.usecase.BuscarConsultaUseCase;
import com.clinicasc.api.agendamento.application.usecase.CancelarConsultaUseCase;
import com.clinicasc.api.agendamento.application.usecase.ConfirmarConsultaUseCase;
import com.clinicasc.api.agendamento.application.usecase.ListarConsultasUseCase;
import com.clinicasc.api.agendamento.domain.model.StatusConsulta;
import com.clinicasc.api.agendamento.infrastructure.rest.exceptionhandler.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsultaController.class)
@Import(ApiExceptionHandler.class)
class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AgendarConsultaUseCase agendarConsultaUseCase;

    @MockitoBean
    private BuscarConsultaUseCase buscarConsultaUseCase;

    @MockitoBean
    private CancelarConsultaUseCase cancelarConsultaUseCase;

    @MockitoBean
    private ConfirmarConsultaUseCase confirmarConsultaUseCase;

    @MockitoBean
    private ListarConsultasUseCase listarConsultasUseCase;

    @Test
    void deveRetornar201AoAgendarConsulta() throws Exception {
        UUID consultaId = UUID.randomUUID();
        when(agendarConsultaUseCase.executar(any())).thenReturn(novaConsulta(consultaId));

        mockMvc.perform(post("/agendamentos")
                        .contentType("application/json")
                        .content("""
                                {
                                  "pacienteId": "%s",
                                  "dentistaId": "%s",
                                  "dataHoraInicio": "2030-01-10T10:00:00",
                                  "dataHoraFim": "2030-01-10T11:00:00"
                                }
                                """.formatted(UUID.randomUUID(), UUID.randomUUID())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(consultaId.toString()))
                .andExpect(jsonPath("$.status").value("AGENDADA"));
    }

    @Test
    void deveRetornar400QuandoDadosObrigatoriosNaoForemInformados() throws Exception {
        mockMvc.perform(post("/agendamentos")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Dados de entrada inválidos."))
                .andExpect(jsonPath("$.erros.pacienteId").exists())
                .andExpect(jsonPath("$.erros.dataHoraInicio").exists());
    }

    @Test
    void deveRetornar404QuandoConsultaNaoForEncontrada() throws Exception {
        UUID consultaId = UUID.randomUUID();
        when(buscarConsultaUseCase.executar(consultaId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/agendamentos/{consultaId}", consultaId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarConsultasComFiltros() throws Exception {
        UUID pacienteId = UUID.randomUUID();
        UUID dentistaId = UUID.randomUUID();
        when(listarConsultasUseCase.executar(any())).thenReturn(new ConsultaPageOutput(
            List.of(novaConsulta(UUID.randomUUID())), 0, 10, 1, 1
        ));

        mockMvc.perform(get("/agendamentos")
                        .param("pacienteId", pacienteId.toString())
                        .param("dentistaId", dentistaId.toString())
                        .param("status", "AGENDADA")
                        .param("dataHoraInicio", "2030-01-01T00:00:00")
                        .param("dataHoraFim", "2030-01-31T23:59:59")
                        .param("pagina", "0")
                        .param("tamanho", "10"))
                .andExpect(status().isOk())
                    .andExpect(jsonPath("$.consultas[0].status").value("AGENDADA"))
                    .andExpect(jsonPath("$.totalElementos").value(1))
                    .andExpect(jsonPath("$.totalPaginas").value(1));

        verify(listarConsultasUseCase).executar(any());
    }

    private ConsultaOutput novaConsulta(UUID consultaId) {
        return new ConsultaOutput(
                consultaId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2030, 1, 10, 10, 0),
                LocalDateTime.of(2030, 1, 10, 11, 0),
                StatusConsulta.AGENDADA,
                null
        );
    }
}