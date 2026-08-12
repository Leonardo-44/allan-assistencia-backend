package com.loja.allanassistencia.ordemservico.controller;

import com.loja.allanassistencia.ordemservico.dto.GarantiaResponseDTO;
import com.loja.allanassistencia.ordemservico.dto.OrdemServicoRequestDTO;
import com.loja.allanassistencia.ordemservico.dto.OrdemServicoResponseDTO;
import com.loja.allanassistencia.ordemservico.service.OrdemServicoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    public OrdemServicoController(
            OrdemServicoService ordemServicoService
    ) {
        this.ordemServicoService = ordemServicoService;
    }

    @GetMapping
    public List<OrdemServicoResponseDTO> listarTodos() {
        return ordemServicoService.listarTodos();
    }

    @GetMapping("/{id}")
    public OrdemServicoResponseDTO buscarPorId(
            @PathVariable Long id
    ) {
        return ordemServicoService.buscarPorId(id);
    }

    @PostMapping
    public OrdemServicoResponseDTO salvar(
            @Valid @RequestBody OrdemServicoRequestDTO dto
    ) {
        return ordemServicoService.salvar(dto);
    }

    @PutMapping("/{id}")
    public OrdemServicoResponseDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OrdemServicoRequestDTO dto
    ) {
        return ordemServicoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        ordemServicoService.remover(id);
    }

    @GetMapping("/{id}/garantia")
    public GarantiaResponseDTO verificarGarantia(
            @PathVariable Long id
    ) {
        return ordemServicoService.verificarGarantia(id);
    }

}