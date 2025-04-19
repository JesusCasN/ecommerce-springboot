package com.diplomado.tienda.controller;

import com.diplomado.tienda.model.Categoria;
import com.diplomado.tienda.model.Producto;
import com.diplomado.tienda.service.CategoriaService;
import com.diplomado.tienda.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/productos")
@PreAuthorize("hasAuthority('ADMIN')")  // Restringir todo el controlador al rol ADMIN
public class ProductoAdminController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoAdminController.class);

    private final ProductoService productoService;
    private final CategoriaService categoriaService;


    // Mostrar todos los productos para gestionar
    @GetMapping
    public String listarProductosAdmin(Model model) {
        List<Producto> productos = productoService.listarProductoPorStock();
        model.addAttribute("productos", productos);
        return "admin/listaProductos";  // Nombre de la vista para listar los productos
    }

    @GetMapping("/nombre")
    public String listarProductosPorNombre(@RequestParam(name = "nombre", required = false) String nombre,
                                           Model model) {
        List<Producto> productos;
        if (nombre != null && !nombre.isEmpty()) {
            productos = productoService.buscarPorNombreProducto(nombre);
        } else {
            productos = productoService.listarProductos();
        }
        model.addAttribute("productos", productos);
        return "admin/listaProductos";
    }

    // Mostrar formulario para crear un nuevo producto
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoProducto(Model model) {
        List<Categoria> categorias = categoriaService.obtenerTodasLasCategorias();  // Obtener las categorías
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categorias);  // Pasar las categorías disponibles al modelo
        return "admin/nuevoProducto";  // Nombre de la vista para crear un nuevo producto
    }

    @PostMapping("/guardar")
    public String guardarProducto(@Valid @ModelAttribute Producto producto,
                                  @RequestParam("file") MultipartFile file,
                                  RedirectAttributes redirectAttributes) {

        // Verificar si se ha subido un archivo
        if (!file.isEmpty()) {
            try {
                // Validar el tipo de archivo (por ejemplo, solo imágenes JPEG y PNG)
                String fileType = file.getContentType();
                if (fileType == null || !fileType.startsWith("image/")) {
                    throw new IllegalArgumentException("Solo se permiten archivos de imagen.");
                }

                // Ruta donde se guardará la imagen
                String uploadDir = "src/main/resources/static/images/productosImg/";
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path path = Paths.get(uploadDir + fileName);

                // Guardar archivo en la carpeta
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                // Guardar solo el nombre del archivo en la base de datos
                producto.setImagen(fileName);
            } catch (IOException e) {
                logger.error("Error al guardar la imagen: {}", e.getMessage(), e);
                redirectAttributes.addFlashAttribute("error", "Error al guardar la imagen.");
                // Agregar una notificación de error para el usuario
                return "admin/nuevoProducto";
            } catch (IllegalArgumentException e) {
                logger.error("Error de archivo inválido: {}", e.getMessage(), e);
                redirectAttributes.addFlashAttribute("error", "Archivo inválido. Solo se permiten imágenes.");
                // En caso de archivo inválido
                return "admin/nuevoProducto";
            }
        }

        // Guardar el producto en la base de datos
        productoService.guardarProducto(producto);

        return "redirect:/admin/productos";  // Redirigir a la lista de productos después de guardar
    }

    // Mostrar formulario para editar un producto existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarProducto(@PathVariable("id") Long id,
                                                  Model model) {

        Producto producto = productoService.obtenerProductoPorId(id);
        model.addAttribute("producto", producto);
        List<Categoria> categorias = categoriaService.obtenerTodasLasCategorias();  // Obtener categorías para editar
        model.addAttribute("categorias", categorias);
        return "admin/editarProducto";  // Nombre de la vista para editar un producto
    }
    // Actualizar el producto existente
    @PostMapping("/actualizar")
    public String actualizarProducto(@ModelAttribute Producto producto) {
        productoService.actualizarProducto(producto);
        return "redirect:/admin/productos";  // Redirigir a la lista de productos después de actualizar
    }

    // Eliminar un producto por ID
    @PostMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return "redirect:/admin/productos";  // Redirigir a la lista de productos después de eliminar
    }
}
