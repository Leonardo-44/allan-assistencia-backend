package com.loja.allanassistencia.configuracao.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "configuracao_assistencia")
public class ConfiguracaoAssistencia {

    @Id
    private Long id = 1L; // singleton: sempre id = 1

    private String nomeFantasia;
    private String endereco;
    private String telefone;
    private String corPrimaria = "#1e3a8a";
    private String logoUrl;

    @Column(length = 500)
    private String rodapeTexto;

    public Long getId() {
        return id;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCorPrimaria() {
        return corPrimaria;
    }

    public void setCorPrimaria(String corPrimaria) {
        this.corPrimaria = corPrimaria;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getRodapeTexto() {
        return rodapeTexto;
    }

    public void setRodapeTexto(String rodapeTexto) {
        this.rodapeTexto = rodapeTexto;
    }
}