const initialProductos = [
    { id: "1", artista: "Pink Floyd", titulo: "The Dark Side of the Moon", formato: "Vinilo", stock: 12, precio: 34.99, portada: "https://upload.wikimedia.org/wikipedia/en/3/3b/Dark_Side_of_the_Moon.png" },
    { id: "2", artista: "Daft Punk", titulo: "Random Access Memories", formato: "CD", stock: 3, precio: 18.50, portada: "https://i.pinimg.com/1200x/4e/d4/ab/4ed4ab4ff5e6c0484d6e47ccb3b55947.jpg" },
    { id: "3", artista: "Michael Jackson", titulo: "Thriller", formato: "Vinilo", stock: 8, precio: 29.99, portada: "https://upload.wikimedia.org/wikipedia/en/5/55/Michael_Jackson_-_Thriller.png" }
];

// Las demás constantes que ya tenías
const initialClientes = [];
const initialTrabajadores = [];
const initialPedidos = [];
const initialProveedores = [];


// Creamos keys para almacenar luego la información
const STORAGE_KEYS = {
    clientes: "74min_clientes",
    trabajadores: "74min_trabajadores",
    productos: "74min_productos",
    pedidos: "74min_pedidos",
    proveedores: "74min_proveedores"
};

// Función flecha para guardar datos
const saveData = (key, data) => {
    sessionStorage.setItem(key, JSON.stringify(data));
};

// Función tradicional para coger los datos
function getData(key) {
    const data = sessionStorage.getItem(key);
    return data ? JSON.parse(data) : [];
}

/**
 * ============================================
 * INICIALIZACIÓN DEL SISTEMA
 * ============================================
 * Esta función se encarga de cargar los datos iniciales 
 * solo si el almacenamiento está vacío.
 */
function initializeStorage() {
    // Mapeamos tus claves con los datos iniciales (procedentes de initial-data.js)
    const initialDataMap = {
        [STORAGE_KEYS.clientes]: initialClientes,
        [STORAGE_KEYS.trabajadores]: initialTrabajadores,
        [STORAGE_KEYS.productos]: initialProductos,
        [STORAGE_KEYS.pedidos]: initialPedidos,
        [STORAGE_KEYS.proveedores]: initialProveedores
    };

    // Usamos Object.entries para recorrer el mapa de forma eficiente
    Object.entries(initialDataMap).forEach(([key, value]) => {
        // Solo guardamos si no existe ya información bajo esa clave
        if (!sessionStorage.getItem(key)) {
            saveData(key, value);
        }
    });
}

/* REINICIO DEL SISTEMA */
function resetStorage() {
    sessionStorage.clear();
    initializeStorage();
    console.log("Sistema reiniciado");

}