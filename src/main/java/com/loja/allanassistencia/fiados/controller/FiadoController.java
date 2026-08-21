package com.loja.allanassistencia.fiados.controller;

import com.loja.allanassistencia.fiados.dto.FiadoRequestDTO;
import com.loja.allanassistencia.fiados.dto.FiadoResponseDTO;
import com.loja.allanassistencia.fiados.service.FiadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fiados")
public class FiadoController {

    private final FiadoService fiadoService;

    public FiadoController(FiadoService fiadoService) {
        this.fiadoService = fiadoService;
    }

    @GetMapping
    public ResponseEntity<List<FiadoResponseDTO>> listar() {
        return ResponseEntity.ok(fiadoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FiadoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fiadoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FiadoResponseDTO> criar(@RequestBody FiadoRequestDTO dto) {
        return ResponseEntity.ok(fiadoService.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FiadoResponseDTO> atualizar(@PathVariable Long id, @RequestBody FiadoRequestDTO dto) {
        return ResponseEntity.ok(fiadoService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/pagamento")
    public ResponseEntity<FiadoResponseDTO> alternarPagamento(@PathVariable Long id) {
        return ResponseEntity.ok(fiadoService.alternarPagamento(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        fiadoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}