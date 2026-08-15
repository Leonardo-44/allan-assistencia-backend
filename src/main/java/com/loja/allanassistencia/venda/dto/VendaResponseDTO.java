package com.loja.allanassistencia.venda.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VendaResponseDTO(

        Long id,

        Long clienteId,

        String aparelho,

        String imei,

        BigDecimal valor,

        BigDecimal valorPago,

        BigDecimal valorRestante,

        String statusPagamento,

        String formaPagamento,

        LocalDateTime dataVenda

) {
}