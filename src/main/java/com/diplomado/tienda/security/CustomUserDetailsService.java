package com.diplomado.tienda.security;

import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        // Buscar el email en la base de datos
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(email);

        // Devolver un objeto UserDetails con los datos del usuario
        return User.withUsername(usuario.getEmail())
                .password(usuario.getContraseña()) // Contraseña ya encriptada en la base de datos
                .roles(usuario.getRol().getNombre()) // Rol del usuario
                .build();
    }

}
