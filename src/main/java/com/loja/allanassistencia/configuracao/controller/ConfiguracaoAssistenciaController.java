package com.loja.allanassistencia.configuracao.controller;

import com.loja.allanassistencia.configuracao.dto.ConfiguracaoAssistenciaDTO;
import com.loja.allanassistencia.configuracao.entity.ConfiguracaoAssistencia;
import com.loja.allanassistencia.configuracao.service.ConfiguracaoAssistenciaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuracao")
public class ConfiguracaoAssistenciaController {

    private final ConfiguracaoAssistenciaService service;

    public ConfiguracaoAssistenciaController(ConfiguracaoAssistenciaService service) {
        this.service = service;
    }

    @GetMapping
    public ConfiguracaoAssistencia buscar() {
        return service.buscar();
    }

    @PutMapping
    public ConfiguracaoAssistencia atualizar(@RequestBody ConfiguracaoAssistenciaDTO dto) {
        return service.atualizar(dto);
    }
}