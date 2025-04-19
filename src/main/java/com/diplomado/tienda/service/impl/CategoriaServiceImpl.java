package com.diplomado.tienda.service.impl;

import com.diplomado.tienda.exception.CategoriaNoEncontradaException;
import com.diplomado.tienda.model.Categoria;
import com.diplomado.tienda.repository.CategoriaRepository;
import com.diplomado.tienda.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> obtenerTodasLasCategorias() {
        log.info("\n 📂 Buscando todas las categorías... \n");

        List<Categoria> categorias = categoriaRepository.findAll();

        if (categorias.isEmpty()) {
            log.warn("\n ⚠️ No se encontraron categorías en la base de datos. \n");
            throw new CategoriaNoEncontradaException("No se encontraron categorías.");
        }

        log.info("\n ✅ Se encontraron {} categorías. \n", categorias.size());
        return categorias;
    }
}
