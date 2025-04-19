package com.diplomado.tienda.controller;

import com.diplomado.tienda.model.Direccion;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.service.DireccionService;
import com.diplomado.tienda.security.AutenticacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/direcciones")
public class DireccionController {

    private final DireccionService direccionService;
    private final AutenticacionService autenticacionService;

    private Usuario obtenerUsuarioDesdePrincipal(Principal principal, RedirectAttributes redirectAttributes) {

        return autenticacionService.obtenerUsuarioDesdePrincipal(principal, redirectAttributes);
    }

    // Mostrar todas las direcciones del usuario autenticado
    @GetMapping
    public String mostrarDirecciones(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioDesdePrincipal(principal, redirectAttributes);

        List<Direccion> direcciones = direccionService.obtenerDireccionesPorUsuario(usuario);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("usuario", usuario);  // Pasar el usuario al modelo
        model.addAttribute("direccion", new Direccion());  // Formulario vacío para agregar una nueva dirección

        return "user/direcciones";  // Thymeleaf template "direcciones.html"
    }

    // Guardar una nueva dirección para el usuario autenticado
    @PostMapping
    public String guardarDireccion(@ModelAttribute Direccion direccion, Principal principal, RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioDesdePrincipal(principal, redirectAttributes);

        direccionService.guardarDireccion(usuario, direccion);
        return "redirect:/direcciones";  // Redirigir a la lista de direcciones
    }

    // Eliminar una dirección
    @PostMapping("/{idDireccion}/eliminar")
    public String eliminarDireccion(@PathVariable("idDireccion") Long idDireccion) {

        direccionService.eliminarDireccion(idDireccion);
        return "redirect:/direcciones";  // Redirigir a la lista de direcciones
    }

    // Mostrar formulario de edición de dirección
    @GetMapping("/{idDireccion}/editar")
    public String mostrarFormularioEdicion(@PathVariable("idDireccion") Long idDireccion,
                                           Principal principal, Model model, RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuarioDesdePrincipal(principal, redirectAttributes);

        Direccion direccion = direccionService.obtenerDireccionPorId(idDireccion).orElse(null);
        if (direccion == null) {
            redirectAttributes.addFlashAttribute("error", "Dirección no encontrada.");
            return "redirect:/direcciones";  // Si no se encuentra la dirección, redirigir a la lista
        }

        model.addAttribute("direccion", direccion);
        model.addAttribute("usuario", usuario);  // Pasar el usuario al modelo
        return "user/editarDireccion";  // Vista para editar la dirección
    }

    // Actualizar dirección
    @PostMapping("/{idDireccion}/editar")
    public String actualizarDireccion(@PathVariable("idDireccion") Long idDireccion,
                                      @ModelAttribute Direccion direccion, Principal principal, RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioDesdePrincipal(principal, redirectAttributes);

        direccion.setIdDireccion(idDireccion);
        direccionService.guardarDireccion(usuario, direccion);  // Usamos el mismo método para guardar la actualización
        return "redirect:/direcciones";  // Redirigir a la lista de direcciones
    }
}
