package com.diplomado.tienda.service.impl;

import com.diplomado.tienda.dto.UserRegistrationDTO;
import com.diplomado.tienda.exception.RolNoEncontradoException;
import com.diplomado.tienda.exception.UsuarioNoEncontradoException;
import com.diplomado.tienda.model.Rol;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.repository.UsuarioRepository;
import com.diplomado.tienda.service.RolService;
import com.diplomado.tienda.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolService rolService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void registerNewUser(UserRegistrationDTO userDTO) {
        log.info("\n 📝 Registrando nuevo usuario: {} \n", userDTO.getUsername());

        if (usuarioRepository.existsByEmail(userDTO.getEmail())) {
            log.warn("\n ❌ El correo {} ya está registrado. Registro cancelado. \n", userDTO.getEmail());
            throw new IllegalArgumentException("El correo electrónico ya esta registrado.");
        }

        Usuario newUser = new Usuario();
        newUser.setUsuario(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        newUser.setContraseña(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setFechaCreacion(LocalDateTime.now());

        Rol userRole = rolService.findByNombre("USER")
                .orElseThrow(() -> {
                    log.error("\n ❌ Rol USER no encontrado durante el registro de {} \n", userDTO.getUsername());
                    return new RolNoEncontradoException("Rol de usuario no encontrado");
                });

        newUser.setRol(userRole);
        usuarioRepository.save(newUser);

        log.info("\n ✅ Usuario '{}' registrado correctamente con rol '{}' \n", newUser.getUsuario(), userRole.getNombre());
    }

    @Override
    public Usuario obtenerUsuarioPorNombre(String nombreUsuario) {
        log.info("\n 🔍 Buscando usuario por nombre: {} \n", nombreUsuario);

        return usuarioRepository.findByUsuario(nombreUsuario)
                .orElseThrow(() -> {
                    log.warn("\n ❌ Usuario no encontrado con el nombre: {} \n", nombreUsuario);
                    return new UsuarioNoEncontradoException("Usuario no encontrado con el nombre: " + nombreUsuario);
                });
    }

    @Override
    public Usuario obtenerUsuarioPorEmail(String email) {
        log.info("\n 📧 Buscando usuario por email: {} \n", email);

        Optional.ofNullable(email)
                .filter(e -> !e.trim().isEmpty())
                .orElseThrow(() -> {
                    log.warn("\n ⚠️ Email vacío o nulo recibido en búsqueda. \n");
                    return new IllegalArgumentException("El email no puede estar vacío.");
                });

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("\n ❌ Usuario no encontrado con el email: {} \n", email);
                    return new UsuarioNoEncontradoException("Usuario no encontrado");
                });
    }

    @Override
    public boolean existeEmail(String email) {
        boolean existe = usuarioRepository.findByEmail(email).isPresent();
        log.debug("\n 📌 Verificando existencia del email '{}': {} \n", email, existe ? "Sí" : "No");
        return existe;
    }

    @Override
    public Usuario actualizarUsuarioAdmin(Usuario usuario) {
        log.info("\n ✏️ Actualizando usuario (Admin) con ID: {} \n", usuario.getIdUsuario());

        return Optional.ofNullable(usuario.getIdUsuario())
                .filter(usuarioRepository::existsById)
                .map(id -> {
                    Usuario actualizado = usuarioRepository.save(usuario);
                    log.info("\n ✅ Usuario actualizado correctamente (Admin): {} \n", actualizado.getUsuario());
                    return actualizado;
                })
                .orElseThrow(() -> {
                    log.warn("\n ❌ Usuario no encontrado para actualizar (Admin). ID: {} \n", usuario.getIdUsuario());
                    return new UsuarioNoEncontradoException("Usuario no encontrado para actualizar.");
                });
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario, String nuevaContrasena) {
        if (usuario == null || usuario.getIdUsuario() == null) {
            log.warn("\n ⚠️ Datos inválidos para actualización de usuario. Usuario o ID nulo. \n");
            throw new IllegalArgumentException("El usuario y su ID no pueden ser nulos.");
        }

        return usuarioRepository.findById(usuario.getIdUsuario())
                .map(u -> {
                    log.info("\n 🔄 Actualizando información del usuario '{}' \n", u.getUsuario());

                    u.setUsuario(usuario.getUsuario());
                    u.setEmail(usuario.getEmail());

                    if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {
                        u.setContraseña(passwordEncoder.encode(nuevaContrasena));
                        log.info("\n 🔐 Contraseña actualizada para el usuario '{}' \n", u.getUsuario());
                    }

                    Usuario actualizado = usuarioRepository.save(u);
                    log.info("\n ✅ Usuario '{}' actualizado correctamente. \n", actualizado.getUsuario());
                    return actualizado;
                })
                .orElseThrow(() -> {
                    log.warn("\n ❌ Usuario no encontrado para actualizar. ID: {} \n", usuario.getIdUsuario());
                    return new UsuarioNoEncontradoException("Usuario no encontrado para actualizar.");
                });
    }

    @Override
    public void eliminarUsuario(Long idUsuario) {
        log.info("\n 🗑️ Solicitando eliminación del usuario con ID: {} \n", idUsuario);

        Optional.ofNullable(idUsuario)
                .filter(usuarioRepository::existsById)
                .ifPresentOrElse(
                        usuarioId -> {
                            usuarioRepository.deleteById(usuarioId);
                            log.info("\n ✅ Usuario eliminado correctamente. ID: {} \n", usuarioId);
                        },
                        () -> {
                            log.warn("\n ❌ Usuario no encontrado para eliminar. ID: {} \n", idUsuario);
                            throw new UsuarioNoEncontradoException("Usuario no encontrado para eliminar.");
                        }
                );
    }

    @Override
    public List<Usuario> listarUsuarios() {
        log.info("\n 👥 Listando todos los usuarios registrados... \n");
        List<Usuario> usuarios = usuarioRepository.findAll();
        log.info("\n ✅ Se encontraron {} usuarios. \n", usuarios.size());
        return usuarios;
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long idUsuario) {
        log.info("\n 🔍 Buscando usuario por ID: {} \n", idUsuario);

        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> {
                    log.warn("\n ❌ Usuario no encontrado con el ID: {} \n", idUsuario);
                    return new UsuarioNoEncontradoException("Usuario no encontrado con el ID: " + idUsuario);
                });
    }
}
