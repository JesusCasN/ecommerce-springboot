package com.diplomado.tienda.service.impl;

import com.diplomado.tienda.exception.DireccionNoEncontradaException;
import com.diplomado.tienda.exception.UsuarioNoAutenticadoException;
import com.diplomado.tienda.model.Direccion;
import com.diplomado.tienda.model.EstadoDireccion;
import com.diplomado.tienda.model.Usuario;
import com.diplomado.tienda.repository.DireccionRepository;
import com.diplomado.tienda.service.DireccionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DireccionServiceImpl implements DireccionService {

    private final DireccionRepository direccionRepository;

    @Override
    public List<Direccion> obtenerDireccionesPorUsuario(Usuario usuario) {
        if (usuario == null) {
            log.warn("\n ⚠️ Se intentó obtener direcciones sin usuario autenticado. \n");
            throw new UsuarioNoAutenticadoException("Usuario no autenticado");
        }

        log.info("\n 📬 Buscando direcciones activas para el usuario '{}' \n", usuario.getUsuario());
        List<Direccion> direcciones = direccionRepository.findByUsuarioAndCondicion(usuario, EstadoDireccion.ACTIVO);
        log.info("\n ✅ Se encontraron {} direcciones activas para el usuario '{}' \n", direcciones.size(), usuario.getUsuario());

        return direcciones;
    }

    @Override
    public Optional<Direccion> obtenerDireccionPorId(Long idDireccion) {
        log.info("\n 🔍 Buscando dirección con ID: {} \n", idDireccion);
        return direccionRepository.findById(idDireccion);
    }

    @Transactional
    @Override
    public Direccion guardarDireccion(Usuario usuario, Direccion direccion) {
        if (usuario == null) {
            log.warn("\n ⚠️ Se intentó guardar una dirección sin usuario autenticado. \n");
            throw new UsuarioNoAutenticadoException("Usuario no autenticado");
        }

        direccion.setUsuario(usuario);
        Direccion direccionGuardada = direccionRepository.save(direccion);

        log.info("\n 💾 Dirección guardada correctamente para el usuario '{}': ID={} \n", usuario.getUsuario(), direccionGuardada.getIdDireccion());
        return direccionGuardada;
    }

    @Transactional
    @Override
    public void eliminarDireccion(Long idDireccion) {
        log.info("\n 🗑️ Solicitando eliminación lógica de la dirección con ID {} \n", idDireccion);

        Direccion direccion = direccionRepository.findById(idDireccion)
                .orElseThrow(() -> {
                    log.warn("\n ❌ Dirección con ID {} no encontrada. \n", idDireccion);
                    return new DireccionNoEncontradaException("Dirección no encontrada");
                });

        direccion.setCondicion(EstadoDireccion.ELIMINADO);
        direccionRepository.save(direccion);

        log.info("\n ✅ Dirección con ID {} marcada como ELIMINADA. \n", idDireccion);
    }
}
