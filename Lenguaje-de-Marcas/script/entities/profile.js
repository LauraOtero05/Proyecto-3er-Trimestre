document.addEventListener("DOMContentLoaded", () => {
    
    initializeStorage();

    const formPerfil = document.getElementById("profileForm");
    const inputNombre = document.getElementById("formName");
    const inputApellido = document.getElementById("formLastName");
    const inputEmail = document.getElementById("formEmail");
    const inputDireccion = document.getElementById("formAdress");
    const inputMovil = document.getElementById("formNumber");
    const inputFecha = document.getElementById("formDate");

    const successModal = document.getElementById("successProfileModal");
    const btnAccept = document.getElementById("btnSuccessAccept");

    cargarDatosPerfil();

    if (formPerfil) {
        formPerfil.addEventListener("submit", (e) => {
            e.preventDefault();

            const generoSeleccionado = document.querySelector('input[name="gender"]:checked').value;

            const perfilActualizado = {
                gender: generoSeleccionado,
                nombre: inputNombre.value.trim(),
                apellido: inputApellido.value.trim(),
                email: inputEmail.value.trim(),
                direccion: inputDireccion.value.trim(),
                movil: inputMovil.value.trim(),
                fechaNacimiento: inputFecha.value
            };

            saveData(STORAGE_KEYS.perfil, perfilActualizado);

            if (successModal) {
                successModal.style.display = "flex";
            }
        });
    }

    if (btnAccept && successModal) {
        btnAccept.addEventListener("click", () => {
            successModal.style.display = "none";
        });
    }

    window.addEventListener("click", (e) => {
        if (e.target === successModal) {
            successModal.style.display = "none";
        }
    });

    function cargarDatosPerfil() {
        const datosCargados = JSON.parse(sessionStorage.getItem(STORAGE_KEYS.perfil));

        if (!datosCargados) return;

        inputNombre.value = datosCargados.nombre || "";
        inputApellido.value = datosCargados.apellido || "";
        inputEmail.value = datosCargados.email || "";
        inputDireccion.value = datosCargados.direccion || "";
        inputMovil.value = datosCargados.movil || "";
        inputFecha.value = datosCargados.fechaNacimiento || "";

        const radios = document.querySelectorAll('input[name="gender"]');
        radios.forEach(radio => {
            if (radio.value === datosCargados.gender) {
                radio.checked = true;
            }
        });
    }
});