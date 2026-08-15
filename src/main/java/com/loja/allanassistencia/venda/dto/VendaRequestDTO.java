package com.loja.allanassistencia.venda.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VendaRequestDTO(

        Long clienteId,

        @NotBlank(message = "O aparelho é obrigatório.")
        String aparelho,

        String imei,

        @NotNull(message = "O valor é obrigatório.")
        @DecimalMin(value = "0.0", message = "O valor não pode ser negativo.")
        BigDecimal valor,

        @DecimalMin(value = "0.0", message = "O valor pago não pode ser negativo.")
        BigDecimal valorPago,

        @NotBlank(message = "A forma de pagamento é obrigatória.")
        String formaPagamento

) {
}