package com.loja.allanassistencia.fiados.dto;

import java.math.BigDecimal;

public class FiadoRequestDTO {

    private String nomeCliente;
    private String descricao;
    private BigDecimal valor;

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}