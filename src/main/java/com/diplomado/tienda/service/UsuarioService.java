package com.diplomado.tienda.service;

import com.diplomado.tienda.dto.UserRegistrationDTO;
import com.diplomado.tienda.model.Usuario;

import java.util.List;


public interface UsuarioService {

    void registerNewUser(UserRegistrationDTO userDTO);

    Usuario obtenerUsuarioPorEmail(String email);

    Usuario obtenerUsuarioPorNombre(String nombreUsuario);

    Usuario actualizarUsuarioAdmin(Usuario usuario);

    boolean existeEmail(String email);

    Usuario actualizarUsuario(Usuario usuario, String nuevaContrasena);

    void eliminarUsuario(Long idUsuario);

    List<Usuario> listarUsuarios();

    Usuario obtenerUsuarioPorId(Long idUsuario);
}
