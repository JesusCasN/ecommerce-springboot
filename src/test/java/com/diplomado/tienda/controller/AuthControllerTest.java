package com.diplomado.tienda.controller;

import com.diplomado.tienda.dto.LoginRequest;
import com.diplomado.tienda.dto.UserRegistrationDTO;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.security.AutenticacionService;
import com.diplomado.tienda.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutenticacionService autenticacionService;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/auth/login debe retornar token si las credenciales son válidas")
    void login_exitoso() throws Exception {
        LoginRequest request = new LoginRequest("usuario", "1234");
        Map<String, String> response = Map.of("token", "jwt-token");

        Mockito.when(autenticacionService.autenticarUsuario(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .with(user("usuario").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    @DisplayName("POST /api/auth/register debe registrar al usuario exitosamente")
    void register_exitoso() throws Exception {
        UserRegistrationDTO userDTO = new UserRegistrationDTO("usuario", "correo@ejemplo.com", "12345");

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario registrado exitosamente"));

        Mockito.verify(usuarioService).registerNewUser(any(UserRegistrationDTO.class));
    }

    @Test
    @DisplayName("GET /api/auth/perfil debe retornar usuario autenticado")
    void perfil_retornaUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUsuario("usuario1");

        Mockito.when(autenticacionService.obtenerUsuarioAutenticado()).thenReturn(usuario);

        mockMvc.perform(get("/api/auth/perfil")
                        .with(user("usuario1").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario").value("usuario1"));
    }
}
