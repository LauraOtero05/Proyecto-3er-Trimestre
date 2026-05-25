const initialProductos = [
    { id: "1", artista: "Pink Floyd", titulo: "The Dark Side of the Moon", formato: "Vinilo", stock: 12, precio: 34.99, portada: "https://upload.wikimedia.org/wikipedia/en/3/3b/Dark_Side_of_the_Moon.png" },
    { id: "2", artista: "Daft Punk", titulo: "Random Access Memories", formato: "CD", stock: 3, precio: 18.50, portada: "https://i.pinimg.com/1200x/4e/d4/ab/4ed4ab4ff5e6c0484d6e47ccb3b55947.jpg" },
    { id: "3", artista: "Michael Jackson", titulo: "Thriller", formato: "Vinilo", stock: 8, precio: 29.99, portada: "https://upload.wikimedia.org/wikipedia/en/5/55/Michael_Jackson_-_Thriller.png" }
];

// Rellenar para tener datos por defecto en esas categorias
//const initialClientes = [];
//const initialTrabajadores = [];
//const initialPedidos = [];

const initialClientes = [
    { id: "C001", name: "Josiah Starline", email: "josiah@74minutes.com" },
    { id: "C002", name: "Isabel Bravo", email: "isabel.bravo@74minutes.com" },
    { id: "C003", name: "Amelia Pond", email: "amelia.pond@74minutes.com" }
];

// Trabajadores mapeados con la propiedad 'name' requerida por el JS
const initialTrabajadores = [
    { id: "T001", name: "Marcus Vance" },
    { id: "T002", name: "Sarah Connor" },
    { id: "T003", name: "John Doe" }
];


// Historial de pedidos iniciales para que la tabla no aparezca vacía al arrancar
const initialPedidos = [
    {
        id: "144826",
        date: "2026-05-19",
        status: "Pendiente",
        clientId: "C001",
        employeeId: "T001",
        totalAmount: 88.48,
        details: [
            { productId: "1", quantity: 2, unitPrice: 34.99 }, // 2x Pink Floyd = 69.98
            { productId: "2", quantity: 1, unitPrice: 18.50 }  // 1x Daft Punk = 18.50
        ]
    },
    {
        id: "144827",
        date: "2026-05-18",
        status: "Enviado",
        clientId: "C003",
        employeeId: "T002",
        totalAmount: 29.99,
        details: [
            { productId: "3", quantity: 1, unitPrice: 29.99 }  // 1x Michael Jackson = 29.99
        ]
    }
];

const initialProveedores = [
    { id: "P001", name: "Sony Music Spain", phone: "+34 911 234 567", email: "distribucion@sony.com", city: "Madrid", country: "España", status: "Activo" },
    { id: "P002", name: "Universal Vinyls", phone: "+44 207 123 456", email: "orders@universal.co.uk", city: "Londres", country: "Reino Unido", status: "Inactivo" }
];

const initialPerfilUsuario = {
    gender: "Femenino",
    nombre: "Noemi",
    apellido: "Cano",
    email: "noemi.cano@74minutes.com",
    direccion: "Avenida de Europa, 2B",
    movil: "+34 611 22 33 44",
    fechaNacimiento: "1995-01-25"
};

// Creamos keys para almacenar luego la información
const STORAGE_KEYS = {
    clientes: "74min_clientes",
    trabajadores: "74min_trabajadores",
    productos: "74min_productos",
    pedidos: "74min_pedidos",
    proveedores: "74min_proveedores",
    perfil: "74min_perfil"
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

/*Esta función carga los datos iniciales solo si el almacenamiento está vacio */
function initializeStorage() {
    const initialDataMap = {
        [STORAGE_KEYS.clientes]: initialClientes,
        [STORAGE_KEYS.trabajadores]: initialTrabajadores,
        [STORAGE_KEYS.productos]: initialProductos,
        [STORAGE_KEYS.pedidos]: initialPedidos,
        [STORAGE_KEYS.proveedores]: initialProveedores,
        [STORAGE_KEYS.perfil]: initialPerfilUsuario
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