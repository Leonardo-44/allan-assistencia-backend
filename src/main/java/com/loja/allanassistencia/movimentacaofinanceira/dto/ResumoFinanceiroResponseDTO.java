package com.loja.allanassistencia.movimentacaofinanceira.dto;

import java.math.BigDecimal;

public record ResumoFinanceiroResponseDTO(
        BigDecimal totalEntradas,
        BigDecimal totalSaidas,
        BigDecimal saldo
) {
}