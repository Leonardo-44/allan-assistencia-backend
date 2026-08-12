package com.loja.allanassistencia.ordemservico.entity;

import com.loja.allanassistencia.cliente.entity.Cliente;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordens_servico")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String aparelho;

    @Column(length = 20)
    private String imei;

    @Column(nullable = false)
    private String defeito;

    @Column(name = "servico_realizado")
    private String servicoRealizado;

    @Column(length = 150)
    private String peca;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(name = "garantia_dias", nullable = false)
    private Integer garantiaDias = 0;

    @Column(name = "garantia_inicio")
    private LocalDate garantiaInicio;

    @Column(name = "garantia_fim")
    private LocalDate garantiaFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusOrdemServico status = StatusOrdemServico.ABERTA;

    @Column(name = "data_entrada")
    private LocalDateTime dataEntrada;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public OrdemServico() {
    }

    public Long getId() {
        return id;
    }

    public String getAparelho() {
        return aparelho;
    }

    public void setAparelho(String aparelho) {
        this.aparelho = aparelho;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDefeito() {
        return defeito;
    }

    public void setDefeito(String defeito) {
        this.defeito = defeito;
    }

    public String getServicoRealizado() {
        return servicoRealizado;
    }

    public void setServicoRealizado(String servicoRealizado) {
        this.servicoRealizado = servicoRealizado;
    }

    public String getPeca() {
        return peca;
    }

    public void setPeca(String peca) {
        this.peca = peca;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getGarantiaDias() {
        return garantiaDias;
    }

    public void setGarantiaDias(Integer garantiaDias) {
        this.garantiaDias = garantiaDias;
    }

    public LocalDate getGarantiaInicio() {
        return garantiaInicio;
    }

    public void setGarantiaInicio(LocalDate garantiaInicio) {
        this.garantiaInicio = garantiaInicio;
    }

    public LocalDate getGarantiaFim() {
        return garantiaFim;
    }

    public void setGarantiaFim(LocalDate garantiaFim) {
        this.garantiaFim = garantiaFim;
    }

    public StatusOrdemServico getStatus() {
        return status;
    }

    public void setStatus(StatusOrdemServico status) {
        this.status = status;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}