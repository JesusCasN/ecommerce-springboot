package com.diplomado.tienda.service;

import com.diplomado.tienda.model.Pedido;

public interface PagoStrategy {
    boolean procesarPago(Pedido pedido);
}
