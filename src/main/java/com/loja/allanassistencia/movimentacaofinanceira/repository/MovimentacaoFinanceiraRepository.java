package com.loja.allanassistencia.movimentacaofinanceira.repository;

import com.loja.allanassistencia.movimentacaofinanceira.entity.MovimentacaoFinanceira;
import com.loja.allanassistencia.movimentacaofinanceira.entity.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface MovimentacaoFinanceiraRepository
        extends JpaRepository<MovimentacaoFinanceira, Long> {

    @Query("""
        SELECT COALESCE(SUM(m.valor), 0)
        FROM MovimentacaoFinanceira m
        WHERE m.tipo = :tipo
    """)
    BigDecimal somarPorTipo(TipoMovimentacao tipo);
}