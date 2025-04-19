package com.diplomado.tienda.repository;

import com.diplomado.tienda.model.DetallePedido;
import com.diplomado.tienda.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByFechaBetween(Timestamp fechaInicio, Timestamp fechaFin);

    List<Pedido> findByFechaBetweenAndUsuario_Usuario(Timestamp fechaInicio, Timestamp fechaFin, String usuario);

    List<Pedido> findByUsuario_Usuario(String usuario);

    // Consulta personalizada para obtener los productos vendidos en un rango de fechas
    @Query("SELECT dp FROM DetallePedido dp JOIN dp.pedido p WHERE p.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<DetallePedido> findVentasPorProducto(@Param("fechaInicio") Timestamp fechaInicio,
                                              @Param("fechaFin") Timestamp fechaFin);

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detalles WHERE p.id_pedido = :id")
    Optional<Pedido> findPedidoWithDetalles(Long id);

    List<Pedido> findByUsuario_Email(String email);

    List<Pedido> findByFechaBetweenAndUsuario_Email(Timestamp fechaInicio, Timestamp fechaFin, String email);


}
