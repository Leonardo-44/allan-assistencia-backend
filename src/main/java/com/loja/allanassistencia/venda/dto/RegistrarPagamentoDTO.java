package com.loja.allanassistencia.venda.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RegistrarPagamentoDTO(

        @NotNull(message = "O valor do pagamento é obrigatório.")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
        BigDecimal valor

) {
}