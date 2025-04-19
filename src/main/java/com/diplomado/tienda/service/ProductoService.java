package com.diplomado.tienda.service;

import com.diplomado.tienda.dto.ProductoDTO;
import com.diplomado.tienda.model.Categoria;
import com.diplomado.tienda.model.Producto;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ProductoService {

    List<Producto> listarProductos();

    List<Producto> listarProductosDisponibles();

    Producto guardarProducto(Producto producto);

    void eliminarProducto(Long id);

    Producto obtenerProductoPorId(Long id);

    Producto actualizarProducto(Producto producto);

    List<Producto> filtrarProductosPorFecha(LocalDate fechaInicio, LocalDate fechaFin);

    List<Producto> buscarPorCategoria(Categoria categoria);

    Page<Producto> listarProductosPaginados(int pagina, int cantidadPorPagina);

    Page<Producto> buscarPorCategoriaPaginado(Categoria categoria, int pagina, int cantidadPorPagina);

    List<ProductoDTO> buscarPorNombre(String nombre);

    List<Producto> buscarPorNombreProducto(String nombre);

    List<Producto> listarProductoPorStock();
}
