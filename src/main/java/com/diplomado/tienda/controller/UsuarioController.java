package com.diplomado.tienda.controller;

import com.diplomado.tienda.dto.UsuarioDTO;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.security.CustomUserDetailsService;
import com.diplomado.tienda.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final CustomUserDetailsService customUserDetailsService;

    // Ver perfil del usuario autenticado
    @GetMapping("/perfil")
    public String verPerfil(Model model, Principal principal) {
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(principal.getName());
        model.addAttribute("usuario", usuario);
        return "user/perfil";  // Vista del perfil
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id,
                                          Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        return "user/editarPerfil";  // Vista para editar el perfil
    }

    // Actualizar información del usuario autenticado (incluye la contraseña opcional)
    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable("id") Long id,
                                    @ModelAttribute UsuarioDTO usuarioDTO,
                                    @RequestParam(required = false) String nuevaContrasena,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Obtener el usuario existente
            Usuario usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
            String correoAntiguo = usuarioExistente.getEmail(); // Guardar el correo antiguo

            // Verificar si el correo electrónico ha cambiado antes de validarlo
            if (!correoAntiguo.equals(usuarioDTO.getEmail())) {
                // Validar si el correo electrónico ya está registrado
                if (usuarioService.existeEmail(usuarioDTO.getEmail())) {
                    redirectAttributes.addFlashAttribute("error", "El correo electrónico ya está registrado. Por favor, elija otro.");
                    return "redirect:/usuario/editar/" + id;  // Redirigir al formulario de edición
                }
            }

            // No se valida que el nombre de usuario sea único, solo si cambió
            if (!usuarioExistente.getUsuario().equals(usuarioDTO.getUsuario())) {
                usuarioExistente.setUsuario(usuarioDTO.getUsuario()); // Permitir cambiar el nombre de usuario
            }

            // Validar que la nueva contraseña no sea igual a la actual
            if (nuevaContrasena != null && nuevaContrasena.equals(usuarioExistente.getContraseña())) {
                redirectAttributes.addFlashAttribute("error", "La nueva contraseña no puede ser la misma que la actual.");
                return "redirect:/usuario/editar/" + id;  // Redirigir al formulario de edición
            }

            // Actualizar los datos del usuario
            usuarioExistente.setEmail(usuarioDTO.getEmail());
            usuarioService.actualizarUsuario(usuarioExistente, nuevaContrasena);

            // Si el correo ha cambiado, actualizar el contexto de seguridad
            if (!correoAntiguo.equals(usuarioDTO.getEmail())) {
                // Crear un nuevo objeto de autenticación con el nuevo correo
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(usuarioDTO.getEmail());
                UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(
                        userDetails, userDetails.getPassword(), userDetails.getAuthorities());

                // Actualizar el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(newAuthentication);
            }

            redirectAttributes.addFlashAttribute("message", "Perfil actualizado con éxito.");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "El usuario no fue encontrado. Por favor, inicie sesión nuevamente.");
            return "redirect:/login"; // Redirigir a la página de inicio de sesión
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "El correo electrónico ya está registrado. Por favor, elija otro.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el perfil: " + e.getMessage());
        }

        return "redirect:/usuario/editar/" + id;
    }

}