package com.diplomado.tienda.service;

import com.diplomado.tienda.dto.UserRegistrationDTO;
import com.diplomado.tienda.exception.UsuarioNoEncontradoException;
import com.diplomado.tienda.model.Rol;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.repository.UsuarioRepository;
import com.diplomado.tienda.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private RolService rolService;
    @InjectMocks private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsuario("usuario1");
        usuario.setEmail("usuario1@test.com");
        usuario.setContraseña("12345");

        rol = new Rol();
        rol.setId_rol(1L);
        rol.setNombre("USER");
    }

    @Test
    @DisplayName("registerNewUser guarda el usuario si el email no existe")
    void registerNewUser_exito() {
        UserRegistrationDTO dto = new UserRegistrationDTO("usuario1", "usuario1@test.com", "12345");

        when(usuarioRepository.findByUsuario(dto.getEmail())).thenReturn(Optional.empty());
        when(rolService.findByNombre("USER")).thenReturn(Optional.of(rol));

        usuarioService.registerNewUser(dto);

        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("registerNewUser lanza excepción si el email ya está registrado")
    void registerNewUser_emailDuplicado() {
        when(usuarioRepository.findByUsuario("usuario1@test.com")).thenReturn(Optional.of(usuario));

        UserRegistrationDTO dto = new UserRegistrationDTO("usuario1", "usuario1@test.com", "12345");

        assertThrows(IllegalArgumentException.class, () -> usuarioService.registerNewUser(dto));
    }

    @Test
    @DisplayName("obtenerUsuarioPorNombre retorna usuario existente")
    void obtenerUsuarioPorNombre_existente() {
        when(usuarioRepository.findByUsuario("usuario1")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.obtenerUsuarioPorNombre("usuario1");

        assertEquals("usuario1", resultado.getUsuario());
    }

    @Test
    @DisplayName("obtenerUsuarioPorNombre lanza excepción si no existe")
    void obtenerUsuarioPorNombre_noExiste() {
        when(usuarioRepository.findByUsuario("usuario1")).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () ->
                usuarioService.obtenerUsuarioPorNombre("usuario1"));
    }

    @Test
    @DisplayName("obtenerUsuarioPorEmail lanza excepción si el email es nulo o vacío")
    void obtenerUsuarioPorEmail_emailInvalido() {
        assertThrows(IllegalArgumentException.class, () -> usuarioService.obtenerUsuarioPorEmail(null));
        assertThrows(IllegalArgumentException.class, () -> usuarioService.obtenerUsuarioPorEmail("  "));
    }

    @Test
    @DisplayName("existeEmail debe devolver true si existe")
    void existeEmail_existente() {
        when(usuarioRepository.findByEmail("usuario1@test.com")).thenReturn(Optional.of(usuario));

        assertTrue(usuarioService.existeEmail("usuario1@test.com"));
    }

    @Test
    @DisplayName("eliminarUsuario elimina si el usuario existe")
    void eliminarUsuario_existente() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarUsuario lanza excepción si el usuario no existe")
    void eliminarUsuario_noExiste() {
        when(usuarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.eliminarUsuario(1L));
    }
}
