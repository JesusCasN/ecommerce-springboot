package com.diplomado.tienda.service;

import com.diplomado.tienda.model.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {

    List<Rol> listarRoles();

    Optional<Rol> obtenerRolPorId(Integer idRol);

    Optional<Rol> findByNombre(String nombre);

}
