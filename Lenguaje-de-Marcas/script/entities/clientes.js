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
        ) || [];

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

    abrirModal.addEventListener("click", () => {

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

                <div class="c-table__cell">

                    <button
                        class="c-table__delete"
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
            document.getElementById("nombre").value;

        const email =
            document.getElementById("email").value;

        const empresa =
            document.getElementById("empresa").value;

        const estado =
            document.getElementById("estado").value;

        if (
            nombre === "" ||
            email === "" ||
            empresa === ""
        ) {

            return;

        }

        const nuevoCliente =
            new Cliente(
                nombre,
                email,
                empresa,
                estado
            );

        clientes.push(nuevoCliente);

        sessionStorage.setItem(
            "clientes",
            JSON.stringify(clientes)
        );

        pintarClientes();

        document.getElementById("nombre").value = "";
        document.getElementById("email").value = "";
        document.getElementById("empresa").value = "";

        modal.style.display = "none";

    });

    listaClientes.addEventListener("click", (e) => {

        if (
            e.target.classList.contains("c-table__delete")
        ) {

            const index =
                e.target.dataset.index;

            clientes.splice(index, 1);

            sessionStorage.setItem(
                "clientes",
                JSON.stringify(clientes)
            );

            pintarClientes();

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