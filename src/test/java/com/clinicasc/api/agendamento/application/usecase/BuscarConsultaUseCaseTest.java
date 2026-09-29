package com.clinicasc.api.agendamento.application.usecase;

import com.clinicasc.api.agendamento.application.dto.ConsultaOutput;
import com.clinicasc.api.agendamento.domain.model.Consulta;
import com.clinicasc.api.agendamento.domain.model.ConsultaId;
import com.clinicasc.api.agendamento.domain.model.PeriodoConsulta;
import com.clinicasc.api.agendamento.domain.model.StatusConsulta;
import com.clinicasc.api.agendamento.domain.repository.ConsultaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarConsultaUseCaseTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @InjectMocks
    private BuscarConsultaUseCase useCase;

    @Test
    void deveRetornarConsultaQuandoIdExistir() {
        UUID consultaId = UUID.randomUUID();
        Consulta consulta = novaConsulta(consultaId);
        when(consultaRepository.buscarPorId(ConsultaId.de(consultaId))).thenReturn(Optional.of(consulta));

        Optional<ConsultaOutput> resultado = useCase.executar(consultaId);

        assertTrue(resultado.isPresent());
        assertEquals(consultaId, resultado.get().id());
        assertEquals(StatusConsulta.AGENDADA, resultado.get().status());
    }

    @Test
    void deveRetornarVazioQuandoIdNaoExistir() {
        UUID consultaId = UUID.randomUUID();
        when(consultaRepository.buscarPorId(ConsultaId.de(consultaId))).thenReturn(Optional.empty());

        Optional<ConsultaOutput> resultado = useCase.executar(consultaId);

        assertTrue(resultado.isEmpty());
    }

    private Consulta novaConsulta(UUID consultaId) {
        return new Consulta(
                ConsultaId.de(consultaId),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new PeriodoConsulta(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1)),
                StatusConsulta.AGENDADA,
                null
        );
    }
}