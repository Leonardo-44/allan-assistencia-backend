package com.loja.allanassistencia.ordemservico.dto;

import com.loja.allanassistencia.ordemservico.entity.StatusOrdemServico;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrdemServicoResponseDTO(

        Long id,

        Long clienteId,

        String clienteNome,

        String aparelho,

        String imei,

        String defeito,

        String servicoRealizado,

        String peca,

        BigDecimal valor,

        Integer garantiaDias,

        LocalDate garantiaInicio,

        LocalDate garantiaFim,

        StatusOrdemServico status,

        LocalDateTime dataEntrada,

        LocalDateTime dataEntrega
) {
}