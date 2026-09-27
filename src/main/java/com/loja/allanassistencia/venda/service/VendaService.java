package com.loja.allanassistencia.venda.service;

import com.loja.allanassistencia.cliente.entity.Cliente;
import com.loja.allanassistencia.cliente.repository.ClienteRepository;
import com.loja.allanassistencia.exception.RecursoNaoEncontradoException;
import com.loja.allanassistencia.movimentacaofinanceira.entity.MovimentacaoFinanceira;
import com.loja.allanassistencia.movimentacaofinanceira.repository.MovimentacaoFinanceiraRepository;
import com.loja.allanassistencia.venda.dto.RegistrarPagamentoDTO;
import com.loja.allanassistencia.venda.dto.VendaRequestDTO;
import com.loja.allanassistencia.venda.dto.VendaResponseDTO;
import com.loja.allanassistencia.venda.entity.Venda;
import com.loja.allanassistencia.venda.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.loja.allanassistencia.movimentacaofinanceira.entity.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class VendaService {

    private static final ZoneId FUSO_BRASIL = ZoneId.of("America/Sao_Paulo");

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

    public VendaService(
            VendaRepository vendaRepository,
            ClienteRepository clienteRepository,
            MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository
    ) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.movimentacaoFinanceiraRepository = movimentacaoFinanceiraRepository;
    }

    public List<VendaResponseDTO> listarTodos() {

        return vendaRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public VendaResponseDTO buscarPorId(Long id) {

        Venda venda = buscarVendaOuFalhar(id);

        return converterParaResponse(venda);
    }

    @Transactional
    public VendaResponseDTO salvar(VendaRequestDTO dto) {

        Cliente cliente = buscarClienteOuNulo(dto.clienteId());

        BigDecimal valorPago = dto.valorPago() != null
                ? dto.valorPago()
                : BigDecimal.ZERO;

        validarValorPago(valorPago, dto.valor());

        Venda venda = new Venda();

        venda.setCliente(cliente);
        venda.setAparelho(dto.aparelho());
        venda.setImei(dto.imei());
        venda.setValor(dto.valor());
        venda.setValorPago(valorPago);
        venda.setFormaPagamento(dto.formaPagamento());
        venda.setDataVenda(LocalDateTime.now(FUSO_BRASIL));

        Venda vendaSalva = vendaRepository.save(venda);

        MovimentacaoFinanceira movimentacao =
                new MovimentacaoFinanceira();

        movimentacao.setTipo(TipoMovimentacao.ENTRADA);
        movimentacao.setDescricao(
                "Venda de " + vendaSalva.getAparelho()
        );
        movimentacao.setValor(vendaSalva.getValor());
        movimentacao.setFormaPagamento(
                vendaSalva.getFormaPagamento()
        );
        movimentacao.setDataMovimentacao(
                LocalDateTime.now(FUSO_BRASIL)
        );

        movimentacaoFinanceiraRepository.save(movimentacao);

        return converterParaResponse(vendaSalva);
    }

    @Transactional
    public VendaResponseDTO atualizar(
            Long id,
            VendaRequestDTO dto
    ) {

        Venda venda = buscarVendaOuFalhar(id);

        Cliente cliente = buscarClienteOuNulo(dto.clienteId());

        BigDecimal valorPago = dto.valorPago() != null
                ? dto.valorPago()
                : BigDecimal.ZERO;

        validarValorPago(valorPago, dto.valor());

        venda.setCliente(cliente);
        venda.setAparelho(dto.aparelho());
        venda.setImei(dto.imei());
        venda.setValor(dto.valor());
        venda.setValorPago(valorPago);
        venda.setFormaPagamento(dto.formaPagamento());

        Venda vendaAtualizada = vendaRepository.save(venda);

        return converterParaResponse(vendaAtualizada);
    }

    @Transactional
    public VendaResponseDTO registrarPagamento(
            Long id,
            RegistrarPagamentoDTO dto
    ) {

        Venda venda = buscarVendaOuFalhar(id);

        BigDecimal novoValorPago =
                venda.getValorPago().add(dto.valor());

        if (novoValorPago.compareTo(venda.getValor()) > 0) {
            novoValorPago = venda.getValor();
        }

        venda.setValorPago(novoValorPago);

        Venda vendaAtualizada = vendaRepository.save(venda);

        return converterParaResponse(vendaAtualizada);
    }

    public void remover(Long id) {

        Venda venda = buscarVendaOuFalhar(id);

        vendaRepository.delete(venda);
    }

    private Venda buscarVendaOuFalhar(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Venda não encontrada."
                        )
                );
    }

    private Cliente buscarClienteOuNulo(Long clienteId) {

        if (clienteId == null) {
            return null;
        }

        return clienteRepository.findById(clienteId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Cliente não encontrado."
                        )
                );
    }

    private void validarValorPago(BigDecimal valorPago, BigDecimal valor) {
        if (valorPago.compareTo(valor) > 0) {
            throw new IllegalArgumentException(
                    "O valor pago não pode ser maior que o valor da venda."
            );
        }
    }

    private VendaResponseDTO converterParaResponse(Venda venda) {

        BigDecimal valorPago = venda.getValorPago() != null
                ? venda.getValorPago()
                : BigDecimal.ZERO;

        BigDecimal valorRestante =
                venda.getValor().subtract(valorPago);

        String statusPagamento;

        if (valorRestante.compareTo(BigDecimal.ZERO) <= 0) {
            statusPagamento = "PAGO";
        } else if (valorPago.compareTo(BigDecimal.ZERO) > 0) {
            statusPagamento = "PARCIAL";
        } else {
            statusPagamento = "PENDENTE";
        }

        return new VendaResponseDTO(
                venda.getId(),
                venda.getCliente() != null
                        ? venda.getCliente().getId()
                        : null,
                venda.getAparelho(),
                venda.getImei(),
                venda.getValor(),
                valorPago,
                valorRestante,
                statusPagamento,
                venda.getFormaPagamento(),
                venda.getDataVenda()
        );
    }
}