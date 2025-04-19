package com.diplomado.tienda.service;

import com.diplomado.tienda.exception.CategoriaNoEncontradaException;
import com.diplomado.tienda.model.Categoria;
import com.diplomado.tienda.repository.CategoriaRepository;
import com.diplomado.tienda.service.impl.CategoriaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setNombre("Tecnología");
    }

    @Test
    @DisplayName("obtenerTodasLasCategorias debe retornar una lista de categorías cuando existen")
    void obtenerTodasLasCategorias_retornaCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<Categoria> resultado = categoriaService.obtenerTodasLasCategorias();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tecnología", resultado.get(0).getNombre());

        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodasLasCategorias debe lanzar excepción si no hay categorías")
    void obtenerTodasLasCategorias_lanzaExcepcionCuandoListaVacia() {
        when(categoriaRepository.findAll()).thenReturn(List.of());

        CategoriaNoEncontradaException ex = assertThrows(
                CategoriaNoEncontradaException.class,
                () -> categoriaService.obtenerTodasLasCategorias()
        );

        assertEquals("No se encontraron categorías.", ex.getMessage());
        verify(categoriaRepository, times(1)).findAll();
    }
}
