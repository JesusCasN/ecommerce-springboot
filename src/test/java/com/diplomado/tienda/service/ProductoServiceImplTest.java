package com.diplomado.tienda.service;

import com.diplomado.tienda.dto.ProductoDTO;
import com.diplomado.tienda.exception.ProductoNoEncontradoException;
import com.diplomado.tienda.model.Categoria;
import com.diplomado.tienda.model.Producto;
import com.diplomado.tienda.repository.ProductoRepository;
import com.diplomado.tienda.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setNombre("Electrónica");

        producto = new Producto();
        producto.setId_producto(1L);
        producto.setNombre("Laptop");
        producto.setDescripcion("Laptop potente");
        producto.setPrecio(BigDecimal.valueOf(999.99));
        producto.setImagen("imagen.jpg");
        producto.setCategoria(categoria);
    }

    @Test
    @DisplayName("guardarProducto debe guardar correctamente")
    void guardarProducto() {
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.guardarProducto(producto);

        assertNotNull(resultado);
        assertEquals("Laptop", resultado.getNombre());
    }

    @Test
    @DisplayName("obtenerProductoPorId debe retornar producto si existe")
    void obtenerProductoPorId_existente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.obtenerProductoPorId(1L);

        assertEquals("Laptop", resultado.getNombre());
    }

    @Test
    @DisplayName("obtenerProductoPorId debe lanzar excepción si no existe")
    void obtenerProductoPorId_noExiste() {
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ProductoNoEncontradoException.class, () ->
                productoService.obtenerProductoPorId(2L));
    }

    @Test
    @DisplayName("listarProductos debe retornar todos los productos")
    void listarProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> lista = productoService.listarProductos();

        assertFalse(lista.isEmpty());
    }

    @Test
    @DisplayName("actualizarProducto debe actualizar si el producto existe")
    void actualizarProducto_existente() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto actualizado = productoService.actualizarProducto(producto);

        assertEquals(producto.getNombre(), actualizado.getNombre());
    }

    @Test
    @DisplayName("actualizarProducto lanza excepción si no existe")
    void actualizarProducto_noExiste() {
        when(productoRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductoNoEncontradoException.class, () ->
                productoService.actualizarProducto(producto));
    }

    @Test
    @DisplayName("buscarPorCategoria debe retornar productos por categoría")
    void buscarPorCategoria() {
        when(productoRepository.findByCategoria(categoria)).thenReturn(List.of(producto));

        List<Producto> resultados = productoService.buscarPorCategoria(categoria);

        assertEquals(1, resultados.size());
    }

    @Test
    @DisplayName("buscarPorNombre debe mapear correctamente a DTO")
    void buscarPorNombre() {
        when(productoRepository.findByNombreContainingIgnoreCase("laptop")).thenReturn(List.of(producto));

        List<ProductoDTO> resultados = productoService.buscarPorNombre("laptop");

        assertEquals(1, resultados.size());
        assertEquals("Laptop", resultados.get(0).getNombre());
    }

    @Test
    @DisplayName("listarProductosPaginados retorna una página de productos")
    void listarProductosPaginados() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Producto> page = new PageImpl<>(List.of(producto));

        when(productoRepository.findAll(pageRequest)).thenReturn(page);

        Page<Producto> resultado = productoService.listarProductosPaginados(0, 5);

        assertEquals(1, resultado.getTotalElements());
    }
}
