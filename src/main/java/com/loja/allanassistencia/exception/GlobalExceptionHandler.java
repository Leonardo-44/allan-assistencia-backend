package com.loja.allanassistencia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> erros = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> erros.put(error.getField(), error.getDefaultMessage())
                );

        Map<String, Object> resposta = new HashMap<>();

        resposta.put("Status", 400);
        resposta.put("Mensagem", "Dados inválido");
        resposta.put("erros", erros);

        return resposta;
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleRecursoNaoEncontrado(
            RecursoNaoEncontradoException exception
    ) {

        Map<String, Object> resposta = new HashMap<>();

        resposta.put("status", 404);
        resposta.put("mensagem", exception.getMessage());

        return resposta;
    }

    @ExceptionHandler(StatusOrdemServicoInvalidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleStatusInvalido(
            StatusOrdemServicoInvalidoException exception
    ) {

        Map<String, Object> resposta = new HashMap<>();

        resposta.put("status", 400);
        resposta.put("mensagem", exception.getMessage());

        return resposta;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(
            IllegalArgumentException exception
    ) {

        Map<String, Object> resposta = new HashMap<>();

        resposta.put("status", 400);
        resposta.put("mensagem", exception.getMessage());

        return resposta;
    }
}
