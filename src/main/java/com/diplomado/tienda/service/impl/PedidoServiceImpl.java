package com.diplomado.tienda.service.impl;

import com.diplomado.tienda.dto.ReporteProducto;
import com.diplomado.tienda.exception.DireccionNoEncontradaException;
import com.diplomado.tienda.exception.PedidoNoEncontradoException;
import com.diplomado.tienda.factory.PedidoFactory;
import com.diplomado.tienda.model.*;
import com.diplomado.tienda.repository.PedidoRepository;
import com.diplomado.tienda.service.CarritoService;
import com.diplomado.tienda.service.DireccionService;
import com.diplomado.tienda.service.PedidoService;
import com.diplomado.tienda.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarritoService carritoService;
    private final UsuarioService usuarioService;
    private final DireccionService direccionService;
    private final PagoPorDeposito pagoPorDeposito;
    private final PedidoFactory pedidoFactory;

    @Override
    @Transactional
    public Pedido realizarPedido(String nombreUsuario, FormasDePago formaPago, Long idDireccion) {
        log.info("\n 🛒 Iniciando proceso de pedido para el usuario '{}' \n", nombreUsuario);

        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(nombreUsuario);
        List<Carrito> productosEnCarrito = carritoService.obtenerProductosEnCarrito(nombreUsuario);

        if (productosEnCarrito.isEmpty()) {
            log.warn("\n ⚠️ El carrito del usuario '{}' está vacío. No se puede generar el pedido. \n", nombreUsuario);
            throw new IllegalArgumentException("El carrito está vacío. No se puede proceder con el pedido.");
        }

        Direccion direccion = direccionService.obtenerDireccionPorId(idDireccion)
                .filter(d -> d.getCondicion() == EstadoDireccion.ACTIVO)
                .orElseThrow(() -> {
                    log.warn("\n ❌ Dirección inválida o eliminada (ID={}) \n", idDireccion);
                    return new DireccionNoEncontradaException("Dirección no válida o eliminada.");
                });

        double total = productosEnCarrito.stream()
                .mapToDouble(item -> item.getProducto().getPrecio().doubleValue() * item.getCantidad())
                .sum();

        log.info("\n 🧾 Total del pedido para usuario '{}': ${} \n", nombreUsuario, total);

        Pedido pedido = pedidoFactory.crearPedido(usuario, formaPago, direccion, total, productosEnCarrito);

        if (!pagoPorDeposito.procesarPago(pedido)) {
            log.error("\n ❌ Error al procesar el pago del pedido para el usuario '{}' \n", nombreUsuario);
            throw new IllegalArgumentException("Error al procesar el pago.");
        }

        carritoService.vaciarCarrito(usuario);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        log.info("\n ✅ Pedido generado y guardado exitosamente (ID={}) para usuario '{}' \n", pedidoGuardado.getId_pedido(), nombreUsuario);

        return pedidoGuardado;
    }

    @Override
    public List<Pedido> filtrarPedidos(Optional<Timestamp> fechaInicio, Optional<Timestamp> fechaFin, Optional<String> usuario) {
        log.info("\n 🔎 Filtrando pedidos con los siguientes parámetros: \n");
        fechaInicio.ifPresent(f -> log.info(" - Fecha inicio: {}", f));
        fechaFin.ifPresent(f -> log.info(" - Fecha fin: {}", f));
        usuario.ifPresent(u -> log.info(" - Usuario: {}", u));

        if (fechaInicio.isPresent() && fechaFin.isPresent() && usuario.isPresent() && !usuario.get().isEmpty()) {
            return pedidoRepository.findByFechaBetweenAndUsuario_Usuario(fechaInicio.get(), fechaFin.get(), usuario.get());
        } else if (fechaInicio.isPresent() && fechaFin.isPresent()) {
            return pedidoRepository.findByFechaBetween(fechaInicio.get(), fechaFin.get());
        } else if (usuario.isPresent() && !usuario.get().isEmpty()) {
            return pedidoRepository.findByUsuario_Usuario(usuario.get());
        } else {
            return pedidoRepository.findAll();
        }
    }

    @Override
    public Pedido obtenerPedidoPorId(Long id) {
        log.info("\n 📦 Buscando pedido por ID: {} \n", id);

        return pedidoRepository.findPedidoWithDetalles(id)
                .orElseThrow(() -> {
                    log.warn("\n ❌ Pedido no encontrado con ID: {} \n", id);
                    return new PedidoNoEncontradoException("Pedido no encontrado con ID: " + id);
                });
    }

    @Override
    public List<ReporteProducto> generarReportePorProducto(Optional<Timestamp> fechaInicio, Optional<Timestamp> fechaFin) {
        log.info("\n 📊 Generando reporte de ventas por producto... \n");

        List<DetallePedido> detalles = pedidoRepository.findVentasPorProducto(
                fechaInicio.orElse(Timestamp.valueOf("1970-01-01 00:00:00")),
                fechaFin.orElse(new Timestamp(System.currentTimeMillis()))
        );

        log.info("\n ✅ Se encontraron {} registros de ventas para el reporte \n", detalles.size());

        return detalles.stream().map(detalle -> new ReporteProducto(
                        detalle.getProducto().getNombre(),
                        detalle.getPedido().getFecha().toLocalDateTime().toLocalDate(),
                        detalle.getCantidad(),
                        detalle.getPrecio(),
                        detalle.getCantidad() * detalle.getPrecio()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> obtenerHistorialUsuario(String email, Optional<Timestamp> fechaInicio, Optional<Timestamp> fechaFin) {
        log.info("\n 📂 Consultando historial de pedidos para el usuario con email: {} \n", email);

        if (fechaInicio.isPresent() && fechaFin.isPresent()) {
            return pedidoRepository.findByFechaBetweenAndUsuario_Email(fechaInicio.get(), fechaFin.get(), email);
        } else {
            return pedidoRepository.findByUsuario_Email(email);
        }
    }
}
