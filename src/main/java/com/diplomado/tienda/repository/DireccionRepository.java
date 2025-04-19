package com.diplomado.tienda.repository;

import com.diplomado.tienda.model.Direccion;
import com.diplomado.tienda.model.EstadoDireccion;
import com.diplomado.tienda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {


    List<Direccion> findByUsuarioAndCondicion(Usuario usuario, EstadoDireccion condicion);


}
