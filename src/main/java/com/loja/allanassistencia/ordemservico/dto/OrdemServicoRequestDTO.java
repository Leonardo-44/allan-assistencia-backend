package com.loja.allanassistencia.ordemservico.dto;

import com.loja.allanassistencia.ordemservico.entity.StatusOrdemServico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrdemServicoRequestDTO(

        @NotNull(message = "O cliente é obrigatório.")
        Long clienteId,

        @NotBlank(message = "O aparelho é obrigatório.")
        String aparelho,

        String imei,

        @NotBlank(message = "O defeito é obrigatório.")
        String defeito,

        String servicoRealizado,

        String peca,

        BigDecimal valor,

        Integer garantiaDias,

        StatusOrdemServico status
) {
}