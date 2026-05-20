// =======================================================
// DATOS DE PRUEBA TEMPORALES (Insertar en tu archivo)
// =======================================================

// Clientes mapeados con la propiedad 'name' requerida por el JS
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


// Ejecución forzada al cargar este archivo
initializeStorage();