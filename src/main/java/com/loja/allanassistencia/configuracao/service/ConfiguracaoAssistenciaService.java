package com.loja.allanassistencia.configuracao.service;

import com.loja.allanassistencia.configuracao.dto.ConfiguracaoAssistenciaDTO;
import com.loja.allanassistencia.configuracao.entity.ConfiguracaoAssistencia;
import com.loja.allanassistencia.configuracao.repository.ConfiguracaoAssistenciaRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracaoAssistenciaService {

    private final ConfiguracaoAssistenciaRepository repository;

    public ConfiguracaoAssistenciaService(ConfiguracaoAssistenciaRepository repository) {
        this.repository = repository;
    }

    public ConfiguracaoAssistencia buscar() {
        return repository.findById(1L).orElseGet(() -> {
            ConfiguracaoAssistencia nova = new ConfiguracaoAssistencia();
            return repository.save(nova);
        });
    }

    public ConfiguracaoAssistencia atualizar(ConfiguracaoAssistenciaDTO dto) {
        ConfiguracaoAssistencia config = buscar();
        config.setNomeFantasia(dto.nomeFantasia());
        config.setEndereco(dto.endereco());
        config.setTelefone(dto.telefone());
        config.setCorPrimaria(dto.corPrimaria());
        config.setLogoUrl(dto.logoUrl());
        config.setRodapeTexto(dto.rodapeTexto());
        return repository.save(config);
    }
}