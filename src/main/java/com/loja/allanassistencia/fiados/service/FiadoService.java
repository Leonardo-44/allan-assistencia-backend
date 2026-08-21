package com.loja.allanassistencia.fiados.service;

import com.loja.allanassistencia.fiados.dto.FiadoRequestDTO;
import com.loja.allanassistencia.fiados.dto.FiadoResponseDTO;
import com.loja.allanassistencia.fiados.entity.Fiado;
import com.loja.allanassistencia.fiados.repository.FiadoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FiadoService {

    private final FiadoRepository fiadoRepository;

    public FiadoService(FiadoRepository fiadoRepository) {
        this.fiadoRepository = fiadoRepository;
    }

    public List<FiadoResponseDTO> listarTodos() {
        return fiadoRepository.findAll()
                .stream()
                .map(FiadoResponseDTO::new)
                .toList();
    }

    public FiadoResponseDTO buscarPorId(Long id) {
        Fiado fiado = fiadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fiado não encontrado"));
        return new FiadoResponseDTO(fiado);
    }

    public FiadoResponseDTO criar(FiadoRequestDTO dto) {
        Fiado fiado = new Fiado();
        fiado.setNomeCliente(dto.getNomeCliente());
        fiado.setDescricao(dto.getDescricao());
        fiado.setValor(dto.getValor());
        fiado.setDataFiado(LocalDateTime.now());
        fiado.setPago(false);

        return new FiadoResponseDTO(fiadoRepository.save(fiado));
    }

    public FiadoResponseDTO atualizar(Long id, FiadoRequestDTO dto) {
        Fiado fiado = fiadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fiado não encontrado"));

        fiado.setNomeCliente(dto.getNomeCliente());
        fiado.setDescricao(dto.getDescricao());
        fiado.setValor(dto.getValor());

        return new FiadoResponseDTO(fiadoRepository.save(fiado));
    }

    public FiadoResponseDTO alternarPagamento(Long id) {
        Fiado fiado = fiadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fiado não encontrado"));

        boolean novoStatus = !fiado.isPago();
        fiado.setPago(novoStatus);
        fiado.setDataPagamento(novoStatus ? LocalDateTime.now() : null);

        return new FiadoResponseDTO(fiadoRepository.save(fiado));
    }

    public void excluir(Long id) {
        if (!fiadoRepository.existsById(id)) {
            throw new RuntimeException("Fiado não encontrado");
        }
        fiadoRepository.deleteById(id);
    }
}