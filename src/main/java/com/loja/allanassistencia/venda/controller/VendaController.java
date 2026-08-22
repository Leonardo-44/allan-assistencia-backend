package com.loja.allanassistencia.venda.controller;

import com.loja.allanassistencia.venda.dto.ComprovanteVendaRequestDTO;
import com.loja.allanassistencia.venda.dto.RegistrarPagamentoDTO;
import com.loja.allanassistencia.venda.dto.VendaRequestDTO;
import com.loja.allanassistencia.venda.dto.VendaResponseDTO;
import com.loja.allanassistencia.venda.service.ComprovanteVendaPdfService;
import com.loja.allanassistencia.venda.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;
    private final ComprovanteVendaPdfService comprovanteVendaPdfService;

    public VendaController(
            VendaService vendaService,
            ComprovanteVendaPdfService comprovanteVendaPdfService
    ) {
        this.vendaService = vendaService;
        this.comprovanteVendaPdfService = comprovanteVendaPdfService;
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

    @PatchMapping("/{id}/pagamento")
    public VendaResponseDTO registrarPagamento(
            @PathVariable Long id,
            @Valid @RequestBody RegistrarPagamentoDTO dto
    ) {
        return vendaService.registrarPagamento(id, dto);
    }

    @DeleteMapping("/{id}")
    public void remover(
            @PathVariable Long id
    ) {
        vendaService.remover(id);
    }

    @PostMapping("/{id}/comprovante")
    public ResponseEntity<byte[]> gerarComprovante(
            @PathVariable Long id,
            @RequestBody(required = false) ComprovanteVendaRequestDTO dto
    ) {
        byte[] pdf = comprovanteVendaPdfService.gerarPdf(id, dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=comprovante-venda-" + id + ".pdf")
                .body(pdf);
    }
}