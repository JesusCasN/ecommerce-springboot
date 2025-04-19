package com.diplomado.tienda.service.impl;

import com.diplomado.tienda.model.Pedido;
import com.diplomado.tienda.service.PagoStrategy;
import org.springframework.stereotype.Service;

@Service
public class PagoPorDeposito implements PagoStrategy {
    @Override
    public boolean procesarPago(Pedido pedido) {
        // Implementación del procesamiento de pago
        System.out.println("Pago realizado con deposito...");
        return true;
    }
}
