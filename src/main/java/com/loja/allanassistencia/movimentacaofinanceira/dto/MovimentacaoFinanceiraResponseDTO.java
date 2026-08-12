package com.loja.allanassistencia.movimentacaofinanceira.dto;

import com.loja.allanassistencia.movimentacaofinanceira.entity.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentacaoFinanceiraResponseDTO(

        Long id,

        TipoMovimentacao tipo,

        String descricao,

        BigDecimal valor,

        String formaPagamento,

        LocalDateTime dataMovimentacao

) {
}