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
let idProductoAEliminar = null;

document.addEventListener("DOMContentLoaded", () => {
    console.log("1. DOM cargado correctamente.");

    initializeStorage();
    renderProductos();
    inicializarFiltros();

    const modal = document.getElementById("productModal");
    const modalTitulo = modal ? modal.querySelector(".modal__top h2") : null;
    const btnAbrir = document.getElementById("OpenModal");
    const btnCerrar = document.getElementById("CloseModal");
    const form = document.getElementById("productForm");
    const confirmModal = document.getElementById("confirmModal");
    const btnConfirmCancel = document.getElementById("btnConfirmCancel");
    const btnConfirmDelete = document.getElementById("btnConfirmDelete");

    const inputTitulo = document.getElementById("formTitulo");
    const inputArtista = document.getElementById("formArtista");
    const inputFormato = document.getElementById("formFormato");
    const inputPrecio = document.getElementById("formPrecio");
    const inputStock = document.getElementById("formStock");
    const inputPortada = document.getElementById("formPortada");

    document.getElementById("filterArtista")?.addEventListener("change", aplicarFiltros);
    document.getElementById("filterFormato")?.addEventListener("change", aplicarFiltros);

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

            if (e.target.classList.contains("p-card__delete")) {
                eliminarProducto(idProducto);
            }

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

    if (btnConfirmCancel && confirmModal) {
        btnConfirmCancel.addEventListener("click", () => {
            confirmModal.style.display = "none";
            idProductoAEliminar = null;
        });
    }

    if (btnConfirmDelete && confirmModal) {
        btnConfirmDelete.addEventListener("click", () => {
            if (idProductoAEliminar) {
                const productosActuales = getData(STORAGE_KEYS.productos);
                const productosFiltrados = productosActuales.filter(prod => prod.id !== idProductoAEliminar);

                saveData(STORAGE_KEYS.productos, productosFiltrados);
                renderProductos();

                confirmModal.style.display = "none";
                idProductoAEliminar = null;
            }
        });
    }

    window.addEventListener("click", (e) => {
        if (e.target === confirmModal) {
            confirmModal.style.display = "none";
            idProductoAEliminar = null;
        }
    });
});

function renderProductos(productosFiltrados = null) {
    const container = document.getElementById("discContainer");
    if (!container) return;

    const productosAVisualizar = productosFiltrados || getData(STORAGE_KEYS.productos);

    if (productosAVisualizar.length === 0) {
        container.innerHTML = `<p class="p-disc__empty">No hay productos para mostrar</p>`;
        return;
    }

    container.innerHTML = "";

    productosAVisualizar.forEach(prodData => {
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

function inicializarFiltros() {

    const productos = getData(STORAGE_KEYS.productos);
    const selectArtista = document.getElementById("filterArtista");
    
    if (!selectArtista) return;
    const artistasUnicos = [...new Set(productos.map(p => p.artista))];
    
    artistasUnicos.forEach(artista => {
        selectArtista.insertAdjacentHTML("beforeend", `<option value="${artista}">${artista}</option>`);
    });

}

function aplicarFiltros() {
    const productos = getData(STORAGE_KEYS.productos);
    
    const filtroArtista = document.getElementById("filterArtista").value;
    const filtroFormato = document.getElementById("filterFormato").value;
    const productosFiltrados = productos.filter(prod => {

        const coincideArtista = filtroArtista === "" || prod.artista === filtroArtista;
        const coincideFormato = filtroFormato === "" || prod.formato === filtroFormato;

        return coincideArtista && coincideFormato;
    });

    renderProductos(productosFiltrados);
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
    const confirmModal = document.getElementById("confirmModal");
    if (!confirmModal) return;

    idProductoAEliminar = id; 

    confirmModal.style.display = "flex"; 
}

// #endregion