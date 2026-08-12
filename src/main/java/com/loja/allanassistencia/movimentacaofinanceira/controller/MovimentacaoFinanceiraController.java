package com.loja.allanassistencia.movimentacaofinanceira.controller;

import com.loja.allanassistencia.movimentacaofinanceira.dto.MovimentacaoFinanceiraRequestDTO;
import com.loja.allanassistencia.movimentacaofinanceira.dto.MovimentacaoFinanceiraResponseDTO;
import com.loja.allanassistencia.movimentacaofinanceira.dto.ResumoFinanceiroResponseDTO;
import com.loja.allanassistencia.movimentacaofinanceira.service.MovimentacaoFinanceiraService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes-financeiras")
public class MovimentacaoFinanceiraController {

    private final MovimentacaoFinanceiraService service;

    public MovimentacaoFinanceiraController(
            MovimentacaoFinanceiraService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<MovimentacaoFinanceiraResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/resumo")
    public ResumoFinanceiroResponseDTO resumo() {
        return service.resumo();
    }

    @GetMapping("/{id}")
    public MovimentacaoFinanceiraResponseDTO buscarPorId(
            @PathVariable Long id
    ) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public MovimentacaoFinanceiraResponseDTO salvar(
            @Valid @RequestBody MovimentacaoFinanceiraRequestDTO dto
    ) {
        return service.salvar(dto);
    }

    @PutMapping("/{id}")
    public MovimentacaoFinanceiraResponseDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MovimentacaoFinanceiraRequestDTO dto
    ) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}