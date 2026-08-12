package com.loja.allanassistencia.venda.controller;

import com.loja.allanassistencia.venda.dto.VendaRequestDTO;
import com.loja.allanassistencia.venda.dto.VendaResponseDTO;
import com.loja.allanassistencia.venda.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @GetMapping
    public List<VendaResponseDTO> listarTodos() {
        return vendaService.listarTodos();
    }

    @GetMapping("/{id}")
    public VendaResponseDTO buscarPorId(
            @PathVariable Long id
    ) {
        return vendaService.buscarPorId(id);
    }

    @PostMapping
    public VendaResponseDTO salvar(
            @Valid @RequestBody VendaRequestDTO dto
    ) {
        return vendaService.salvar(dto);
    }

    @PutMapping("/{id}")
    public VendaResponseDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VendaRequestDTO dto
    ) {
        return vendaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void remover(
            @PathVariable Long id
    ) {
        vendaService.remover(id);
    }
}