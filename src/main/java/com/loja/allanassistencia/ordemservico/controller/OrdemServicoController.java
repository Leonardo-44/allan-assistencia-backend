package com.loja.allanassistencia.ordemservico.controller;

import com.loja.allanassistencia.ordemservico.dto.ComprovanteRequestDTO;
import com.loja.allanassistencia.ordemservico.dto.GarantiaResponseDTO;
import com.loja.allanassistencia.ordemservico.dto.OrdemServicoRequestDTO;
import com.loja.allanassistencia.ordemservico.dto.OrdemServicoResponseDTO;
import com.loja.allanassistencia.ordemservico.service.ComprovantePdfService;
import com.loja.allanassistencia.ordemservico.service.OrdemServicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;
    private final ComprovantePdfService comprovantePdfService;

    public OrdemServicoController(
            OrdemServicoService ordemServicoService,
            ComprovantePdfService comprovantePdfService
    ) {
        this.ordemServicoService = ordemServicoService;
        this.comprovantePdfService = comprovantePdfService;
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

    @PostMapping("/{id}/comprovante-pdf")
    public ResponseEntity<byte[]> gerarComprovantePdf(
            @PathVariable Long id,
            @RequestBody(required = false) ComprovanteRequestDTO dto
    ) {
        byte[] pdf = comprovantePdfService.gerarPdf(id, dto);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=comprovante-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}