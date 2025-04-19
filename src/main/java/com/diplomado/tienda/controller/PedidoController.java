package com.diplomado.tienda.controller;

import com.diplomado.tienda.model.Pedido;
import com.diplomado.tienda.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // Mostrar historial de pedidos del usuario autenticado
    @GetMapping
    public String listarPedidosUsuario(@AuthenticationPrincipal User user,
                                       @RequestParam("fechaInicio") Optional<LocalDate> fechaInicio,
                                       @RequestParam("fechaFin") Optional<LocalDate> fechaFin,
                                       Model model) {

        String email = user.getUsername();
        Optional<Timestamp> inicio = fechaInicio.map(date -> Timestamp.valueOf(date.atStartOfDay()));
        Optional<Timestamp> fin = fechaFin.map(date -> Timestamp.valueOf(date.atTime(23, 59, 59)));

        List<Pedido> pedidos = pedidoService.obtenerHistorialUsuario(email, inicio, fin);

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("fechaInicio", fechaInicio.orElse(null));
        model.addAttribute("fechaFin", fechaFin.orElse(null));

        return "user/pedidos";
    }

    // Ver detalles de un pedido específico del usuario
    @GetMapping("/detalle/{id}")
    public String verDetallesPedido(@PathVariable("id") Long id,
                                    @AuthenticationPrincipal User user,
                                    Model model) {

        Pedido pedido = pedidoService.obtenerPedidoPorId(id);

        // Seguridad: evitar que un usuario acceda a pedidos de otros
        if (!pedido.getUsuario().getEmail().equals(user.getUsername())) {
            return "redirect:/usuario/pedidos?error=acceso-denegado";
        }

        model.addAttribute("pedido", pedido);
        return "user/detallePedido";
    }
}
