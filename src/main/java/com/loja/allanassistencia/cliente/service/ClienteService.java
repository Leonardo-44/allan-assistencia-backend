package com.loja.allanassistencia.cliente.service;

import com.loja.allanassistencia.cliente.dto.ClienteReponseDTO;
import com.loja.allanassistencia.cliente.dto.ClienteRequestDTO;
import com.loja.allanassistencia.exception.RecursoNaoEncontradoException;
import com.loja.allanassistencia.cliente.repository.ClienteRepository;
import com.loja.allanassistencia.cliente.entity.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService( ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteReponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map (cliente -> new ClienteReponseDTO(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getTelefone(),
                        cliente.getEndereco()
                ))
                .toList();
    }

    public ClienteReponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("CLiente não encontrado."));

        return new ClienteReponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTelefone(),
                cliente.getEndereco()
        );
    }

    public ClienteReponseDTO salvar(ClienteRequestDTO dto){
        Cliente cliente = new Cliente(
                dto.nome(),
                dto.telefone(),
                dto.endereco()
        );

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return new ClienteReponseDTO(
                clienteSalvo.getId(),
                clienteSalvo.getNome(),
                clienteSalvo.getTelefone(),
                clienteSalvo.getEndereco()
        );
    }

    public ClienteReponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));

        cliente.setNome(dto.nome());
        cliente.setTelefone(dto.telefone());
        cliente.setEndereco(dto.endereco());

        Cliente clienteAtualizado = clienteRepository.save(cliente);

        return new ClienteReponseDTO(
                clienteAtualizado.getId(),
                clienteAtualizado.getNome(),
                clienteAtualizado.getTelefone(),
                clienteAtualizado.getEndereco()
        );
    }

    public void remover(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));

        clienteRepository.delete(cliente);
    }
}
