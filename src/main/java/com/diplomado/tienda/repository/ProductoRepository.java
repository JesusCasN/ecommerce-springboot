package com.diplomado.tienda.repository;

import com.diplomado.tienda.model.Producto;
import com.diplomado.tienda.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;


import org.springframework.data.jpa.repository.Query;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByStockGreaterThan(int stock);

    @Query("SELECT p FROM Producto p WHERE p.fecha_creacion BETWEEN :fechaInicio AND :fechaFin")
    List<Producto> filtrarProductosPorFecha(@Param("fechaInicio") LocalDateTime fechaInicio,
                                            @Param("fechaFin") LocalDateTime fechaFin);

    //Metodo para buscar productos por categoria
    List<Producto> findByCategoria(Categoria categoria);

    Page<Producto> findByCategoria(Categoria categoria, Pageable pageable);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findAllByOrderByStockAsc();
}

