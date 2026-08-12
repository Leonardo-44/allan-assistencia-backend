package com.loja.allanassistencia.venda.service;

import com.loja.allanassistencia.cliente.entity.Cliente;
import com.loja.allanassistencia.cliente.repository.ClienteRepository;
import com.loja.allanassistencia.exception.RecursoNaoEncontradoException;
import com.loja.allanassistencia.movimentacaofinanceira.entity.MovimentacaoFinanceira;
import com.loja.allanassistencia.movimentacaofinanceira.repository.MovimentacaoFinanceiraRepository;
import com.loja.allanassistencia.venda.dto.VendaRequestDTO;
import com.loja.allanassistencia.venda.dto.VendaResponseDTO;
import com.loja.allanassistencia.venda.entity.Venda;
import com.loja.allanassistencia.venda.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.loja.allanassistencia.movimentacaofinanceira.entity.TipoMovimentacao;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {

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

        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Venda não encontrada."
                        )
                );

        return converterParaResponse(venda);
    }

    @Transactional
    public VendaResponseDTO salvar(VendaRequestDTO dto) {

        Cliente cliente = null;

        if (dto.clienteId() != null) {
            cliente = clienteRepository.findById(dto.clienteId())
                    .orElseThrow(() ->
                            new RecursoNaoEncontradoException(
                                    "Cliente não encontrado."
                            )
                    );
        }

        Venda venda = new Venda();

        venda.setCliente(cliente);
        venda.setAparelho(dto.aparelho());
        venda.setImei(dto.imei());
        venda.setValor(dto.valor());
        venda.setFormaPagamento(dto.formaPagamento());
        venda.setDataVenda(LocalDateTime.now());

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
                LocalDateTime.now()
        );

        movimentacaoFinanceiraRepository.save(movimentacao);

        return converterParaResponse(vendaSalva);
    }

    public VendaResponseDTO atualizar(
            Long id,
            VendaRequestDTO dto
    ) {

        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Venda não encontrada."
                        )
                );

        Cliente cliente = null;

        if (dto.clienteId() != null) {
            cliente = clienteRepository.findById(dto.clienteId())
                    .orElseThrow(() ->
                            new RecursoNaoEncontradoException(
                                    "Cliente não encontrado."
                            )
                    );
        }

        venda.setCliente(cliente);
        venda.setAparelho(dto.aparelho());
        venda.setImei(dto.imei());
        venda.setValor(dto.valor());
        venda.setFormaPagamento(dto.formaPagamento());

        Venda vendaAtualizada = vendaRepository.save(venda);

        return converterParaResponse(vendaAtualizada);
    }

    public void remover(Long id) {

        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Venda não encontrada."
                        )
                );

        vendaRepository.delete(venda);
    }

    private VendaResponseDTO converterParaResponse(Venda venda) {

        return new VendaResponseDTO(
                venda.getId(),
                venda.getCliente() != null
                        ? venda.getCliente().getId()
                        : null,
                venda.getAparelho(),
                venda.getImei(),
                venda.getValor(),
                venda.getFormaPagamento(),
                venda.getDataVenda()
        );
    }
}
