package com.clinicasc.api.agendamento.domain.model;

import java.util.List;

public record ConsultaPage(List<Consulta> consultas, int pagina, int tamanho, long totalElementos) {

    public int totalPaginas() {
        return (int) Math.ceil((double) totalElementos / tamanho);
    }
}