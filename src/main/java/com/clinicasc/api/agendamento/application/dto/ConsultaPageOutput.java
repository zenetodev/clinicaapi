package com.clinicasc.api.agendamento.application.dto;

import java.util.List;

public record ConsultaPageOutput(
        List<ConsultaOutput> consultas,
        int pagina,
        int tamanho,
        long totalElementos,
        int totalPaginas
) {
}