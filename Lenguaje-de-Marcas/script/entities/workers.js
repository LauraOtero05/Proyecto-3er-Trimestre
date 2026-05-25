document.addEventListener('DOMContentLoaded', () => {

    class Worker {

        constructor(id, nombre, email, cargo, estado) {
            this.id = id;
            this.nombre = nombre;
            this.email = email;
            this.cargo = cargo;
            this.estado = estado;

        }

    }

    let workers =
        JSON.parse(
            sessionStorage.getItem("workers")
        );

    if (!workers || workers.length === 0) {
        workers = [
            new Worker("T001", "Marcus Vance", "marcus@74minutes.com", "Manager", "Activo"),
            new Worker("T002", "Sarah Connor", "sarah@74minutes.com", "Dependienta", "Activo"),
            new Worker("T003", "John Doe", "john@74minutes.com", "Mozo de almacén", "Inactivo")
        ];
        sessionStorage.setItem("workers", JSON.stringify(workers));
    }

    let workerEnEdicion = null;

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

    const modalTitulo =
        modal.querySelector(".modal__top h2");
    
    let indiceAEliminar = null;

    const modalConfirmar =
        document.getElementById("modalConfirmar");

    const cancelarEliminar =
        document.getElementById("cancelarEliminar");

    const confirmarEliminar =
        document.getElementById("confirmarEliminar");

    cancelarEliminar.addEventListener("click", () => {
        modalConfirmar.style.display = "none";
        indiceAEliminar = null;
    });

    confirmarEliminar.addEventListener("click", () => {
        if (indiceAEliminar !== null) {
            workers.splice(indiceAEliminar, 1);
            sessionStorage.setItem("workers", JSON.stringify(workers));
            pintarWorkers();
            indiceAEliminar = null;
        }
        modalConfirmar.style.display = "none";
    });

    abrirModal.addEventListener("click", () => {

        workerEnEdicion = null;
        modalTitulo.textContent = "Nuevo Trabajador";
        document.getElementById("nombre").value = "";
        document.getElementById("email").value = "";
        document.getElementById("cargo").value = "";
        document.getElementById("estado").value = "Activo";
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

    <div class="w-table__cell">${worker.nombre}</div>

    <div class="w-table__cell">${worker.email}</div>

    <div class="w-table__cell">${worker.cargo}</div>

    <div class="w-table__cell">
        <span class="
            w-table__estado
            ${worker.estado.toLowerCase()}
        ">
            ${worker.estado}
        </span>
    </div>

    <div class="w-table__cell" style="display:flex; gap:8px;">
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
            document.getElementById("nombre").value.trim();

        const email =
            document.getElementById("email").value.trim();

        const cargo =
            document.getElementById("cargo").value.trim();

        const estado =
            document.getElementById("estado").value;

        if (
            nombre === "" ||
            email === "" ||
            cargo === ""
        ) {

            return;

        }

        if (workerEnEdicion === null) {

            workers.push(new Worker(nombre, email, cargo, estado));

        } else {

            workers[workerEnEdicion] = new Worker(nombre, email, cargo, estado);
            workerEnEdicion = null;

        }

        sessionStorage.setItem(
            "workers",
            JSON.stringify(workers)
        );

        pintarWorkers();

        modal.style.display = "none";

    });

    tablaWorkers.addEventListener("click", (e) => {

        const index = parseInt(e.target.dataset.index);

        if (e.target.classList.contains("table__delete")) {
    indiceAEliminar = index;
    modalConfirmar.style.display = "flex";
}

        if (e.target.classList.contains("table__edit")) {

            workerEnEdicion = index;
            const w = workers[index];

            modalTitulo.textContent = "Editar Trabajador";
            document.getElementById("nombre").value = w.nombre;
            document.getElementById("email").value = w.email;
            document.getElementById("cargo").value = w.cargo;
            document.getElementById("estado").value = w.estado;

            modal.style.display = "flex";

        }

    });

    pintarWorkers();

});