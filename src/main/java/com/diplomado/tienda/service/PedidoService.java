package com.diplomado.tienda.service;

import com.diplomado.tienda.dto.ReporteProducto;
import com.diplomado.tienda.model.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface PedidoService {

    Pedido realizarPedido(String nombreUsuario, FormasDePago formaPago, Long idDireccion);

    List<Pedido> filtrarPedidos(Optional<Timestamp> fechaInicio, Optional<Timestamp> fechaFin, Optional<String> usuario);

    Pedido obtenerPedidoPorId(Long id);

    List<ReporteProducto> generarReportePorProducto(Optional<Timestamp> fechaInicio, Optional<Timestamp> fechaFin);

    List<Pedido> obtenerHistorialUsuario(String email, Optional<Timestamp> fechaInicio, Optional<Timestamp> fechaFin);

}
