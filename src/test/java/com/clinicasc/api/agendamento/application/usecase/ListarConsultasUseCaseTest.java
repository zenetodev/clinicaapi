package com.clinicasc.api.agendamento.application.usecase;

import com.clinicasc.api.agendamento.application.dto.ConsultaOutput;
import com.clinicasc.api.agendamento.application.dto.ListarConsultasInput;
import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;
import com.clinicasc.api.agendamento.domain.model.Consulta;
import com.clinicasc.api.agendamento.domain.model.ConsultaId;
import com.clinicasc.api.agendamento.domain.model.ConsultaPage;
import com.clinicasc.api.agendamento.domain.model.PeriodoConsulta;
import com.clinicasc.api.agendamento.domain.model.StatusConsulta;
import com.clinicasc.api.agendamento.domain.repository.ConsultaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarConsultasUseCaseTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @InjectMocks
    private ListarConsultasUseCase useCase;

    @Test
    void deveListarConsultasComFiltros() {
        UUID pacienteId = UUID.randomUUID();
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fim = LocalDateTime.now().plusDays(1);
        Consulta consulta = novaConsulta();
        when(consultaRepository.listar(pacienteId, null, StatusConsulta.AGENDADA.name(), inicio, fim, 0, 10))
            .thenReturn(new ConsultaPage(List.of(consulta), 0, 10, 1));

        var resultado = useCase.executar(
            new ListarConsultasInput(pacienteId, null, StatusConsulta.AGENDADA, inicio, fim, 0, 10)
        );

        assertEquals(1, resultado.consultas().size());
        assertEquals(consulta.getId().valor(), resultado.consultas().get(0).id());
        assertEquals(1, resultado.totalElementos());
        verify(consultaRepository).listar(pacienteId, null, StatusConsulta.AGENDADA.name(), inicio, fim, 0, 10);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverConsultas() {
        when(consultaRepository.listar(null, null, null, null, null, 0, 20))
            .thenReturn(new ConsultaPage(List.of(), 0, 20, 0));

        var resultado = useCase.executar(new ListarConsultasInput(null, null, null, null, null, 0, 20));

        assertEquals(List.of(), resultado.consultas());
        assertEquals(0, resultado.totalPaginas());
    }

    @Test
    void deveRejeitarFiltroComApenasUmaData() {
        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(
            new ListarConsultasInput(null, null, null, LocalDateTime.now(), null, 0, 20)
        ));
    }

    private Consulta novaConsulta() {
        return new Consulta(
                UUID.randomUUID(),
                UUID.randomUUID(),
                new PeriodoConsulta(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1))
        );
    }
}