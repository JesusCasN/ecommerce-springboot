package com.diplomado.tienda.service;

import com.diplomado.tienda.exception.FormaDePagoNoEncontradaException;
import com.diplomado.tienda.model.FormasDePago;
import com.diplomado.tienda.repository.FormasDePagoRepository;
import com.diplomado.tienda.service.impl.FormasDePagoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormasDePagoServiceImplTest {

    @Mock
    private FormasDePagoRepository formasDePagoRepository;

    @InjectMocks
    private FormasDePagoServiceImpl formasDePagoService;

    private FormasDePago forma;

    @BeforeEach
    void setUp() {
        forma = new FormasDePago();
        forma.setIdFormaPago(1);
        forma.setNombre("Tarjeta de Crédito");
    }

    @Test
    @DisplayName("listarFormasDePago debe retornar una lista de formas de pago")
    void listarFormasDePago_retornarLista() {
        when(formasDePagoRepository.findAll()).thenReturn(List.of(forma));

        List<FormasDePago> resultado = formasDePagoService.listarFormasDePago();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tarjeta de Crédito", resultado.get(0).getNombre());
        verify(formasDePagoRepository).findAll();
    }

    @Test
    @DisplayName("obtenerPorId debe retornar la forma de pago si existe")
    void obtenerPorId_existente() {
        when(formasDePagoRepository.findById(1)).thenReturn(Optional.of(forma));

        FormasDePago resultado = formasDePagoService.obtenerPorId(1);

        assertNotNull(resultado);
        assertEquals("Tarjeta de Crédito", resultado.getNombre());
        verify(formasDePagoRepository).findById(1);
    }

    @Test
    @DisplayName("obtenerPorId debe lanzar excepción si no se encuentra")
    void obtenerPorId_noExistente() {
        when(formasDePagoRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(FormaDePagoNoEncontradaException.class, () ->
                formasDePagoService.obtenerPorId(2));
        verify(formasDePagoRepository).findById(2);
    }
}
