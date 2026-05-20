// #region --------------------- NAVEGACIÓN SIDEBAR ---------------------

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

const sidebar = document.querySelector('.sidebar');
const sidebarToggle = document.getElementById('sidebarToggle');

if (sidebarToggle && sidebar) {
    sidebarToggle.addEventListener('click', (e) => {
        e.stopPropagation();
        sidebar.classList.toggle('is-open');
        
        const icon = sidebarToggle.querySelector('.material-symbols-rounded');
        if (icon) {
            icon.textContent = sidebar.classList.contains('is-open') ? 'close' : 'menu';
        }
    });

    const navItems = document.querySelectorAll('.nav__item');
    navItems.forEach(item => {
        item.addEventListener('click', () => {
            sidebar.classList.remove('is-open');
            const icon = sidebarToggle.querySelector('.material-symbols-rounded');
            if (icon) icon.textContent = 'menu';
        });
    });
}


});

// #endregion

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

// #region --------------------- DASHBOARD ---------------------

document.addEventListener('DOMContentLoaded', () => {
    
    const KEYS = {
        pedidos: "74min_pedidos",
        productos: "74min_productos",
        perfil: "74min_perfil",
        trabajadores: "74min_trabajadores"
    };

    const welcomeName = document.getElementById('welcomeName');
    const kpiIngresos = document.getElementById('kpiIngresos');
    const kpiStock = document.getElementById('kpiStock');
    const kpiRuta = document.getElementById('kpiRuta');
    const kpiClientes = document.getElementById('kpiClientes');

    function loadProfileName() {
        const perfil = JSON.parse(sessionStorage.getItem(KEYS.perfil));
        
        if (perfil && perfil.nombre) {
            welcomeName.textContent = perfil.nombre;
        } else {
            welcomeName.textContent = "Josiah";
        }
    }

    function calculateDashboardKPIs() {
        const pedidos = JSON.parse(sessionStorage.getItem(KEYS.pedidos)) || [];
        const productos = JSON.parse(sessionStorage.getItem(KEYS.productos)) || [];

        const totalIngresos = pedidos.reduce((acc, p) => acc + Number(p.totalAmount || 0), 0);
        kpiIngresos.textContent = `${totalIngresos.toFixed(2)} €`;

        const stockCritico = productos.filter(p => Number(p.stock) < 5).length;
        kpiStock.textContent = stockCritico;

        const pedidosEnRuta = pedidos.filter(p => p.status === "Enviado").length;
        kpiRuta.textContent = pedidosEnRuta;

        const clientesConPedido = pedidos.map(p => p.clientId);
        const clientesUnicos = new Set(clientesConPedido).size;
        kpiClientes.textContent = clientesUnicos;
    }

    function createActivityFeed() {
        const feedContainer = document.getElementById('activityFeed');
        const pedidos = JSON.parse(sessionStorage.getItem(KEYS.pedidos)) || [];
        const trabajadores = JSON.parse(sessionStorage.getItem(KEYS.trabajadores)) || [];

        feedContainer.innerHTML = '';

        if (pedidos.length === 0) {
            feedContainer.innerHTML = '<p class="text">No hay movimientos registrados.</p>';
            return;
        }

        const lastOrders = pedidos.slice(-3).reverse();

        lastOrders.forEach(pedido => {
            const worker = trabajadores.find(t => String(t.id) === String(pedido.employeeId))?.name || "Sistema";
            
            const item = document.createElement('div');
            item.className = 'd-activity__item';
            item.innerHTML = `
                <div class="d-activity__icon">
                    <span class="material-symbols-rounded">assignment_turned_in</span>
                </div>
                <div>
                    <p class="text">
                        Pedido <strong>#${pedido.id}</strong> registrado con éxito.
                    </p>
                    <p class="text">Gestionado por el trabajador: <strong>${worker}</strong></p>
                </div>
            `;
            feedContainer.appendChild(item);
        });
    }

    function createTopProducts() {
        const listContainer = document.getElementById('topProductsList');
        const pedidos = JSON.parse(sessionStorage.getItem(KEYS.pedidos)) || [];
        const productos = JSON.parse(sessionStorage.getItem(KEYS.productos)) || [];

        listContainer.innerHTML = '';

        const sellsMap = {};
        pedidos.forEach(pedido => {
            if (pedido.details) {
                pedido.details.forEach(line => {
                    sellsMap[line.productId] = (sellsMap[line.productId] || 0) + Number(line.quantity);
                });
            }
        });

        const productsSold = productos.map(prod => {
            return {
                ...prod,
                sold: sellsMap[prod.id] || 0
            };
        });

        const top3 = productsSold.sort((a, b) => b.sold - a.sold).slice(0, 3);

        top3.forEach(album => {
            const item = document.createElement('div');
            item.className = 'd-top__item';
            item.innerHTML = `
                <img src="${album.portada || 'https://via.placeholder.com/50'}" alt="${album.titulo}" class="d-top__thumb">
                <div class="d-top__info">
                    <p class="text">${album.titulo}</p>
                    <p class="text">${album.artista} (${album.formato})</p>
                </div>
                <div class="d-top__badge">${album.sold} ud${album.sold !== 1 ? 's' : ''}</div>
            `;
            listContainer.appendChild(item);
        });
    }

    loadProfileName();
    calculateDashboardKPIs();
    createActivityFeed();
    createTopProducts();
});

// #endregion

