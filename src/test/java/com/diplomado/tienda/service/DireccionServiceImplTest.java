package com.diplomado.tienda.service;

import com.diplomado.tienda.exception.DireccionNoEncontradaException;
import com.diplomado.tienda.exception.UsuarioNoAutenticadoException;
import com.diplomado.tienda.model.Direccion;
import com.diplomado.tienda.model.EstadoDireccion;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.repository.DireccionRepository;
import com.diplomado.tienda.service.impl.DireccionServiceImpl;
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
class DireccionServiceImplTest {

    @Mock
    private DireccionRepository direccionRepository;

    @InjectMocks
    private DireccionServiceImpl direccionService;

    private Usuario usuario;
    private Direccion direccion;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsuario("cliente1");

        direccion = new Direccion();
        direccion.setIdDireccion(100L);
        direccion.setUsuario(usuario);
        direccion.setCondicion(EstadoDireccion.ACTIVO);
    }

    @Test
    @DisplayName("obtenerDireccionesPorUsuario debe retornar lista cuando el usuario está autenticado")
    void obtenerDireccionesPorUsuario_usuarioAutenticado() {
        when(direccionRepository.findByUsuarioAndCondicion(usuario, EstadoDireccion.ACTIVO))
                .thenReturn(List.of(direccion));

        List<Direccion> direcciones = direccionService.obtenerDireccionesPorUsuario(usuario);

        assertEquals(1, direcciones.size());
        verify(direccionRepository).findByUsuarioAndCondicion(usuario, EstadoDireccion.ACTIVO);
    }

    @Test
    @DisplayName("obtenerDireccionesPorUsuario debe lanzar excepción cuando el usuario es null")
    void obtenerDireccionesPorUsuario_usuarioNull() {
        assertThrows(UsuarioNoAutenticadoException.class, () ->
                direccionService.obtenerDireccionesPorUsuario(null));
    }

    @Test
    @DisplayName("guardarDireccion debe guardar correctamente la dirección si el usuario está autenticado")
    void guardarDireccion_conUsuarioAutenticado() {
        when(direccionRepository.save(direccion)).thenReturn(direccion);

        Direccion resultado = direccionService.guardarDireccion(usuario, direccion);

        assertNotNull(resultado);
        assertEquals(usuario, resultado.getUsuario());
        verify(direccionRepository).save(direccion);
    }

    @Test
    @DisplayName("guardarDireccion debe lanzar excepción si el usuario es null")
    void guardarDireccion_usuarioNull() {
        assertThrows(UsuarioNoAutenticadoException.class, () ->
                direccionService.guardarDireccion(null, direccion));
    }

    @Test
    @DisplayName("obtenerDireccionPorId debe retornar la dirección si existe")
    void obtenerDireccionPorId_existente() {
        when(direccionRepository.findById(100L)).thenReturn(Optional.of(direccion));

        Optional<Direccion> resultado = direccionService.obtenerDireccionPorId(100L);

        assertTrue(resultado.isPresent());
        assertEquals(direccion, resultado.get());
        verify(direccionRepository).findById(100L);
    }

    @Test
    @DisplayName("eliminarDireccion debe marcar como eliminada si la dirección existe")
    void eliminarDireccion_existente() {
        when(direccionRepository.findById(100L)).thenReturn(Optional.of(direccion));

        direccionService.eliminarDireccion(100L);

        assertEquals(EstadoDireccion.ELIMINADO, direccion.getCondicion());
        verify(direccionRepository).save(direccion);
    }

    @Test
    @DisplayName("eliminarDireccion debe lanzar excepción si la dirección no existe")
    void eliminarDireccion_noExistente() {
        when(direccionRepository.findById(200L)).thenReturn(Optional.empty());

        assertThrows(DireccionNoEncontradaException.class, () ->
                direccionService.eliminarDireccion(200L));
    }
}
