package com.diplomado.tienda.service;

import com.diplomado.tienda.model.FormasDePago;

import java.util.List;

public interface FormasDePagoService {

    List<FormasDePago> listarFormasDePago();

    FormasDePago obtenerPorId(Integer idFormaPago);
}
