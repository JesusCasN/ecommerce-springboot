package com.diplomado.tienda.controller;

import com.diplomado.tienda.dto.LoginRequest;
import com.diplomado.tienda.dto.UserRegistrationDTO;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.security.AutenticacionService;
import com.diplomado.tienda.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AutenticacionService autenticacionService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        log.info("\n 🔐 Intento de login para el usuario: {} \n", request.getUsername());
        try {
            Map<String, String> response = autenticacionService.autenticarUsuario(request);
            log.info("\n ✅ Usuario '{}' autenticado correctamente. \n", request.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.warn("\n ❌ Fallo en el inicio de sesión para '{}': {} \n", request.getUsername(), e.getMessage());
            return ResponseEntity.status(401).body(Map.of("message", "Error en el inicio de sesión: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO userDTO) {
        log.info("\n 📝 Solicitud de registro para nuevo usuario: {} \n", userDTO.getUsername());
        try {
            usuarioService.registerNewUser(userDTO);
            log.info("\n ✅ Usuario '{}' registrado exitosamente. \n", userDTO.getUsername());
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (Exception e) {
            log.error("\n ❌ Error al registrar usuario '{}': {} \n", userDTO.getUsername(), e.getMessage());
            return ResponseEntity.status(400).body("Error en el registro: " + e.getMessage());
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<Usuario> obtenerPerfil() {
        Usuario usuario = autenticacionService.obtenerUsuarioAutenticado();
        log.info("\n 👤 Acceso al perfil del usuario autenticado: {} \n", usuario.getUsuario());
        return ResponseEntity.ok(usuario);
    }
}
