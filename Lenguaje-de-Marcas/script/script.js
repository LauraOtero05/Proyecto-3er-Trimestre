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

document.addEventListener("DOMContentLoaded", () => {
    console.log("1. DOM cargado correctamente.");

    initializeStorage(); 
    renderProductos();

    // Captura de elementos del Modal
    const modal = document.getElementById("productModal");
    const btnAbrir = document.getElementById("btnOpenModal");
    const btnCerrar = document.getElementById("btnCloseModal");
    const form = document.getElementById("productForm");

    // Inputs para aplicar validaciones controladas
    const inputPrecio = document.getElementById("formPrecio");
    const inputStock = document.getElementById("formStock");

    // --- GESTIÓN DE EVENTOS DEL DOM: MODAL ---
    if (btnAbrir && modal) {
        btnAbrir.addEventListener("click", () => {
            modal.style.display = "flex";
        });
    }

    if (btnCerrar && modal) {
        btnCerrar.addEventListener("click", () => {
            modal.style.display = "none";
            form.reset(); // Limpia los campos al cerrar
        });
    }

    // Cerrar el modal haciendo clic fuera de la caja blanca (Uso de Window/Viewport indirecto)
    window.addEventListener("click", (e) => {
        if (e.target === modal) {
            modal.style.display = "none";
            form.reset();
        }
    });

    // --- RESTRICCIONES DE ENTRADA EN TIEMPO REAL ---

    // Validar precio: Solo permite números y un único punto o coma (elimina símbolos como €)
    inputPrecio.addEventListener("input", (e) => {
        // Reemplaza cualquier caracter que no sea número, punto o coma
        let value = e.target.value.replace(/[^0-9.,]/g, "");
        // Cambia comas por puntos automáticamente para no romper la lógica decimal de JS
        value = value.replace(/,/g, ".");
        // Evita que pongan más de un punto decimal
        const puntos = value.split(".");
        if (puntos.length > 2) {
            value = puntos[0] + "." + puntos.slice(1).join("");
        }
        e.target.value = value;
    });

    // Validar Stock: Solo permite números enteros positivos
    inputStock.addEventListener("input", (e) => {
        e.target.value = e.target.value.replace(/[^0-9]/g, "");
    });


    // --- PROCESAR FORMULARIO DE ALTA ---
    if (form) {
        form.addEventListener("submit", (e) => {
            e.preventDefault(); // Evita que la página se recargue

            const titulo = document.getElementById("formTitulo").value.trim();
            const artista = document.getElementById("formArtista").value.trim();
            
            // Tratamiento de Formato: Ya viene controlado por el <select>, aseguramos consistencia
            const formatoSelect = document.getElementById("formFormato").value; 
            const formatoFormateado = formatoSelect === "CD" ? "CD" : "Vinilo";

            const precio = parseFloat(inputPrecio.value);
            const stock = parseInt(inputStock.value);
            const portada = document.getElementById("formPortada").value.trim();

            if (!titulo || !artista || isNaN(precio) || isNaN(stock)) {
                alert("Por favor, rellena correctamente todos los campos obligatorios.");
                return;
            }

            const productosActuales = getData(STORAGE_KEYS.productos);
            const nuevoId = Date.now().toString();

            // Construimos el objeto plano para SessionStorage
            const nuevoDiscoData = {
                id: nuevoId,
                titulo: titulo,
                artista: artista,
                genero: "General",
                formato: formatoFormateado, // Guarda exactamente "CD" o "Vinilo"
                precio: precio,
                stock: stock,
                portada: portada
            };

            productosActuales.push(nuevoDiscoData);
            saveData(STORAGE_KEYS.productos, productosActuales);
            
            // Renderizamos, limpiamos el formulario y cerramos
            renderProductos();
            form.reset();
            modal.style.display = "none";
        });
    }

    // Delegación de eventos para la acción de Eliminar
    const container = document.getElementById("discContainer");
    if (container) {
        container.addEventListener("click", (e) => {
            if (e.target.classList.contains("p-card__delete")) {
                const idProducto = e.target.getAttribute("data-id");
                eliminarProducto(idProducto);
            }
        });
    }
});

function renderProductos() {
    const container = document.getElementById("discContainer");
    if (!container) return;

    const productosRaw = getData(STORAGE_KEYS.productos);

    if (productosRaw.length === 0) {
        container.innerHTML = `<p class="p-disc__empty">No hay productos en el catálogo.</p>`;
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

function eliminarProducto(id) {
    if (confirm("¿Estás seguro de que deseas eliminar este disco?")) {
        const productosActuales = getData(STORAGE_KEYS.productos);
        const productosFiltrados = productosActuales.filter(prod => prod.id !== id);
        
        saveData(STORAGE_KEYS.productos, productosFiltrados);
        renderProductos();
    }
}

// #endregion