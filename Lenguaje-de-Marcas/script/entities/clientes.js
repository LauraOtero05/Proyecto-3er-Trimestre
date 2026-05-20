document.addEventListener('DOMContentLoaded', () => {

    class Cliente {

        constructor(nombre, email, empresa, estado) {

            this.nombre = nombre;
            this.email = email;
            this.empresa = empresa;
            this.estado = estado;

        }

    }

    let clientes =
        JSON.parse(
            sessionStorage.getItem("clientes")
        );

    if (!clientes || clientes.length === 0) {
    clientes = [
        new Cliente("Josiah Starline", "josiah@amazon.com", "amazon", "Activo"),
        new Cliente("Isabel Bravo", "isabel.bravo@vinted.com", "vinted", "Activo"),
        new Cliente("Amelia Pond", "amelia.pond@wallapop.com", "wallapop", "Activo")
    ];
    sessionStorage.setItem("clientes", JSON.stringify(clientes));}

    let clienteEnEdicion = null;

    const listaClientes =
        document.getElementById("tablaClientes");

    const modal =
        document.getElementById("modal");

    const abrirModal =
        document.getElementById("abrirModal");

    const cerrarModal =
        document.getElementById("cerrarModal");

    const guardarCliente =
        document.getElementById("guardarCliente");

    const totalClientes =
        document.getElementById("totalClientes");

    const clientesActivos =
        document.getElementById("clientesActivos");

    const clientesInactivos =
        document.getElementById("clientesInactivos");

    const modalTitulo =
        modal.querySelector(".modal__top h2");
        const modalConfirmar   = document.getElementById("modalConfirmar");
const cancelarEliminar = document.getElementById("cancelarEliminar");
const confirmarEliminar = document.getElementById("confirmarEliminar");

let indiceAEliminar = null;

cancelarEliminar.addEventListener("click", () => {
    modalConfirmar.style.display = "none";
    indiceAEliminar = null;
});

confirmarEliminar.addEventListener("click", () => {
    if (indiceAEliminar !== null) {
        clientes.splice(indiceAEliminar, 1);
        sessionStorage.setItem("clientes", JSON.stringify(clientes));
        pintarClientes();
        indiceAEliminar = null;
    }
    modalConfirmar.style.display = "none";
});

    abrirModal.addEventListener("click", () => {

        clienteEnEdicion = null;
        modalTitulo.textContent = "Nuevo Cliente";
        document.getElementById("nombre").value  = "";
        document.getElementById("email").value   = "";
        document.getElementById("empresa").value = "";
        document.getElementById("estado").value  = "Activo";
        modal.style.display = "flex";

    });

    cerrarModal.addEventListener("click", () => {

        modal.style.display = "none";

    });

    window.addEventListener("click", (e) => {

        if (e.target === modal) {

            modal.style.display = "none";

        }

    });

    function pintarClientes() {

        listaClientes.innerHTML = "";

        clientes.forEach((cliente, index) => {

            const fila =
                document.createElement("div");

            fila.classList.add("c-table__row");

            fila.innerHTML = `

                <div class="c-table__cell">
                    ${cliente.nombre}
                </div>

                <div class="c-table__cell">
                    ${cliente.email}
                </div>

                <div class="c-table__cell">
                    ${cliente.empresa}
                </div>

                <div class="c-table__cell">

                    <span class="
                        c-table__estado
                        ${cliente.estado.toLowerCase()}
                    ">

                        ${cliente.estado}

                    </span>

                </div>

                <div class="c-table__cell" style="display:flex; gap:8px;">

                    <button
                        class="table__edit"
                        data-index="${index}"
                    >
                        Editar
                    </button>

                    <button
                        class="table__delete"
                        data-index="${index}"
                    >
                        Borrar
                    </button>

                </div>

            `;

            listaClientes.appendChild(fila);

        });

        actualizarStats();

    }

    guardarCliente.addEventListener("click", (e) => {

        e.preventDefault();

        const nombre =
            document.getElementById("nombre").value.trim();

        const email =
            document.getElementById("email").value.trim();

        const empresa =
            document.getElementById("empresa").value.trim();

        const estado =
            document.getElementById("estado").value;

        if (
            nombre === "" ||
            email === "" ||
            empresa === ""
        ) {

            return;

        }

        if (clienteEnEdicion === null) {

            clientes.push(new Cliente(nombre, email, empresa, estado));

        } else {

            clientes[clienteEnEdicion] = new Cliente(nombre, email, empresa, estado);
            clienteEnEdicion = null;

        }

        sessionStorage.setItem(
            "clientes",
            JSON.stringify(clientes)
        );

        pintarClientes();

        modal.style.display = "none";

    });

     listaClientes.addEventListener("click", (e) => {

        const index = parseInt(e.target.dataset.index);

        if (e.target.classList.contains("table__delete")) {
            indiceAEliminar = index;
            modalConfirmar.style.display = "flex";
        }

        if (e.target.classList.contains("table__edit")) {
            clienteEnEdicion = index;
            const c = clientes[index];
            modalTitulo.textContent = "Editar Cliente";
            document.getElementById("nombre").value  = c.nombre;
            document.getElementById("email").value   = c.email;
            document.getElementById("empresa").value = c.empresa;
            document.getElementById("estado").value  = c.estado;
            modal.style.display = "flex";
        }
    


        if (e.target.classList.contains("table__edit")) {

            clienteEnEdicion = index;
            const c = clientes[index];

            modalTitulo.textContent = "Editar Cliente";
            document.getElementById("nombre").value  = c.nombre;
            document.getElementById("email").value   = c.email;
            document.getElementById("empresa").value = c.empresa;
            document.getElementById("estado").value  = c.estado;

            modal.style.display = "flex";

        }

    });

    function actualizarStats() {

        totalClientes.textContent =
            clientes.length;

        const activos =
            clientes.filter(cliente =>
                cliente.estado === "Activo"
            );

        const inactivos =
            clientes.filter(cliente =>
                cliente.estado === "Inactivo"
            );

        clientesActivos.textContent =
            activos.length;

        clientesInactivos.textContent =
            inactivos.length;

    }

    window.addEventListener("resize", () => {

        console.log(window.innerWidth);

    });

    pintarClientes();

});