package com.diplomado.tienda.service.impl;

import com.diplomado.tienda.dto.ProductoDTO;
import com.diplomado.tienda.exception.ProductoNoEncontradoException;
import com.diplomado.tienda.model.Producto;
import com.diplomado.tienda.model.Categoria;
import com.diplomado.tienda.repository.ProductoRepository;
import com.diplomado.tienda.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public List<Producto> listarProductos() {
        log.info("\n 📦 Listando todos los productos... \n");
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> listarProductosDisponibles() {
        log.info("\n 🟢 Listando productos con stock disponible... \n");
        return productoRepository.findByStockGreaterThan(0);
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        Producto guardado = productoRepository.save(producto);
        log.info("\n ✅ Producto guardado: ID={}, Nombre='{}' \n", guardado.getId_producto(), guardado.getNombre());
        return guardado;
    }

    @Override
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("\n ❌ Producto no encontrado para eliminar. ID={} \n", id);
                    return new ProductoNoEncontradoException("Producto no encontrado para eliminar.");
                });

        String nombreImagen = producto.getImagen();
        if (nombreImagen != null && !nombreImagen.isEmpty()) {
            String rutaImagen = "src/main/resources/static/images/productosImg/" + nombreImagen;
            Path path = Paths.get(rutaImagen);

            try {
                Files.deleteIfExists(path);
                log.info("\n 🗑️ Imagen del producto eliminada: {} \n", rutaImagen);
            } catch (IOException e) {
                log.warn("\n ⚠️ No se pudo eliminar la imagen '{}': {} \n", rutaImagen, e.getMessage());
            }
        }

        productoRepository.delete(producto);
        log.info("\n ✅ Producto eliminado con ID: {} \n", id);
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        log.info("\n 🔍 Buscando producto por ID: {} \n", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("\n ❌ Producto no encontrado con ID: {} \n", id);
                    return new ProductoNoEncontradoException("Producto no encontrado");
                });
    }

    @Override
    public Producto actualizarProducto(Producto producto) {
        Long id = producto.getId_producto();
        log.info("\n ✏️ Intentando actualizar producto con ID: {} \n", id);

        return Optional.ofNullable(id)
                .filter(productoRepository::existsById)
                .map(i -> {
                    Producto actualizado = productoRepository.save(producto);
                    log.info("\n ✅ Producto actualizado: ID={}, Nombre='{}' \n", actualizado.getId_producto(), actualizado.getNombre());
                    return actualizado;
                })
                .orElseThrow(() -> {
                    log.warn("\n ❌ Producto no encontrado para actualizar. ID={} \n", id);
                    return new ProductoNoEncontradoException("Producto no encontrado para actualizar.");
                });
    }

    @Override
    public List<Producto> filtrarProductosPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime startDateTime = fechaInicio.atStartOfDay();
        LocalDateTime endDateTime = fechaFin.atTime(23, 59, 59);
        log.info("\n 📅 Filtrando productos entre {} y {} \n", startDateTime, endDateTime);
        return productoRepository.filtrarProductosPorFecha(startDateTime, endDateTime);
    }

    @Override
    public List<Producto> buscarPorCategoria(Categoria categoria) {
        log.info("\n 🔎 Buscando productos por categoría: {} \n", categoria.getNombre());
        return productoRepository.findByCategoria(categoria);
    }

    @Override
    public Page<Producto> listarProductosPaginados(int pagina, int cantidadPorPagina) {
        log.info("\n 📄 Listando productos paginados - Página: {}, Tamaño: {} \n", pagina, cantidadPorPagina);
        Pageable pageable = PageRequest.of(pagina, cantidadPorPagina);
        return productoRepository.findAll(pageable);
    }

    @Override
    public Page<Producto> buscarPorCategoriaPaginado(Categoria categoria, int pagina, int cantidadPorPagina) {
        log.info("\n 📄 Buscando productos por categoría '{}' paginado - Página: {}, Tamaño: {} \n", categoria.getNombre(), pagina, cantidadPorPagina);
        Pageable pageable = PageRequest.of(pagina, cantidadPorPagina);
        return productoRepository.findByCategoria(categoria, pageable);
    }

    @Override
    public List<ProductoDTO> buscarPorNombre(String nombre) {
        log.info("\n 🔍 Buscando productos que contengan en su nombre: '{}' \n", nombre);
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(producto -> new ProductoDTO(
                        producto.getId_producto(),
                        producto.getNombre(),
                        producto.getDescripcion(),
                        producto.getPrecio(),
                        producto.getImagen()
                )).collect(Collectors.toList());
    }

    @Override
    public List<Producto> buscarPorNombreProducto(String nombre) {
        log.info("\n 🔍 Buscando productos (sin DTO) que contengan en su nombre: '{}' \n", nombre);
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Producto> listarProductoPorStock() {
        log.info("\n 📦 Listando productos ordenados por stock ascendente \n");
        return productoRepository.findAllByOrderByStockAsc();
    }
}
