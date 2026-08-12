package com.loja.allanassistencia.movimentacaofinanceira.dto;

import com.loja.allanassistencia.movimentacaofinanceira.entity.TipoMovimentacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MovimentacaoFinanceiraRequestDTO(

        @NotNull(message = "O tipo da movimentação é obrigatório.")
        TipoMovimentacao tipo,

        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @NotNull(message = "O valor é obrigatório.")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
        BigDecimal valor,

        String formaPagamento

) {
}