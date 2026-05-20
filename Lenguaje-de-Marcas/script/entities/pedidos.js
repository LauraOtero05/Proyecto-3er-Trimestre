document.addEventListener('DOMContentLoaded', () => {

    const KEYS = {
        orders: "74min_pedidos",
        clients: "74min_clientes",
        employees: "74min_trabajadores",
        products: "74min_productos"
    };

    const ordersTableBody = document.getElementById('ordersTableBody');
    const addOrderModal = document.getElementById('addOrderModal');
    const modalForm = document.getElementById('productForm');
    
    const openOrdersModalBtn = document.getElementById('openOrdersModal');
    const closeOrdersModalBtn = document.getElementById('closeOrdersModal');
    const addLineBtn = document.getElementById('addLineBtn');

    const orderDate = document.getElementById('orderDate');
    const orderStatus = document.getElementById('orderStatus');
    const orderClient = document.getElementById('orderClient');
    const orderWorker = document.getElementById('orderWorker');
    const detailLinesContainer = document.getElementById('detailLinesContainer');
    const calculatedOrderTotal = document.getElementById('calculatedOrderTotal');

    const modalDeleteOrder = document.getElementById('modalDeleteOrder');
    const cancelDeleteOrder = document.getElementById('cancelDeleteOrder');
    const confirmDeleteOrder = document.getElementById('confirmDeleteOrder');

    let orderInProgressId = null;
    let idToDelete = null;
    let currentLines = [];

    const read = (key) => JSON.parse(sessionStorage.getItem(key)) || [];
    const save = (key, data) => sessionStorage.setItem(key, JSON.stringify(data));

    function generateOrderId() {
        const orders = read(KEYS.orders);
        if (orders.length === 0) return 144826;
        return Math.max(...orders.map(o => parseInt(o.id) || 0)) + 1;
    }

    function fillSelectOptions(element, dataKey, defaultText) {
        const items = read(dataKey);
        element.innerHTML = `<option value="">— ${defaultText} —</option>`;
        items.forEach(item => {
            const opt = document.createElement('option');
            opt.value = item.id;
            opt.textContent = item.name || `${item.titulo} (${item.precio.toFixed(2)} €)`;
            element.appendChild(opt);
        });
    }

    function createDetailLines() {
        detailLinesContainer.innerHTML = '';
        const products = read(KEYS.products);

        currentLines.forEach((line, index) => {
            const row = document.createElement('div');
            row.classList.add('o-form__details'); 

            const selectProd = document.createElement('select');
            selectProd.style.flex = '2';
            selectProd.innerHTML = '<option value="">— Selecciona un Álbum —</option>';
            products.forEach(p => {
                const opt = document.createElement('option');
                opt.value = p.id;
                opt.textContent = `${p.titulo} (${p.precio.toFixed(2)} €)`;
                if (String(p.id) === String(line.productId)) opt.selected = true;
                selectProd.appendChild(opt);
            });
            selectProd.addEventListener('change', () => {
                currentLines[index].productId = selectProd.value;
                calculateTotalAmount();
            });

            const inputAmount = document.createElement('input');
            inputAmount.type = 'number';
            inputAmount.min = '1';
            inputAmount.value = line.quantity;
            inputAmount.style.width = '70px';
            inputAmount.addEventListener('input', () => {
                currentLines[index].quantity = parseInt(inputAmount.value) || 1;
                calculateTotalAmount();
            });

            const btnDeleteLine = document.createElement('button');
            btnDeleteLine.classList.add('p-card__delete');
            btnDeleteLine.textContent = '✕';
            btnDeleteLine.addEventListener('click', () => {
                currentLines.splice(index, 1);
                createDetailLines();
            });

            row.appendChild(selectProd);
            row.appendChild(inputAmount);
            row.appendChild(btnDeleteLine);
            detailLinesContainer.appendChild(row);
        });

        calculateTotalAmount();
    }

    function calculateTotalAmount() {
        const products = read(KEYS.products);
        let total = 0;

        currentLines.forEach(line => {
            const prod = products.find(p => String(p.id) === String(line.productId));
            if (prod) total += prod.precio * line.quantity;
        });

        calculatedOrderTotal.textContent = `${total.toFixed(2)} €`;
        return total;
    }

    openOrdersModalBtn.addEventListener('click', () => {
        orderInProgressId = null;
        currentLines = [];
        modalForm.reset();
        addOrderModal.querySelector('h2').textContent = 'Añadir Nuevo Pedido';
        orderDate.value = new Date().toISOString().split('T')[0];

        fillSelectOptions(orderClient, KEYS.clients, "Selecciona Cliente");
        fillSelectOptions(orderWorker, KEYS.employees, "Sin Asignar");
        createDetailLines();

        addOrderModal.style.display = 'flex';
    });

    closeOrdersModalBtn.addEventListener('click', () => addOrderModal.style.display = 'none');
    cancelDeleteOrder.addEventListener('click', () => modalDeleteOrder.style.display = 'none');

    addLineBtn.addEventListener('click', () => {
        currentLines.push({ productId: '', quantity: 1 });
        createDetailLines();
    });

    modalForm.addEventListener('submit', (e) => {
        e.preventDefault();

        if (!orderClient.value) {
            alert('Por favor, selecciona un cliente antes de continuar.');
            return;
        }
        if (currentLines.length === 0 || currentLines.some(l => !l.productId)) {
            alert('Añade al menos un producto válido al pedido.');
            return;
        }

        let orders = read(KEYS.orders);
        const products = read(KEYS.products);
        const total = calculateTotalAmount();

        const orderDetails = currentLines.map(l => ({
            productId: l.productId,
            quantity: l.quantity,
            unitPrice: products.find(p => String(p.id) === String(l.productId))?.precio || 0
        }));

        if (orderInProgressId === null) {
            const newOrder = {
                id: String(generateOrderId()),
                date: orderDate.value,
                status: orderStatus.value,
                clientId: orderClient.value,
                employeeId: orderWorker.value,
                totalAmount: total,
                details: orderDetails
            };
            orders.push(newOrder);
        } else {
            const idx = orders.findIndex(o => String(o.id) === String(orderInProgressId));
            if (idx !== -1) {
                orders[idx].date = orderDate.value;
                orders[idx].status = orderStatus.value;
                orders[idx].clientId = orderClient.value;
                orders[idx].employeeId = orderWorker.value;
                orders[idx].totalAmount = total;
                orders[idx].details = orderDetails;
            }
        }

        save(KEYS.orders, orders);
        createOrdersTable();
        addOrderModal.style.display = 'none';
    });

    confirmDeleteOrder.addEventListener('click', () => {
        if (idToDelete) {
            let orders = read(KEYS.orders).filter(o => String(o.id) !== String(idToDelete));
            save(KEYS.orders, orders);
            createOrdersTable();
        }
        modalDeleteOrder.style.display = 'none';
    });

    function createOrdersTable() {
        ordersTableBody.innerHTML = '';
        const orders = read(KEYS.orders);
        const clients = read(KEYS.clients);
        const employees = read(KEYS.employees);

        if (orders.length === 0) {
            const emptyRow = document.createElement('div');
            emptyRow.innerHTML = `
                <p>No hay pedidos en este momento</p>
            `;
            ordersTableBody.appendChild(emptyRow);
            return;
        }

        orders.forEach(order => {
            const clientName = clients.find(c => String(c.id) === String(order.clientId))?.name || '(Desconocido)';
            const workerName = employees.find(e => String(e.id) === String(order.employeeId))?.name || '—';
            const statusClass = order.status.toLowerCase() === 'pendiente' ? 'o-badge--pending' : 'o-badge--delivered';

            const row = document.createElement('div');
            row.className = 'o-table__row'; 

            row.innerHTML = `
                <div class="o-table__item o-cell__id">${order.id}</div>
                <div class="o-table__item o-cell__date">${order.date}</div>
                <div class="o-table__item o-cell__status"><span class="tag">${order.status}</span></div>
                <div class="o-table__item o-cell__client">${clientName}</div>
                <div class="o-table__item o-cell__worker">${workerName}</div>
                <div class="o-table__item o-cell__amount" style="font-weight: 600;">${Number(order.totalAmount).toFixed(2)} €</div>
            `;

            const actionsCell = document.createElement('div');
            actionsCell.className = 'o-table__item o-cell__actions o-dropdown'

            const btnMore = document.createElement('div');
            btnMore.className = 'o-action__icon';
            btnMore.innerHTML = '<span class="material-symbols-rounded">more_horiz</span>';
            
            const dropdownMenu = document.createElement('div');
            dropdownMenu.className = 'o-dropdown__menu';
            
            const itemEdit = document.createElement('button');
            itemEdit.className = 'o-dropdown__item';
            itemEdit.innerHTML = '<span class="material-symbols-rounded">edit</span> Editar';
            itemEdit.addEventListener('click', (e) => {
                e.stopPropagation();
                dropdownMenu.classList.remove('is-active');
                openEditForm(order);
            });

            const itemDelete = document.createElement('button');
            itemDelete.className = 'o-dropdown__item o-dropdown__item--delete';
            itemDelete.innerHTML = '<span class="material-symbols-rounded">delete</span> Eliminar';
            itemDelete.addEventListener('click', (e) => {
                e.stopPropagation();
                dropdownMenu.classList.remove('is-active');
                idToDelete = order.id;
                modalDeleteOrder.style.display = 'flex';
            });

            dropdownMenu.appendChild(itemEdit);
            dropdownMenu.appendChild(itemDelete);
            actionsCell.appendChild(btnMore);
            actionsCell.appendChild(dropdownMenu);
            row.appendChild(actionsCell);

            btnMore.addEventListener('click', (e) => {
                e.stopPropagation();
                
                document.querySelectorAll('.o-dropdown__menu').forEach(menu => {
                    if (menu !== dropdownMenu) menu.classList.remove('is-active');
                });

                dropdownMenu.classList.toggle('is-active');
            });

            ordersTableBody.appendChild(row);
        });
    }

    addOrderModal.addEventListener('click', (e) => {
        if (e.target === addOrderModal) {
            addOrderModal.style.display = 'none';
        }
    });

    window.addEventListener('click', () => {
        document.querySelectorAll('.o-dropdown__menu').forEach(menu => {
            menu.classList.remove('is-active');
        });
    });

    function openEditForm(order) {
        orderInProgressId = order.id;
        addOrderModal.querySelector('h2').textContent = `Editar Pedido #${order.id}`;
        orderDate.value = order.date;
        orderStatus.value = order.status;

        fillSelectOptions(orderClient, KEYS.clients, "Selecciona Cliente");
        fillSelectOptions(orderWorker, KEYS.employees, "Sin Asignar");
        
        orderClient.value = order.clientId;
        orderWorker.value = order.employeeId;

        currentLines = order.details ? order.details.map(d => ({
            productId: d.productId,
            quantity: d.quantity
        })) : [];

        createDetailLines();
        addOrderModal.style.display = 'flex';
    }

    createOrdersTable();
});
