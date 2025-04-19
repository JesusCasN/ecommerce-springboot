package com.diplomado.tienda.service.impl;

import com.diplomado.tienda.exception.FormaDePagoNoEncontradaException;
import com.diplomado.tienda.model.FormasDePago;
import com.diplomado.tienda.repository.FormasDePagoRepository;
import com.diplomado.tienda.service.FormasDePagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormasDePagoServiceImpl implements FormasDePagoService {

    private final FormasDePagoRepository formasDePagoRepository;

    @Override
    public List<FormasDePago> listarFormasDePago() {
        log.info("\n 💳 Solicitando lista de todas las formas de pago... \n");

        List<FormasDePago> formas = formasDePagoRepository.findAll();

        log.info("\n ✅ Se encontraron {} formas de pago registradas. \n", formas.size());
        return formas;
    }

    @Override
    public FormasDePago obtenerPorId(Integer idFormaPago) {
        log.info("\n 🔍 Buscando forma de pago con ID: {} \n", idFormaPago);

        return formasDePagoRepository.findById(idFormaPago)
                .orElseThrow(() -> {
                    log.warn("\n ❌ Forma de pago con ID {} no encontrada. \n", idFormaPago);
                    return new FormaDePagoNoEncontradaException("Forma de pago no encontrada");
                });
    }
}
