package com.loja.allanassistencia.ordemservico.service;

import com.loja.allanassistencia.cliente.entity.Cliente;
import com.loja.allanassistencia.cliente.repository.ClienteRepository;
import com.loja.allanassistencia.exception.RecursoNaoEncontradoException;
import com.loja.allanassistencia.exception.StatusOrdemServicoInvalidoException;
import com.loja.allanassistencia.movimentacaofinanceira.entity.MovimentacaoFinanceira;
import com.loja.allanassistencia.movimentacaofinanceira.entity.TipoMovimentacao;
import com.loja.allanassistencia.movimentacaofinanceira.repository.MovimentacaoFinanceiraRepository;
import com.loja.allanassistencia.ordemservico.dto.GarantiaResponseDTO;
import com.loja.allanassistencia.ordemservico.dto.OrdemServicoRequestDTO;
import com.loja.allanassistencia.ordemservico.dto.OrdemServicoResponseDTO;
import com.loja.allanassistencia.ordemservico.entity.OrdemServico;
import com.loja.allanassistencia.ordemservico.entity.StatusOrdemServico;
import com.loja.allanassistencia.ordemservico.repository.OrdemServicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final ClienteRepository clienteRepository;
    private final MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

    public OrdemServicoService(
            OrdemServicoRepository ordemServicoRepository,
            ClienteRepository clienteRepository,
            MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository
    ) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.clienteRepository = clienteRepository;
        this.movimentacaoFinanceiraRepository = movimentacaoFinanceiraRepository;
    }

    public List<OrdemServicoResponseDTO> listarTodos() {

        return ordemServicoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public OrdemServicoResponseDTO buscarPorId(Long id) {

        OrdemServico ordemServico = ordemServicoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Ordem de serviço não encontrada."
                        )
                );

        return converterParaResponse(ordemServico);
    }

    public OrdemServicoResponseDTO salvar(OrdemServicoRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Cliente não encontrado."
                        )
                );

        OrdemServico ordemServico = new OrdemServico();

        ordemServico.setCliente(cliente);
        ordemServico.setAparelho(dto.aparelho());
        ordemServico.setImei(dto.imei());
        ordemServico.setDefeito(dto.defeito());
        ordemServico.setServicoRealizado(dto.servicoRealizado());
        ordemServico.setPeca(dto.peca());

        ordemServico.setValor(
                dto.valor() != null
                        ? dto.valor()
                        : BigDecimal.ZERO
        );

        ordemServico.setGarantiaDias(
                dto.garantiaDias() != null
                        ? dto.garantiaDias()
                        : 0
        );

        if (ordemServico.getGarantiaDias() > 0) {

            LocalDate inicio = LocalDate.now();

            ordemServico.setGarantiaInicio(inicio);

            ordemServico.setGarantiaFim(
                    inicio.plusDays(ordemServico.getGarantiaDias())
            );
        }

        ordemServico.setStatus(
                dto.status() != null
                        ? dto.status()
                        : StatusOrdemServico.ABERTA
        );

        ordemServico.setDataEntrada(LocalDateTime.now());

        OrdemServico ordemSalva =
                ordemServicoRepository.save(ordemServico);

        return converterParaResponse(ordemSalva);
    }

    @Transactional
    public OrdemServicoResponseDTO atualizar(
            Long id,
            OrdemServicoRequestDTO dto
    ) {

        OrdemServico ordemServico =
                ordemServicoRepository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Ordem de serviço não encontrada."
                                )
                        );

        Cliente cliente =
                clienteRepository.findById(dto.clienteId())
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Cliente não encontrado."
                                )
                        );

        ordemServico.setCliente(cliente);
        ordemServico.setAparelho(dto.aparelho());
        ordemServico.setImei(dto.imei());
        ordemServico.setDefeito(dto.defeito());
        ordemServico.setServicoRealizado(dto.servicoRealizado());
        ordemServico.setPeca(dto.peca());

        ordemServico.setValor(
                dto.valor() != null
                        ? dto.valor()
                        : BigDecimal.ZERO
        );

        ordemServico.setGarantiaDias(
                dto.garantiaDias() != null
                        ? dto.garantiaDias()
                        : 0
        );

        if (ordemServico.getGarantiaDias() > 0) {

            LocalDate inicio = LocalDate.now();

            ordemServico.setGarantiaInicio(inicio);

            ordemServico.setGarantiaFim(
                    inicio.plusDays(ordemServico.getGarantiaDias())
            );

        } else {

            ordemServico.setGarantiaInicio(null);
            ordemServico.setGarantiaFim(null);
        }

        if (dto.status() != null) {

            StatusOrdemServico statusAtual =
                    ordemServico.getStatus();

            StatusOrdemServico novoStatus =
                    dto.status();

            if (!statusAtual.podeIrPara(novoStatus)) {

                throw new StatusOrdemServicoInvalidoException(
                        "Não é possível alterar o status de "
                                + statusAtual
                                + " para "
                                + novoStatus
                );
            }

            ordemServico.setStatus(novoStatus);

            /*
             * Quando a ordem passa de PRONTA para ENTREGUE,
             * significa que o serviço foi finalizado e entregue
             * ao cliente.
             *
             * Nesse momento criamos automaticamente uma
             * entrada financeira.
             */
            if (statusAtual == StatusOrdemServico.PRONTA
                    && novoStatus == StatusOrdemServico.ENTREGUE) {

                ordemServico.setDataEntrega(LocalDateTime.now());

                MovimentacaoFinanceira movimentacao =
                        new MovimentacaoFinanceira();

                movimentacao.setTipo(TipoMovimentacao.ENTRADA);

                movimentacao.setDescricao(
                        "Serviço realizado - "
                                + ordemServico.getAparelho()
                );

                movimentacao.setValor(
                        ordemServico.getValor()
                );

                movimentacao.setFormaPagamento(null);

                movimentacao.setDataMovimentacao(
                        LocalDateTime.now()
                );

                movimentacaoFinanceiraRepository.save(
                        movimentacao
                );
            }
        }

        OrdemServico ordemAtualizada =
                ordemServicoRepository.save(ordemServico);

        return converterParaResponse(ordemAtualizada);
    }

    public void remover(Long id) {

        OrdemServico ordemServico =
                ordemServicoRepository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Ordem de serviço não encontrada."
                                )
                        );

        ordemServicoRepository.delete(ordemServico);
    }

    public GarantiaResponseDTO verificarGarantia(Long id) {

        OrdemServico ordemServico =
                ordemServicoRepository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Ordem de serviço não encontrada."
                                )
                        );

        LocalDate hoje = LocalDate.now();

        boolean emGarantia =
                ordemServico.getGarantiaInicio() != null
                        && ordemServico.getGarantiaFim() != null
                        && !hoje.isBefore(
                        ordemServico.getGarantiaInicio()
                )
                        && !hoje.isAfter(
                        ordemServico.getGarantiaFim()
                );

        return new GarantiaResponseDTO(
                ordemServico.getId(),
                emGarantia,
                ordemServico.getGarantiaInicio(),
                ordemServico.getGarantiaFim()
        );
    }

    private OrdemServicoResponseDTO converterParaResponse(
            OrdemServico ordemServico
    ) {

        return new OrdemServicoResponseDTO(
                ordemServico.getId(),
                ordemServico.getCliente().getId(),
                ordemServico.getCliente().getNome(),
                ordemServico.getAparelho(),
                ordemServico.getImei(),
                ordemServico.getDefeito(),
                ordemServico.getServicoRealizado(),
                ordemServico.getPeca(),
                ordemServico.getValor(),
                ordemServico.getGarantiaDias(),
                ordemServico.getGarantiaInicio(),
                ordemServico.getGarantiaFim(),
                ordemServico.getStatus(),
                ordemServico.getDataEntrada(),
                ordemServico.getDataEntrega()
        );
    }
}

