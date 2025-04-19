package com.diplomado.tienda.security;

import com.diplomado.tienda.dto.LoginRequest;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AutenticacionService {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;

    // Método para verificar si el Principal es nulo y manejar el error
    private boolean esPrincipalValido(Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para realizar esta acción.");
            return false;
        }
        return true;
    }

    public Usuario obtenerUsuarioDesdePrincipal(Principal principal, RedirectAttributes redirectAttributes) {
        // Uso del nuevo método para verificar si el principal es válido
        if (!esPrincipalValido(principal, redirectAttributes)) {
            return null;
        }

        String nombreUsuario = principal.getName();
       Usuario usuario = usuarioService.obtenerUsuarioPorEmail(nombreUsuario);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
        }

        return usuario;
    }


    //Api AuthController
    public Map<String, String> autenticarUsuario(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, String> result = new HashMap<>();
        result.put("message", "Inicio de sesión exitoso");
        return result;
    }

    public Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No hay usuario autenticado.");
        }

        String email = authentication.getName();
        return usuarioService.obtenerUsuarioPorEmail(email);
    }

    
}
