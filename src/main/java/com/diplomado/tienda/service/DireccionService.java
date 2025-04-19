package com.diplomado.tienda.service;

import com.diplomado.tienda.model.Direccion;
import com.diplomado.tienda.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface DireccionService {

    List<Direccion> obtenerDireccionesPorUsuario(Usuario usuario);

    Optional<Direccion> obtenerDireccionPorId(Long idDireccion);

    void eliminarDireccion(Long idDireccion);

    Direccion guardarDireccion(Usuario usuario, Direccion direccionDTO);
}
