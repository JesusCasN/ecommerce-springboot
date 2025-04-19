package com.diplomado.tienda.exception;

public class UsuarioNoAutenticadoException extends RuntimeException {
    public UsuarioNoAutenticadoException(String message) {
        super(message);
    }
}
