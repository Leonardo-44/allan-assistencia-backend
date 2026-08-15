package com.loja.allanassistencia.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDTO(
        @NotBlank(message="O nome é obrigatório.")
        String nome,

        @NotBlank(message="O telefone é obrigatório.")
        String telefone,

        @Email(message = "Informe um email válido.")
        String email) {
}
