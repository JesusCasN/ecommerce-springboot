package com.diplomado.tienda.service;

import com.diplomado.tienda.exception.DireccionNoEncontradaException;
import com.diplomado.tienda.exception.PedidoNoEncontradoException;
import com.diplomado.tienda.factory.PedidoFactory;
import com.diplomado.tienda.model.*;
import com.diplomado.tienda.repository.PedidoRepository;
import com.diplomado.tienda.service.impl.PagoPorDeposito;
import com.diplomado.tienda.service.impl.PedidoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private CarritoService carritoService;
    @Mock private UsuarioService usuarioService;
    @Mock private DireccionService direccionService;
    @Mock private PagoPorDeposito pagoPorDeposito;
    @Mock private PedidoFactory pedidoFactory;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Usuario usuario;
    private Producto producto;
    private Carrito carrito;
    private Direccion direccion;
    private FormasDePago formaPago;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsuario("cliente1");

        producto = new Producto();
        producto.setPrecio(BigDecimal.valueOf(100));

        carrito = new Carrito();
        carrito.setProducto(producto);
        carrito.setCantidad(2);

        direccion = new Direccion();
        direccion.setIdDireccion(1L);
        direccion.setCondicion(EstadoDireccion.ACTIVO);

        formaPago = new FormasDePago();
        formaPago.setIdFormaPago(
                1);
        formaPago.setNombre("Depósito");

        pedido = new Pedido();
        pedido.setId_pedido(99L);
    }

    @Test
    @DisplayName("realizarPedido debe crear un pedido válido")
    void realizarPedido_correcto() {
        when(usuarioService.obtenerUsuarioPorNombre("cliente1")).thenReturn(usuario);
        when(carritoService.obtenerProductosEnCarrito("cliente1")).thenReturn(List.of(carrito));
        when(direccionService.obtenerDireccionPorId(1L)).thenReturn(Optional.of(direccion));
        when(pedidoFactory.crearPedido(any(), any(), any(), anyDouble(), anyList())).thenReturn(pedido);
        when(pagoPorDeposito.procesarPago(pedido)).thenReturn(true);
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido resultado = pedidoService.realizarPedido("cliente1", formaPago, 1L);

        assertNotNull(resultado);
        assertEquals(99L, resultado.getId_pedido());
        verify(carritoService).vaciarCarrito(usuario);
    }

    @Test
    @DisplayName("realizarPedido lanza excepción si el carrito está vacío")
    void realizarPedido_carritoVacio() {
        when(usuarioService.obtenerUsuarioPorNombre("cliente1")).thenReturn(usuario);
        when(carritoService.obtenerProductosEnCarrito("cliente1")).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () ->
                pedidoService.realizarPedido("cliente1", formaPago, 1L));
    }

    @Test
    @DisplayName("realizarPedido lanza excepción si dirección está eliminada")
    void realizarPedido_direccionInvalida() {
        direccion.setCondicion(EstadoDireccion.ELIMINADO);
        when(usuarioService.obtenerUsuarioPorNombre("cliente1")).thenReturn(usuario);
        when(carritoService.obtenerProductosEnCarrito("cliente1")).thenReturn(List.of(carrito));
        when(direccionService.obtenerDireccionPorId(1L)).thenReturn(Optional.of(direccion));

        assertThrows(DireccionNoEncontradaException.class, () ->
                pedidoService.realizarPedido("cliente1", formaPago, 1L));
    }

    @Test
    @DisplayName("obtenerPedidoPorId debe retornar pedido si existe")
    void obtenerPedidoPorId_existente() {
        when(pedidoRepository.findPedidoWithDetalles(99L)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.obtenerPedidoPorId(99L);

        assertEquals(99L, resultado.getId_pedido());
    }

    @Test
    @DisplayName("obtenerPedidoPorId lanza excepción si no existe")
    void obtenerPedidoPorId_noExiste() {
        when(pedidoRepository.findPedidoWithDetalles(1L)).thenReturn(Optional.empty());

        assertThrows(PedidoNoEncontradoException.class, () ->
                pedidoService.obtenerPedidoPorId(1L));
    }
}
