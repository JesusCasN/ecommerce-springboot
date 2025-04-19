const inputBusqueda = document.getElementById("busqueda");
const contenedorSugerencias = document.createElement("div");
contenedorSugerencias.id = "sugerencias";
contenedorSugerencias.className = "list-group position-absolute w-100 z-3";
contenedorSugerencias.style.maxHeight = "200px";
contenedorSugerencias.style.overflowY = "auto";

// Insertar el contenedor justo después del input
inputBusqueda.parentNode.appendChild(contenedorSugerencias);

// Escucha cambios en el campo de búsqueda
inputBusqueda.addEventListener("input", function () {
    const query = this.value.trim();

    if (query.length === 0) {
        limpiarSugerencias();

        const urlParams = new URLSearchParams(window.location.search);
        const pagina = urlParams.get("pagina") || 0;
        const categoriaId = urlParams.get("categoriaId");

        let nuevaURL = `/productos?pagina=${pagina}`;
        if (categoriaId) {
            nuevaURL += `&categoriaId=${categoriaId}`;
        }

        window.location.href = nuevaURL;
        return;
    }

    fetch(`/productos/buscar?nombre=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(data => {
            mostrarSugerencias(data);
            actualizarProductos(data);
        })
        .catch(error => console.error("Error en la búsqueda:", error));
});

// Función para renderizar los productos buscados
function actualizarProductos(productos) {
    const contenedor = document.querySelector("#productos .row");
    contenedor.innerHTML = "";

    if (productos.length === 0) {
        contenedor.innerHTML = `<div class="col text-center"><p>No se encontraron productos.</p></div>`;
        return;
    }

    const csrfToken = document.getElementById("csrf-token")?.value || "";

    productos.forEach(producto => {
        const card = `
            <div class="col">
                <div class="card h-100 shadow-lg rounded">
                    <img class="product-image" src="/images/productosImg/${producto.imagen}" alt="Imagen del producto">
                    <div class="card-body">
                        <h5 class="card-title">${producto.nombre}</h5>
                        <p class="card-text text-muted">${producto.descripcion}</p>
                        <p class="card-text text-primary fw-bold">${producto.precio}.00 MXN</p>
                    </div>
                    <div class="card-footer">
                        <form action="/carrito/agregar" method="post">
                            <input type="hidden" name="_csrf" value="${csrfToken}">
                            <input type="hidden" name="productoId" value="${producto.id_producto}">
                            <input type="number" name="cantidad" value="1" min="1" class="form-control mb-2">
                            <button type="submit" class="btn btn-primary w-100">
                                <i class="fas fa-cart-plus"></i> Añadir al carrito
                            </button>
                        </form>
                    </div>
                </div>
            </div>`;
        contenedor.insertAdjacentHTML("beforeend", card);
    });
}

// Función para mostrar sugerencias debajo del input
function mostrarSugerencias(productos) {
    limpiarSugerencias();

    if (productos.length === 0) return;

    productos.forEach(producto => {
        const item = document.createElement("a");
        item.href = "#";
        item.classList.add("list-group-item", "list-group-item-action");
        item.textContent = producto.nombre;
        item.addEventListener("click", function (e) {
            e.preventDefault();
            inputBusqueda.value = producto.nombre;
            limpiarSugerencias();
            fetch(`/productos/buscar?nombre=${encodeURIComponent(producto.nombre)}`)
                .then(res => res.json())
                .then(actualizarProductos);
        });
        contenedorSugerencias.appendChild(item);
    });
}

// Función para limpiar las sugerencias del DOM
function limpiarSugerencias() {
    contenedorSugerencias.innerHTML = "";
}
