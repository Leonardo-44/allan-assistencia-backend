package com.loja.allanassistencia.ordemservico.dto;

import java.time.LocalDate;

public record GarantiaResponseDTO(
        Long ordemServico,
        boolean emGarantia,
        LocalDate garantiaInicial,
        LocalDate garantiaFim
) {
}
