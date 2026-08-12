package com.loja.allanassistencia.movimentacaofinanceira.service;

import com.loja.allanassistencia.exception.RecursoNaoEncontradoException;
import com.loja.allanassistencia.movimentacaofinanceira.dto.MovimentacaoFinanceiraRequestDTO;
import com.loja.allanassistencia.movimentacaofinanceira.dto.MovimentacaoFinanceiraResponseDTO;
import com.loja.allanassistencia.movimentacaofinanceira.dto.ResumoFinanceiroResponseDTO;
import com.loja.allanassistencia.movimentacaofinanceira.entity.MovimentacaoFinanceira;
import com.loja.allanassistencia.movimentacaofinanceira.entity.TipoMovimentacao;
import com.loja.allanassistencia.movimentacaofinanceira.repository.MovimentacaoFinanceiraRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoFinanceiraService {

    private final MovimentacaoFinanceiraRepository repository;

    public MovimentacaoFinanceiraService(
            MovimentacaoFinanceiraRepository repository
    ) {
        this.repository = repository;
    }

    public List<MovimentacaoFinanceiraResponseDTO> listarTodos() {

        return repository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public MovimentacaoFinanceiraResponseDTO buscarPorId(Long id) {

        MovimentacaoFinanceira movimentacao = repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Movimentação financeira não encontrada."
                        )
                );

        return converterParaResponse(movimentacao);
    }

    public MovimentacaoFinanceiraResponseDTO salvar(
            MovimentacaoFinanceiraRequestDTO dto
    ) {

        MovimentacaoFinanceira movimentacao =
                new MovimentacaoFinanceira();

        movimentacao.setTipo(dto.tipo());
        movimentacao.setDescricao(dto.descricao());
        movimentacao.setValor(dto.valor());
        movimentacao.setFormaPagamento(dto.formaPagamento());
        movimentacao.setDataMovimentacao(LocalDateTime.now());

        MovimentacaoFinanceira salva =
                repository.save(movimentacao);

        return converterParaResponse(salva);
    }

    public MovimentacaoFinanceiraResponseDTO atualizar(
            Long id,
            MovimentacaoFinanceiraRequestDTO dto
    ) {

        MovimentacaoFinanceira movimentacao =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Movimentação financeira não encontrada."
                                )
                        );

        movimentacao.setTipo(dto.tipo());
        movimentacao.setDescricao(dto.descricao());
        movimentacao.setValor(dto.valor());
        movimentacao.setFormaPagamento(dto.formaPagamento());

        MovimentacaoFinanceira atualizada =
                repository.save(movimentacao);

        return converterParaResponse(atualizada);
    }

    public void remover(Long id) {

        MovimentacaoFinanceira movimentacao =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Movimentação financeira não encontrada."
                                )
                        );

        repository.delete(movimentacao);
    }

    public ResumoFinanceiroResponseDTO resumo() {

        BigDecimal totalEntradas =
                repository.somarPorTipo(TipoMovimentacao.ENTRADA);

        BigDecimal totalSaidas =
                repository.somarPorTipo(TipoMovimentacao.SAIDA);

        BigDecimal saldo =
                totalEntradas.subtract(totalSaidas);

        return new ResumoFinanceiroResponseDTO(
                totalEntradas,
                totalSaidas,
                saldo
        );
    }

    private MovimentacaoFinanceiraResponseDTO converterParaResponse(
            MovimentacaoFinanceira movimentacao
    ) {

        return new MovimentacaoFinanceiraResponseDTO(
                movimentacao.getId(),
                movimentacao.getTipo(),
                movimentacao.getDescricao(),
                movimentacao.getValor(),
                movimentacao.getFormaPagamento(),
                movimentacao.getDataMovimentacao()
        );
    }
}