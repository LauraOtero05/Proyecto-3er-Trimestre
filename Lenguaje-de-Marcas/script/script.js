// #region --------------------- NAVEGACIÓN SIDEBAR ---------------------

document.addEventListener('DOMContentLoaded', () => {
    const navItems = document.querySelectorAll('.nav__item');
    const sidebar = document.querySelector('.sidebar');
    const sidebarToggle = document.getElementById('sidebarToggle');

    const currentPath = window.location.pathname.split('/').pop() || 'dashboard.html';

    navItems.forEach(item => {
        const img = item.querySelector('img');
        if (img) img.src = img.dataset.default;

        if (item.dataset.href === currentPath) {
            item.classList.add('active');
            if (img) img.src = img.dataset.active;
        }

        item.addEventListener('click', () => {
            window.location.href = item.dataset.href;
        });
    });

    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', (e) => {
            e.stopPropagation();
            sidebar.classList.toggle('is-open');

            const icon = sidebarToggle.querySelector('.material-symbols-rounded');
            if (icon) {
                icon.textContent = sidebar.classList.contains('is-open') ? 'close' : 'menu';
            }
        });

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

// #region --------------------- PRODUCTS ---------------------

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

            if (e.target.classList.contains("button__secondary--red")) {
                eliminarProducto(idProducto);
            }

            if (e.target.classList.contains("button__secondary--orange")) {
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
            inicializarFiltros();
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
                inicializarFiltros();

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
    
    selectArtista.innerHTML = '<option value="">Artista (Todos)</option>';

    const artistasUnicos = [...new Set(productos.map(p => p.artista).filter(Boolean))].sort();

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


// #region --------------------- SUPPLIERS ---------------------

let idProveedorEnEdicion = null;
let idProveedorAEliminar = null;

document.addEventListener("DOMContentLoaded", () => {
    console.log("1. DOM de Proveedores cargado correctamente.");

    initializeStorage();

    poblarFiltros();
    renderProveedores();

    const modal = document.getElementById("supplierModal");
    const modalTitulo = document.getElementById("supplierModalTitle");
    const btnAbrir = document.getElementById("OpenModalSuppliers");
    const btnCerrar = document.getElementById("CloseModalSuppliers");
    const form = document.getElementById("supplierForm");

    const deleteModal = document.getElementById("deleteConfirmModal");
    const btnCloseDelete = document.getElementById("CloseDeleteModal");
    const btnCancelDelete = document.getElementById("btnCancelDelete");
    const btnConfirmDelete = document.getElementById("btnConfirmDelete");

    const selectFilterId = document.getElementById("filterProveedorID");
    const selectFilterName = document.getElementById("filterProveedorName");
    const selectFilterPais = document.getElementById("filterProveedorPais");

    const inputName = document.getElementById("formSupplierName");
    const inputPhone = document.getElementById("formSupplierPhone");
    const inputEmail = document.getElementById("formSupplierEmail");
    const inputCity = document.getElementById("formSupplierCity");
    const inputCountry = document.getElementById("formSupplierCountry");
    const inputStatus = document.getElementById("formSupplierStatus");

    const ejecutarFiltro = () => {
        const valId = selectFilterId ? selectFilterId.value : "";
        const valName = selectFilterName ? selectFilterName.value : "";
        const valPais = selectFilterPais ? selectFilterPais.value : "";

        const proveedoresTodos = getData(STORAGE_KEYS.proveedores);

        const proveedoresFiltrados = proveedoresTodos.filter(prov => {
            const cumpleId = valId === "" || prov.id === valId;
            const cumpleName = valName === "" || prov.name === valName;
            const cumplePais = valPais === "" || prov.country === valPais;
            return cumpleId && cumpleName && cumplePais;
        });

        renderProveedores(proveedoresFiltrados);
    };

    if (selectFilterId) selectFilterId.addEventListener("change", ejecutarFiltro);
    if (selectFilterName) selectFilterName.addEventListener("change", ejecutarFiltro);
    if (selectFilterPais) selectFilterPais.addEventListener("change", ejecutarFiltro);


    if (btnAbrir && modal) {
        btnAbrir.addEventListener("click", () => {
            idProveedorEnEdicion = null;
            if (modalTitulo) modalTitulo.textContent = "Añadir Nuevo Proveedor";
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

    const cerrarModalBorrado = () => {
        if (deleteModal) deleteModal.style.display = "none";
        idProveedorAEliminar = null;
    };

    if (btnCloseDelete) btnCloseDelete.addEventListener("click", cerrarModalBorrado);
    if (btnCancelDelete) btnCancelDelete.addEventListener("click", cerrarModalBorrado);

    if (btnConfirmDelete) {
        btnConfirmDelete.addEventListener("click", () => {
            if (idProveedorAEliminar) {
                ejecutarEliminacionReal(idProveedorAEliminar);
                cerrarModalBorrado();
            }
        });
    }

    window.addEventListener("click", (e) => {
        if (e.target === modal) {
            modal.style.display = "none";
            if (form) form.reset();
        }
        if (e.target === deleteModal) {
            cerrarModalBorrado();
        }
    });

    const tableBody = document.getElementById("suppliersTable");
    if (tableBody) {
        tableBody.addEventListener("click", (e) => {
            const idProveedor = e.target.getAttribute("data-id");
            if (!idProveedor) return;

            if (e.target.classList.contains("s-btn__delete") || e.target.parentElement.classList.contains("s-btn__delete")) {
                idProveedorAEliminar = idProveedor;
                if (deleteModal) deleteModal.style.display = "flex";
            }

            if (e.target.classList.contains("s-btn__edit") || e.target.parentElement.classList.contains("s-btn__edit")) {
                abrirModalEditarProveedor(
                    idProveedor, modal, modalTitulo,
                    inputName, inputPhone, inputEmail,
                    inputCity, inputCountry, inputStatus
                );
            }
        });
    }

    if (form) {
        form.addEventListener("submit", (e) => {
            e.preventDefault();

            const nombre = inputName.value.trim();
            const telefono = inputPhone.value.trim();
            const email = inputEmail.value.trim();
            const ciudad = inputCity.value.trim();
            const pais = inputCountry.value.trim();
            const estado = inputStatus.value;

            if (!nombre || !telefono || !email || !ciudad || !pais) {
                alert("Por favor, rellena todos los campos obligatorios.");
                return;
            }

            const proveedoresActuales = getData(STORAGE_KEYS.proveedores);

            if (idProveedorEnEdicion === null) {
                let siguienteNumero = 1;

                if (proveedoresActuales.length > 0) {
                    const idsNumericos = proveedoresActuales.map(p => {
                        const numero = parseInt(p.id.replace("P", ""), 10);
                        return isNaN(numero) ? 0 : numero;
                    });
                    siguienteNumero = Math.max(...idsNumericos) + 1;
                }

                const nuevoId = "P" + siguienteNumero.toString().padStart(3, "0");

                const nuevoProveedor = {
                    id: nuevoId,
                    name: nombre,
                    phone: telefono,
                    email: email,
                    city: ciudad,
                    country: pais,
                    status: estado
                };
                proveedoresActuales.push(nuevoProveedor);
            } else {
                const index = proveedoresActuales.findIndex(prov => prov.id === idProveedorEnEdicion);
                if (index !== -1) {
                    proveedoresActuales[index].name = nombre;
                    proveedoresActuales[index].phone = telefono;
                    proveedoresActuales[index].email = email;
                    proveedoresActuales[index].city = ciudad;
                    proveedoresActuales[index].country = pais;
                    proveedoresActuales[index].status = estado;
                }
            }

            saveData(STORAGE_KEYS.proveedores, proveedoresActuales);

            poblarFiltros();
            renderProveedores();

            form.reset();
            modal.style.display = "none";
            idProveedorEnEdicion = null;
        });
    }
});

function poblarFiltros() {
    const selectFilterId = document.getElementById("filterProveedorID");
    const selectFilterName = document.getElementById("filterProveedorName");
    const selectFilterPais = document.getElementById("filterProveedorPais");

    const proveedores = getData(STORAGE_KEYS.proveedores);

    if (selectFilterId) selectFilterId.innerHTML = '<option value="">&nbsp;&nbsp;ID (Todos)</option>';
    if (selectFilterName) selectFilterName.innerHTML = '<option value="">&nbsp;&nbsp;Nombre (Todos)</option>';
    if (selectFilterPais) selectFilterPais.innerHTML = '<option value="">&nbsp;&nbsp;País (Todos)</option>';

    const paisesUnicos = new Set();

    proveedores.forEach(prov => {
        if (selectFilterId) {
            selectFilterId.insertAdjacentHTML('beforeend', `<option value="${prov.id}">&nbsp;&nbsp;${prov.id}</option>`);
        }
        if (selectFilterName) {
            selectFilterName.insertAdjacentHTML('beforeend', `<option value="${prov.name}">&nbsp;&nbsp;${prov.name}</option>`);
        }
        if (prov.country) {
            paimsUnicos = paisesUnicos.add(prov.country);
        }
    });

    paisesUnicos.forEach(pais => {
        if (selectFilterPais) {
            selectFilterPais.insertAdjacentHTML('beforeend', `<option value="${pais}">&nbsp;&nbsp;${pais}</option>`);
        }
    });
}

function renderProveedores(proveedoresLista = null) {
    const container = document.getElementById("suppliersTable");
    if (!container) return;

    const proveedoresTodos = getData(STORAGE_KEYS.proveedores);
    const listaAMostrar = proveedoresLista === null ? proveedoresTodos : proveedoresLista;

    actualizarEstadisticas(proveedoresTodos);

    if (listaAMostrar.length === 0) {
        container.innerHTML = `<p style="padding: 20px; text-align: center; color: var(--tertiary-color);">No se encontraron proveedores con los filtros aplicados.</p>`;
        return;
    }

    container.innerHTML = "";

    listaAMostrar.forEach(prov => {
        const proveedorInstancia = new Proveedor(
            prov.id,
            prov.name,
            prov.phone,
            prov.email,
            prov.city,
            prov.country,
            prov.status
        );
        container.insertAdjacentHTML("beforeend", proveedorInstancia.createRowHTML());
    });
}

function actualizarEstadisticas(proveedores) {
    const totalElement = document.getElementById("totalProveedores");
    const activosElement = document.getElementById("proveedoresActivos");
    const inactivosElement = document.getElementById("clientesInactivos");

    const total = proveedores.length;
    const activos = proveedores.filter(p => p.status === "Activo").length;
    const inactivos = proveedores.filter(p => p.status === "Inactivo").length;

    if (totalElement) totalElement.textContent = total;
    if (activosElement) activosElement.textContent = activos;
    if (inactivosElement) inactivosElement.textContent = inactivos;
}

function abrirModalEditarProveedor(id, modal, modalTitulo, inputName, inputPhone, inputEmail, inputCity, inputCountry, inputStatus) {
    const proveedores = getData(STORAGE_KEYS.proveedores);
    const proveedorAEditar = proveedores.find(prov => prov.id === id);

    if (!proveedorAEditar) return;
    const prov = proveedorAEditar;

    idProveedorEnEdicion = id;

    if (modalTitulo) modalTitulo.textContent = "Editar Proveedor";

    inputName.value = prov.name;
    inputPhone.value = prov.phone;
    inputEmail.value = prov.email;
    inputCity.value = prov.city;
    inputCountry.value = prov.country;
    inputStatus.value = prov.status || "Activo";

    if (modal) modal.style.display = "flex";
}

function ejecutarEliminacionReal(id) {
    const proveedoresActuales = getData(STORAGE_KEYS.proveedores);
    const proveedoresFiltrados = proveedoresActuales.filter(prov => prov.id !== id);

    saveData(STORAGE_KEYS.proveedores, proveedoresFiltrados);

    poblarFiltros();
    renderProveedores();
}

// #endregion


