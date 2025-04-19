package com.diplomado.tienda.service;

import com.diplomado.tienda.model.Pedido;
import com.diplomado.tienda.service.impl.PagoPorDeposito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PagoPorDepositoTest {

    @Test
    @DisplayName("procesarPago debe retornar true siempre para PagoPorDeposito")
    void procesarPago_retornaTrue() {
        // Arrange
        PagoPorDeposito pagoPorDeposito = new PagoPorDeposito();
        Pedido pedido = new Pedido(); // puedes configurar más datos si es necesario

        // Act
        boolean resultado = pagoPorDeposito.procesarPago(pedido);

        // Assert
        assertTrue(resultado);
    }
}
