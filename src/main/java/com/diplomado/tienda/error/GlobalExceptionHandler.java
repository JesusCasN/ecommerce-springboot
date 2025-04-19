package com.diplomado.tienda.error;

import com.diplomado.tienda.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Manejador global de excepciones para controlar errores personalizados y generales
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Manejo de excepciones de tipo "no encontrado"
     */
    @ExceptionHandler({
            ProductoNoEncontradoException.class,
            UsuarioNoEncontradoException.class,
            CategoriaNoEncontradaException.class,
            DireccionNoEncontradaException.class,
            FormaDePagoNoEncontradaException.class,
            PedidoNoEncontradoException.class,
            RolNoEncontradoException.class,
            ProductoNoEncontradoEnCarritoException.class,
    })
    public ModelAndView handleNoEncontrado(RuntimeException ex) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.setStatus(HttpStatus.NOT_FOUND);
        mav.addObject("mensaje", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNoHandlerFound(NoHandlerFoundException ex) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.setStatus(HttpStatus.NOT_FOUND);
        mav.addObject("mensaje", "La página que estás buscando no existe.");
        return mav;
    }

    /**
     * Producto no disponible para la compra
     */
    @ExceptionHandler(ProductoNoDisponibleException.class)
    public ModelAndView handleProductoNoDisponible(ProductoNoDisponibleException ex) {
        ModelAndView mav = new ModelAndView("error/producto-no-disponible");
        mav.setStatus(HttpStatus.CONFLICT); // 409 Conflict
        mav.addObject("mensaje", ex.getMessage());
        return mav;
    }

    /**
     * Usuario no autenticado o sin permisos
     */
    @ExceptionHandler(UsuarioNoAutenticadoException.class)
    public ModelAndView handleNoAutenticado(UsuarioNoAutenticadoException ex) {
        ModelAndView mav = new ModelAndView("error/403");
        mav.setStatus(HttpStatus.FORBIDDEN); // 403 Forbidden
        mav.addObject("mensaje", ex.getMessage());
        return mav;
    }

    /**
     * Manejo general de errores inesperados
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex) {
        ModelAndView mav = new ModelAndView("error/500");
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        mav.addObject("mensaje", "Ocurrió un error inesperado. Inténtalo más tarde.");
        return mav;
    }
}
