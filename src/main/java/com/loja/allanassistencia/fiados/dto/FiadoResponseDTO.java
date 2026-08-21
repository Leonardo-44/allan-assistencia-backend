package com.loja.allanassistencia.fiados.dto;

import com.loja.allanassistencia.fiados.entity.Fiado;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FiadoResponseDTO {

    private Long id;
    private String nomeCliente;
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime dataFiado;
    private boolean pago;
    private LocalDateTime dataPagamento;

    public FiadoResponseDTO(Fiado fiado) {
        this.id = fiado.getId();
        this.nomeCliente = fiado.getNomeCliente();
        this.descricao = fiado.getDescricao();
        this.valor = fiado.getValor();
        this.dataFiado = fiado.getDataFiado();
        this.pago = fiado.isPago();
        this.dataPagamento = fiado.getDataPagamento();
    }

    public Long getId() { return id; }
    public String getNomeCliente() { return nomeCliente; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
    public LocalDateTime getDataFiado() { return dataFiado; }
    public boolean isPago() { return pago; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }
}