package com.diplomado.tienda.controller;

import com.diplomado.tienda.model.*;
import com.diplomado.tienda.service.*;
import com.diplomado.tienda.reports.ComprobanteDePagoPDF;
import com.diplomado.tienda.security.AutenticacionService;
import com.diplomado.tienda.util.FormatoUtil;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final ProductoService productoService;
    private final AutenticacionService autenticacionService;
    private final PedidoService pedidoService;
    private final FormasDePagoService formasDePagoService;
    private final DireccionService direccionService;
    private final ComprobanteDePagoPDF comprobanteDePagoPDF;

    // Métodos auxiliares
    private Usuario verificarYObtenerUsuario(Principal principal, RedirectAttributes redirectAttributes) {
        Usuario usuario = autenticacionService.obtenerUsuarioDesdePrincipal(principal, redirectAttributes);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión para realizar esta acción.");
        }
        return usuario;
    }

    private Producto verificarYObtenerProducto(Long productoId, RedirectAttributes redirectAttributes) {
        Producto producto = productoService.obtenerProductoPorId(productoId);
        if (producto == null) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
        }
        return producto;
    }

    // Método auxiliar para verificar y obtener la dirección por su ID
    private Direccion verificarYObtenerDireccion(Long idDireccion, RedirectAttributes redirectAttributes) {
        Optional<Direccion> direccion = direccionService.obtenerDireccionPorId(idDireccion);
        if (direccion.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Dirección no válida.");
        }
        return direccion.orElse(null);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String mostrarCarrito(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        Usuario usuario = verificarYObtenerUsuario(principal, redirectAttributes);

        log.info("\n 🛒 Mostrando carrito para el usuario '{}' \n", usuario.getUsuario());
        List<Carrito> productosEnCarrito = carritoService.obtenerProductosEnCarrito(usuario.getUsuario());
        BigDecimal totalCarrito = productosEnCarrito.stream()
                .map(Carrito::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("productosEnCarrito", productosEnCarrito);
        model.addAttribute("totalCarrito", FormatoUtil.formatDecimal(totalCarrito));

        return "user/carrito";  // Thymeleaf template "carrito.html"
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/agregar")
    public String agregarProductoAlCarrito(@RequestParam(name = "productoId") Long productoId,
                                           @RequestParam(name = "cantidad") int cantidad,
                                           Principal principal, RedirectAttributes redirectAttributes) {
        Usuario usuario = verificarYObtenerUsuario(principal, redirectAttributes);
        if (usuario == null) {
            log.warn("\n ⚠️ Usuario no autenticado al intentar agregar producto. \n");
            return "redirect:/carrito";  // Redirige si no hay usuario encontrado
        }

        Producto producto = verificarYObtenerProducto(productoId, redirectAttributes);
        if (producto == null) {
            log.warn("\n ⚠️ Producto con ID {} no encontrado al intentar agregar al carrito. \n", productoId);
            return "redirect:/carrito";  // Redirige si no se encuentra el producto
        }

        try {
            carritoService.agregarProducto(usuario, producto, cantidad);
            log.info("\n ✅ Producto '{}' agregado al carrito del usuario '{}' \n", producto.getNombre(), usuario.getUsuario());
            redirectAttributes.addFlashAttribute("message", "Producto agregado al carrito exitosamente.");
        } catch (IllegalArgumentException e) {
            log.error("\n ❌ Error al agregar producto al carrito: {} \n", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/carrito";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/actualizar")
    public String actualizarCantidadProducto(@RequestParam("productoId") Long productoId,
                                             @RequestParam("cantidad") int cantidad,
                                             Principal principal, RedirectAttributes redirectAttributes) {
        Usuario usuario = verificarYObtenerUsuario(principal, redirectAttributes);
        if (usuario == null) {
            return "redirect:/carrito";  // Redirige si no hay usuario encontrado
        }

        Producto producto = verificarYObtenerProducto(productoId, redirectAttributes);
        if (producto == null) {
            return "redirect:/carrito";  // Redirige si no se encuentra el producto
        }

        try {
            carritoService.actualizarCantidadProducto(usuario, producto, cantidad);
            redirectAttributes.addFlashAttribute("message", "Cantidad actualizada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/carrito";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/eliminar/{id_producto}")
    public String eliminarProductoDelCarrito(@PathVariable("id_producto") Long id_producto,
                                             Principal principal, RedirectAttributes redirectAttributes) {
        Usuario usuario = verificarYObtenerUsuario(principal, redirectAttributes);
        if (usuario == null) {
            return "redirect:/carrito";
        }

        Producto producto = verificarYObtenerProducto(id_producto, redirectAttributes);
        if (producto == null) {
            return "redirect:/carrito";
        }

        try {
            carritoService.eliminarProducto(usuario, producto);
            redirectAttributes.addFlashAttribute("message", "Producto eliminado del carrito.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/carrito";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/vaciar")
    public String vaciarCarrito(Principal principal, RedirectAttributes redirectAttributes) {
        Usuario usuario = verificarYObtenerUsuario(principal, redirectAttributes);
        if (usuario == null) {
            return "redirect:/carrito";
        }

        try {
            carritoService.vaciarCarrito(usuario);
            redirectAttributes.addFlashAttribute("message", "Carrito vaciado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al vaciar el carrito: " + e.getMessage());
        }

        return "redirect:/carrito";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/proceder-al-pago")
    public String procederAlPago(@RequestParam("idFormaPago") Integer idFormaPago,
                                 @RequestParam("idDireccion") Long idDireccion,
                                 Principal principal, Model model, RedirectAttributes redirectAttributes) {
        return procesarPago(idFormaPago, idDireccion, principal, model, redirectAttributes);
    }

    private String procesarPago(@RequestParam("idFormaPago") Integer idFormaPago,
                                @RequestParam("idDireccion") Long idDireccion,
                                Principal principal, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = verificarYObtenerUsuario(principal, redirectAttributes);
        if (usuario == null) {
            return "redirect:/login"; // Redirigir al login si el usuario no está autenticado
        }

        try {
            // Obtener la forma de pago seleccionada
            FormasDePago formaPago = formasDePagoService.obtenerPorId(idFormaPago);
            if (formaPago == null) {
                redirectAttributes.addFlashAttribute("error", "Método de pago inválido.");
                return "redirect:/carrito";
            }

            // Verificar y obtener la dirección
            Direccion direccion = verificarYObtenerDireccion(idDireccion, redirectAttributes);
            if (direccion == null) {
                return "redirect:/carrito";
            }

            // Crear el pedido con el método de pago seleccionado
            Pedido pedido = pedidoService.realizarPedido(usuario.getUsuario(), formaPago, idDireccion);

            // Añadir el pedido al modelo para la vista de confirmación
            model.addAttribute("pedido", pedido);

            // Redirigir a la vista de confirmación del pedido
            return "pedidoConfirmacion"; // Un template llamado "pedidoConfirmacion.html"
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/carrito";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/confirmar-carrito")
    public String confirmarCarrito(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = verificarYObtenerUsuario(principal, redirectAttributes);
        if (usuario == null) {
            return "redirect:/login"; // Redirigir al login si el usuario no está autenticado
        }

        // Obtener los productos del carrito
        List<Carrito> productosEnCarrito = carritoService.obtenerProductosEnCarrito(usuario.getUsuario());
        if (productosEnCarrito.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El carrito está vacío.");
            return "redirect:/carrito";
        }

        // Calcular el total
        BigDecimal totalCarrito = productosEnCarrito.stream()
                .map(Carrito::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Obtener las formas de pago disponibles
        List<FormasDePago> formasDePago = formasDePagoService.listarFormasDePago();

        // Obtener direcciones del usuario
        List<Direccion> direcciones = direccionService.obtenerDireccionesPorUsuario(usuario);

        // Pasar los datos al modelo
        model.addAttribute("productosEnCarrito", productosEnCarrito);
        model.addAttribute("totalCarrito", FormatoUtil.formatDecimal(totalCarrito));
        model.addAttribute("formasDePago", formasDePago);
        model.addAttribute("direcciones", direcciones);

        return "user/confirmar-carrito"; // Retorna la vista "confirmar-carrito.html"
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/confirmar-pedido")
    public String confirmarPedido(@RequestParam("idFormaPago") Integer idFormaPago,
                                  @RequestParam("idDireccion") Long idDireccion,
                                  Principal principal, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = verificarYObtenerUsuario(principal, redirectAttributes);
        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            // Obtener la forma de pago seleccionada
            FormasDePago formaPago = formasDePagoService.obtenerPorId(idFormaPago);
            if (formaPago == null) {
                redirectAttributes.addFlashAttribute("error", "Método de pago inválido.");
                return "redirect:/carrito/confirmar-carrito";
            }

            // Verificar y obtener la dirección
            Direccion direccion = verificarYObtenerDireccion(idDireccion, redirectAttributes);
            if (direccion == null) {
                return "redirect:/carrito";
            }

            // Crear el pedido
            Pedido pedido = pedidoService.realizarPedido(usuario.getUsuario(), formaPago, idDireccion);

            // Pasar los detalles del pedido al modelo
            model.addAttribute("pedido", pedido);

            return "user/pedidoConfirmacion"; // Plantilla Thymeleaf
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/carrito/confirmar-carrito";
        }
    }

    @GetMapping("/comprobante")
    public ResponseEntity<byte[]> descargarComprobante(@RequestParam("id") Long idPedido) {
        try {
            Pedido pedido = pedidoService.obtenerPedidoPorId(idPedido);
            byte[] pdfBytes = comprobanteDePagoPDF.generarComprobantePago(pedido);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprobante_" + idPedido + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
