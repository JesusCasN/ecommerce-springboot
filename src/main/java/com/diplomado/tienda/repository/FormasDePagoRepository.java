package com.diplomado.tienda.repository;

import com.diplomado.tienda.model.FormasDePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormasDePagoRepository extends JpaRepository<FormasDePago, Integer> {

}
