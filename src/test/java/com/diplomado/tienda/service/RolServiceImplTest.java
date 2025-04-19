package com.diplomado.tienda.service;

import com.diplomado.tienda.exception.RolNoEncontradoException;
import com.diplomado.tienda.model.Rol;
import com.diplomado.tienda.repository.RolRepository;
import com.diplomado.tienda.service.impl.RolServiceImpl;
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
class RolServiceImplTest {

    @Mock private RolRepository rolRepository;

    @InjectMocks
    private RolServiceImpl rolService;

    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol();
        rol.setId_rol(1L);
        rol.setNombre("ADMIN");
    }

    @Test
    @DisplayName("listarRoles debe retornar solo roles con nombre no nulo")
    void listarRoles_rolesConNombre() {
        Rol rolSinNombre = new Rol();
        rolSinNombre.setId_rol(2L);
        rolSinNombre.setNombre(null);

        when(rolRepository.findAll()).thenReturn(List.of(rol, rolSinNombre));

        List<Rol> resultado = rolService.listarRoles();

        assertEquals(1, resultado.size());
        assertEquals("ADMIN", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("obtenerRolPorId debe retornar un rol válido")
    void obtenerRolPorId_valido() {
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));

        Optional<Rol> resultado = rolService.obtenerRolPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("ADMIN", resultado.get().getNombre());
    }

    @Test
    @DisplayName("obtenerRolPorId debe lanzar excepción si el rol no existe")
    void obtenerRolPorId_noExiste() {
        when(rolRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(RolNoEncontradoException.class, () -> rolService.obtenerRolPorId(2));
    }

    @Test
    @DisplayName("findByNombre debe retornar el rol si existe")
    void findByNombre_existente() {
        when(rolRepository.findByNombre("ADMIN")).thenReturn(Optional.of(rol));

        Optional<Rol> resultado = rolService.findByNombre("ADMIN");

        assertTrue(resultado.isPresent());
        assertEquals("ADMIN", resultado.get().getNombre());
    }

    @Test
    @DisplayName("findByNombre debe retornar vacío si no existe")
    void findByNombre_noExiste() {
        when(rolRepository.findByNombre("PACIENTE")).thenReturn(Optional.empty());

        Optional<Rol> resultado = rolService.findByNombre("PACIENTE");

        assertTrue(resultado.isEmpty());
    }
}
