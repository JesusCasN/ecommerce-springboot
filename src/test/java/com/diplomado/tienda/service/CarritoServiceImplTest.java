package com.diplomado.tienda.service;

import com.diplomado.tienda.exception.ProductoNoDisponibleException;
import com.diplomado.tienda.exception.ProductoNoEncontradoEnCarritoException;
import com.diplomado.tienda.factory.CarritoFactory;
import com.diplomado.tienda.model.Carrito;
import com.diplomado.tienda.model.Producto;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.repository.CarritoRepository;
import com.diplomado.tienda.service.impl.CarritoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private CarritoFactory carritoFactory;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Usuario usuario;
    private Producto producto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setUsuario("usuario1");

        producto = new Producto();
        producto.setId_producto(1L);
        producto.setNombre("Producto Test");
        producto.setStock(10);
        producto.setPrecio(BigDecimal.valueOf(100));
    }

    @Test
    void agregarProducto_conStockDisponible_guardarNuevoCarrito() {
        when(carritoRepository.findByUsuarioAndProducto(usuario, producto)).thenReturn(null);

        Carrito nuevoCarrito = new Carrito();
        when(carritoFactory.crearCarrito(usuario, producto, 2)).thenReturn(nuevoCarrito);

        carritoService.agregarProducto(usuario, producto, 2);

        verify(carritoRepository).save(nuevoCarrito);
    }

    @Test
    void agregarProducto_sinStockDisponible_lanzaExcepcion() {
        producto.setStock(1);
        assertThrows(ProductoNoDisponibleException.class, () -> {
            carritoService.agregarProducto(usuario, producto, 5);
        });
    }

    @Test
    void actualizarCantidadProductoExistente_conStockActualizaCorrectamente() {
        Carrito carritoExistente = new Carrito();
        carritoExistente.setCantidad(2);

        carritoService.actualizarCantidadProductoExistente(carritoExistente, 2, producto);

        assertEquals(4, carritoExistente.getCantidad());
        verify(carritoRepository).save(carritoExistente);
    }

    @Test
    void actualizarCantidadProductoExistente_sinStock_lanzaExcepcion() {
        Carrito carritoExistente = new Carrito();
        carritoExistente.setCantidad(8);
        producto.setStock(9);

        assertThrows(ProductoNoDisponibleException.class, () -> {
            carritoService.actualizarCantidadProductoExistente(carritoExistente, 5, producto);
        });
    }

    @Test
    void actualizarCantidadProducto_eliminaCuandoCantidadEsCero() {
        Carrito carrito = new Carrito();
        carrito.setCantidad(2);
        when(carritoRepository.findByUsuarioAndProducto(usuario, producto)).thenReturn(carrito);

        carritoService.actualizarCantidadProducto(usuario, producto, 0);

        verify(carritoRepository).delete(carrito);
    }

    @Test
    void eliminarProducto_existente_eliminaCorrectamente() {
        Carrito carrito = new Carrito();
        when(carritoRepository.findByUsuarioAndProducto(usuario, producto)).thenReturn(carrito);

        carritoService.eliminarProducto(usuario, producto);

        verify(carritoRepository).delete(carrito);
    }

    @Test
    void eliminarProducto_noExistente_lanzaExcepcion() {
        when(carritoRepository.findByUsuarioAndProducto(usuario, producto)).thenReturn(null);

        assertThrows(ProductoNoEncontradoEnCarritoException.class, () -> {
            carritoService.eliminarProducto(usuario, producto);
        });
    }
}
