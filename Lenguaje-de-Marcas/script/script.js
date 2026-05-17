document.addEventListener('DOMContentLoaded', () => {
    const navItems = document.querySelectorAll('.nav__item');

    const activeItem = document.querySelector('.nav__item.active');
    if (activeItem) {
        const img = activeItem.querySelector('img');
        if (img) img.src = img.dataset.active;
    }

    navItems.forEach(item => {
        item.addEventListener('click', () => {

            navItems.forEach(i => {
                const img = i.querySelector('img');
                i.classList.remove('active');
                if (img) img.src = img.dataset.default;
            });

            item.classList.add('active');
            const activeImg = item.querySelector('img');
            if (activeImg) activeImg.src = activeImg.dataset.active;

            window.location.href = item.dataset.href;
        });
    });
});

// #region --------------------- Carga de datos PRODUCTS.JS ---------------------

let idProductoEnEdicion = null;

document.addEventListener("DOMContentLoaded", () => {
    console.log("1. DOM cargado correctamente.");

    initializeStorage();
    renderProductos();

    const modal = document.getElementById("productModal");
    const modalTitulo = modal ? modal.querySelector(".modal__top h2") : null;
    const btnAbrir = document.getElementById("OpenModal");
    const btnCerrar = document.getElementById("CloseModal");
    const form = document.getElementById("productForm");

    const inputTitulo = document.getElementById("formTitulo");
    const inputArtista = document.getElementById("formArtista");
    const inputFormato = document.getElementById("formFormato");
    const inputPrecio = document.getElementById("formPrecio");
    const inputStock = document.getElementById("formStock");
    const inputPortada = document.getElementById("formPortada");

    if (btnAbrir && modal) {
        btnAbrir.addEventListener("click", () => {
            idProductoEnEdicion = null;
            if (modalTitulo) modalTitulo.textContent = "Añadir Nuevo Producto";
            if (form) form.reset();
            modal.style.display = "flex";
        });
    }

    if (btnCerrar && modal) {
        btnCerrar.addEventListener("click", () => {
            modal.style.display = "none";
            if (form) form.reset();
        });
    }

    window.addEventListener("click", (e) => {
        if (e.target === modal) {
            modal.style.display = "none";
            if (form) form.reset();
        }
    });

    if (inputPrecio) {
        inputPrecio.addEventListener("input", (e) => {
            let value = e.target.value.replace(/[^0-9.,]/g, "").replace(/,/g, ".");
            const puntos = value.split(".");
            if (puntos.length > 2) {
                value = puntos[0] + "." + puntos.slice(1).join("");
            }
            e.target.value = value;
        });
    }

    if (inputStock) {
        inputStock.addEventListener("input", (e) => {
            e.target.value = e.target.value.replace(/[^0-9]/g, "");
        });
    }

    const container = document.getElementById("discContainer");
    if (container) {
        container.addEventListener("click", (e) => {
            const idProducto = e.target.getAttribute("data-id");

            // Caso A: Click en Eliminar
            if (e.target.classList.contains("p-card__delete")) {
                eliminarProducto(idProducto);
            }

            // Caso B: Click en Editar
            if (e.target.classList.contains("p-card__edit")) {
                abrirModalEditar(idProducto, modal, modalTitulo, inputTitulo, inputArtista, inputFormato, inputPrecio, inputStock, inputPortada);
            }
        });
    }

    if (form) {
        form.addEventListener("submit", (e) => {
            e.preventDefault();

            const titulo = inputTitulo.value.trim();
            const artista = inputArtista.value.trim();
            const formatoSelect = inputFormato.value;
            const formatoFormateado = formatoSelect === "CD" ? "CD" : "Vinilo";

            const precio = parseFloat(inputPrecio.value);
            const stock = parseInt(inputStock.value);
            const portada = inputPortada.value.trim();

            if (!titulo || !artista || isNaN(precio) || isNaN(stock)) {
                alert("Por favor, rellena correctamente todos los campos obligatorios");
                return;
            }

            const productosActuales = getData(STORAGE_KEYS.productos);

            if (idProductoEnEdicion === null) {
                // MODO: CREAR NUEVO
                const nuevoId = Date.now().toString();
                const nuevoDiscoData = {
                    id: nuevoId,
                    titulo: titulo,
                    artista: artista,
                    genero: "General",
                    formato: formatoFormateado,
                    precio: precio,
                    stock: stock,
                    portada: portada
                };
                productosActuales.push(nuevoDiscoData);
            } else {
                // MODO: EDITAR EXISTENTE
                const index = productosActuales.findIndex(prod => prod.id === idProductoEnEdicion);
                if (index !== -1) {
                    productosActuales[index].titulo = titulo;
                    productosActuales[index].artista = artista;
                    productosActuales[index].formato = formatoFormateado;
                    productosActuales[index].precio = precio;
                    productosActuales[index].stock = stock;
                    productosActuales[index].portada = portada;
                }
            }

            saveData(STORAGE_KEYS.productos, productosActuales);

            renderProductos();
            form.reset();
            modal.style.display = "none";
            idProductoEnEdicion = null;
        });
    }
});

function renderProductos() {
    const container = document.getElementById("discContainer");
    if (!container) return;

    const productosRaw = getData(STORAGE_KEYS.productos);

    if (productosRaw.length === 0) {
        container.innerHTML = `<p class="p-disc__empty">No hay productos en el catálogo</p>`;
        return;
    }

    container.innerHTML = "";

    productosRaw.forEach(prodData => {
        const productoInstancia = new Producto(
            prodData.id,
            prodData.titulo,
            prodData.artista,
            prodData.genero || "General",
            prodData.formato,
            prodData.precio,
            prodData.stock,
            prodData.portada
        );

        container.insertAdjacentHTML("beforeend", productoInstancia.createCardHTML());
    });
}

function abrirModalEditar(id, modal, modalTitulo, inputTitulo, inputArtista, inputFormato, inputPrecio, inputStock, inputPortada) {
    const productos = getData(STORAGE_KEYS.productos);
    const productoAEditar = productos.find(prod => prod.id === id);

    if (!productoAEditar) return;

    idProductoEnEdicion = id;

    if (modalTitulo) modalTitulo.textContent = "Editar Producto";

    inputTitulo.value = productoAEditar.titulo;
    inputArtista.value = productoAEditar.artista;
    inputFormato.value = productoAEditar.formato;
    inputPrecio.value = productoAEditar.precio;
    inputStock.value = productoAEditar.stock;
    inputPortada.value = productoAEditar.portada;

    if (modal) modal.style.display = "flex";
}

function eliminarProducto(id) {
    if (confirm("¿Estás seguro de que deseas eliminar este disco?")) {
        const productosActuales = getData(STORAGE_KEYS.productos);
        const productosFiltrados = productosActuales.filter(prod => prod.id !== id);

        saveData(STORAGE_KEYS.productos, productosFiltrados);
        renderProductos();
    }
}

// #endregion