package com.loja.allanassistencia.ordemservico.entity;

public enum StatusOrdemServico {

    ABERTA,
    EM_ANALISE,
    AGUARDANDO_PECA,
    EM_MANUTENCAO,
    PRONTA,
    CONCLUIDA,
    ENTREGUE;

    public boolean podeIrPara(StatusOrdemServico novoStatus) {

        return switch (this) {

            case ABERTA ->
                    novoStatus == EM_ANALISE;

            case EM_ANALISE ->
                    novoStatus == AGUARDANDO_PECA
                            || novoStatus == EM_MANUTENCAO;

            case AGUARDANDO_PECA ->
                    novoStatus == EM_MANUTENCAO;

            case EM_MANUTENCAO ->
                    novoStatus == PRONTA;

            case PRONTA ->
                    novoStatus == CONCLUIDA;

            case CONCLUIDA ->
                    novoStatus == ENTREGUE;

            case ENTREGUE ->
                    false;
        };
    }

}
