package com.loja.allanassistencia.cliente.repository;

import com.loja.allanassistencia.cliente.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}