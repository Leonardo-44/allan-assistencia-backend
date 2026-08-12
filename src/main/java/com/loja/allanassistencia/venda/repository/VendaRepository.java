package com.loja.allanassistencia.venda.repository;

import com.loja.allanassistencia.venda.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}