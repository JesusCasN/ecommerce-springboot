package com.diplomado.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.diplomado.tienda.model.DetallePedido;


@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}
