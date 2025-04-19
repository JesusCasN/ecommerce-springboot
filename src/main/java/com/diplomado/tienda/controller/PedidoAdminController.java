package com.diplomado.tienda.controller;

import com.diplomado.tienda.model.Pedido;
import com.diplomado.tienda.service.impl.PedidoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;


import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/pedidos")
public class PedidoAdminController {

    private final PedidoServiceImpl pedidoServiceImpl;

    // Método para listar todos los pedidos con filtros
    @GetMapping
    public String listarPedidos(@RequestParam("fechaInicio") Optional<LocalDate> fechaInicio,
                                @RequestParam("fechaFin") Optional<LocalDate> fechaFin,
                                @RequestParam("usuario") Optional<String> usuario,
                                Model model) {
        // Convertir LocalDate a Timestamp
        Optional<Timestamp> inicio = fechaInicio.map(date -> Timestamp.valueOf(date.atStartOfDay()));  // Inicio del día
        Optional<Timestamp> fin = fechaFin.map(date -> Timestamp.valueOf(date.atTime(23, 59, 59)));  // Final del día

        // Obtener los pedidos filtrados por fecha y usuario
        List<Pedido> pedidos = pedidoServiceImpl.filtrarPedidos(inicio, fin, usuario);

        // Agregar los atributos al modelo para la vista
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("fechaInicio", fechaInicio.orElse(null));
        model.addAttribute("fechaFin", fechaFin.orElse(null));
        model.addAttribute("usuario", usuario.orElse(""));

        return "admin/listaPedidos";  // Vista de lista de pedidos
    }

    @GetMapping("/detalle/{id}")
    public String verDetalles(@PathVariable("id") Long id, Model model) {
        // Buscar el pedido por ID
        Pedido pedido = pedidoServiceImpl.obtenerPedidoPorId(id);

        // Verificar si el pedido existe
        if (pedido == null) {
            // Redirigir a la lista de pedidos si no se encuentra el pedido
            return "redirect:/admin/pedidos";
        }

        // Agregar el pedido al modelo
        model.addAttribute("pedido", pedido);

        // Retornar la vista de detalles
        return "admin/detallePedido";
    }
}
