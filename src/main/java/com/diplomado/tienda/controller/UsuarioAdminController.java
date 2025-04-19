package com.diplomado.tienda.controller;

import com.diplomado.tienda.dto.UsuarioDTO;
import com.diplomado.tienda.model.Rol;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.service.RolService;
import com.diplomado.tienda.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasAuthority('ADMIN')")
public class UsuarioAdminController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    // Listar todos los usuarios
    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios";
    }

    // Mostrar formulario para editar un usuario
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        List<Rol> roles = rolService.listarRoles();

        // Convertir Usuario a UsuarioDTO
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(usuario.getIdUsuario());
        usuarioDTO.setUsuario(usuario.getUsuario());
        usuarioDTO.setEmail(usuario.getEmail());

        if (usuario.getRol() != null) {
            usuarioDTO.setIdRol(usuario.getRol().getId_rol());
        }

        model.addAttribute("usuarioDTO", usuarioDTO);
        model.addAttribute("roles", roles);

        return "admin/editarUsuario";
    }


    // Actualizar un usuario
    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable("id") Long id,
                                    @ModelAttribute UsuarioDTO usuarioDTO) {
        // Obtener el usuario actual de la base de datos
        Usuario usuarioExistente = usuarioService.obtenerUsuarioPorId(id);
        usuarioExistente.setEmail(usuarioDTO.getEmail());
        usuarioExistente.setUsuario(usuarioDTO.getUsuario());

        // Actualizar el rol si es diferente
        if (usuarioDTO.getIdRol() != null) {
            Rol rol = rolService.obtenerRolPorId(usuarioDTO.getIdRol().intValue())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            usuarioExistente.setRol(rol);
        }

        usuarioService.actualizarUsuarioAdmin(usuarioExistente);
        return "redirect:/admin/usuarios?success";  // Redirigir con mensaje de éxito
    }


    // Eliminar un usuario
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id,
                                  RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("message", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}
