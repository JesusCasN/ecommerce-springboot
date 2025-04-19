package com.diplomado.tienda.controller;

import com.diplomado.tienda.dto.ProductoDTO;
import com.diplomado.tienda.model.Producto;
import com.diplomado.tienda.model.Categoria;
import com.diplomado.tienda.repository.CategoriaRepository;
import com.diplomado.tienda.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductoController {


    private final CategoriaRepository categoriaRepository;
    private final ProductoService productoService;

    @GetMapping("/")
    public String listarProductos(@RequestParam(name = "categoriaId", required = false) Integer categoriaId,
                                  @RequestParam(name = "pagina", defaultValue = "0") int pagina,
                                  Model model) {
        cargarProductosYCategorias(pagina, categoriaId, model);
        return "index";
    }

    @GetMapping("/productos")
    public String mostrarProductos(@RequestParam(name = "categoriaId", required = false) Integer categoriaId,
                                   @RequestParam(name = "pagina", defaultValue = "0") int pagina,
                                   Model model) {
        cargarProductosYCategorias(pagina, categoriaId, model);
        return "user/productos";
    }


    @GetMapping("/productos/buscar")
    @ResponseBody
    public List<ProductoDTO> buscarProductoPorNombre(@RequestParam("nombre") String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    private void cargarProductosYCategorias(int pagina, Integer categoriaId, Model model) {
        int cantidadPorPagina = 9;
        Page<Producto> productosPage;

        if (categoriaId != null) {
            Categoria categoria = categoriaRepository.findById(categoriaId).orElse(null);
            if (categoria != null) {
                productosPage = productoService.buscarPorCategoriaPaginado(categoria, pagina, cantidadPorPagina);
            } else {
                productosPage = productoService.listarProductosPaginados(pagina, cantidadPorPagina);
            }
        } else {
            productosPage = productoService.listarProductosPaginados(pagina, cantidadPorPagina);
        }

        model.addAttribute("productos", productosPage.getContent());
        model.addAttribute("totalPaginas", productosPage.getTotalPages());
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("categorias", categoriaRepository.findAll());

    }
}
