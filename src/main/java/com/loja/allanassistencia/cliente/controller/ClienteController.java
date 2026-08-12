package com.loja.allanassistencia.cliente.controller;

import com.loja.allanassistencia.cliente.dto.ClienteReponseDTO;
import com.loja.allanassistencia.cliente.dto.ClienteRequestDTO;
import com.loja.allanassistencia.cliente.service.ClienteService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteReponseDTO> listarTodos(){
        return clienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ClienteReponseDTO buscarPorId(@PathVariable Long id){
        return clienteService.buscarPorId(id);
    }

    @PostMapping
    public ClienteReponseDTO salvar(@Valid @RequestBody ClienteRequestDTO dto){
        return clienteService.salvar(dto);
    }

    @PutMapping("/{id}")
    public ClienteReponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto){
        return clienteService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id){
        clienteService.remover(id);
    }

}
