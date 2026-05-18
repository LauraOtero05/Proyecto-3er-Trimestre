document.addEventListener('DOMContentLoaded', () => {

    class Worker {

        constructor(nombre, email, cargo, estado) {

            this.nombre = nombre;
            this.email = email;
            this.cargo = cargo;
            this.estado = estado;

        }

    }

    let workers =
        JSON.parse(
            sessionStorage.getItem("workers")
        ) || [];

    const tablaWorkers =
        document.getElementById("tablaWorkers");

    const modal =
        document.getElementById("modal");

    const abrirModal =
        document.getElementById("abrirModal");

    const cerrarModal =
        document.getElementById("cerrarModal");

    const guardarWorker =
        document.getElementById("guardarWorker");

    const totalWorkers =
        document.getElementById("totalWorkers");

    const workersActivos =
        document.getElementById("workersActivos");

    const workersInactivos =
        document.getElementById("workersInactivos");

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

    function pintarWorkers() {

        tablaWorkers.innerHTML = "";

        workers.forEach((worker, index) => {

            const fila =
                document.createElement("div");

            fila.classList.add("w-table__row");

            fila.innerHTML = `

                <div>${worker.nombre}</div>

                <div>${worker.email}</div>

                <div>${worker.cargo}</div>

                <div>

                    <span class="
                        w-table__estado
                        ${worker.estado.toLowerCase()}
                    ">

                        ${worker.estado}

                    </span>

                </div>

                <div>

                    <button
                        class="w-table__delete"
                        data-index="${index}"
                    >

                        Borrar

                    </button>

                </div>

            `;

            tablaWorkers.appendChild(fila);

        });

        actualizarStats();

    }

    function actualizarStats() {

        totalWorkers.textContent =
            workers.length;

        const activos =
            workers.filter(worker =>
                worker.estado === "Activo"
            );

        const inactivos =
            workers.filter(worker =>
                worker.estado === "Inactivo"
            );

        workersActivos.textContent =
            activos.length;

        workersInactivos.textContent =
            inactivos.length;

    }

    guardarWorker.addEventListener("click", () => {

        const nombre =
            document.getElementById("nombre").value;

        const email =
            document.getElementById("email").value;

        const cargo =
            document.getElementById("cargo").value;

        const estado =
            document.getElementById("estado").value;

        if (
            nombre === "" ||
            email === "" ||
            cargo === ""
        ) {

            return;

        }

        const nuevoWorker =
            new Worker(
                nombre,
                email,
                cargo,
                estado
            );

        workers.push(nuevoWorker);

        sessionStorage.setItem(
            "workers",
            JSON.stringify(workers)
        );

        pintarWorkers();

        document.getElementById("nombre").value = "";

        document.getElementById("email").value = "";

        document.getElementById("cargo").value = "";

        modal.style.display = "none";

    });

    tablaWorkers.addEventListener("click", (e) => {

        if (
            e.target.classList.contains("w-table__delete")
        ) {

            const index =
                e.target.dataset.index;

            workers.splice(index, 1);

            sessionStorage.setItem(
                "workers",
                JSON.stringify(workers)
            );

            pintarWorkers();

        }

    });

    pintarWorkers();

});