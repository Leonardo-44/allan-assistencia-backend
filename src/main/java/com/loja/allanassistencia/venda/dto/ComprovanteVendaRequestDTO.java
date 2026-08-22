package com.loja.allanassistencia.venda.dto;

import java.math.BigDecimal;

public record ComprovanteVendaRequestDTO(
        String aparelho,
        String imei,
        BigDecimal valor,
        BigDecimal valorPago,
        String formaPagamento
) {
}