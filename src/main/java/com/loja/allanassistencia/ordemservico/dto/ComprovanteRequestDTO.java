package com.loja.allanassistencia.ordemservico.dto;

import java.math.BigDecimal;

public record ComprovanteRequestDTO(
        String nomeProduto,
        String nomeCliente,
        String servicoRealizado,
        BigDecimal valor,
        Integer garantiaDias,
        String imei
) {
}