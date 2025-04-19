package com.diplomado.tienda.exception;

public class ProductoNoEncontradoEnCarritoException extends RuntimeException{

    public ProductoNoEncontradoEnCarritoException(String mensaje) {
        super(mensaje);
    }

}
