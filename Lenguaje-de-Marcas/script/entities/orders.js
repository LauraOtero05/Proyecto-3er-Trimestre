document.addEventListener('DOMContentLoaded', () => {

    const KEYS = {
        orders: "74min_pedidos",
        clients: "clientes",
        employees: "workers",
        products: "74min_productos"
    };

    if (typeof initializeStorage === "function") {
        initializeStorage();
    }

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

    let currentPage = 1;

    const prevPageBtn = document.getElementById('prevPageBtn');
    const nextPageBtn = document.getElementById('nextPageBtn');
    const paginationNumbers = document.getElementById('paginationNumbers');

    const searchOrderId = document.getElementById('searchOrderId');

    const read = (key) => {
        const data = sessionStorage.getItem(key);
        if (!data) return [];
        return JSON.parse(data);
    };
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

            opt.value = item.id || item.nombre; 
            
            opt.textContent = item.nombre || `${item.titulo} (${item.precio.toFixed(2)} €)`;
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
            btnDeleteLine.type = 'button';
            btnDeleteLine.classList.add('button__secondary', 'button__secondary--red');
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
        const tableBody = document.getElementById('ordersTableBody');
        tableBody.innerHTML = '';

        let orders = read(KEYS.orders);
        const clients = read(KEYS.clients);
        const employees = read(KEYS.employees);

        const searchTerm = searchOrderId ? searchOrderId.value.trim() : '';
        if (searchTerm !== '') {
            orders = orders.filter(order => String(order.id).includes(searchTerm));
        }

        if (orders.length === 0) {
            const emptyRow = document.createElement('div');
            emptyRow.innerHTML = searchTerm !== '' 
                ? `<p>No se encontró ningún pedido con el ID "${searchTerm}"</p>`
                : `<p>No hay pedidos en este momento</p>`;
            tableBody.appendChild(emptyRow);
            createPaginationControls(0); 
            return;
        }

        let itemsPerPage = 8;
        if (window.innerWidth <= 480) {
            itemsPerPage = 3;
        } else if (window.innerWidth <= 900) {
            itemsPerPage = 5;
        }

        const totalPages = Math.ceil(orders.length / itemsPerPage);
        if (currentPage > totalPages) currentPage = totalPages || 1;

        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        const visibleOrders = orders.slice(startIndex, endIndex);

        visibleOrders.forEach(order => {
            const clientMatch = clients.find(c => String(c.id) === String(order.clientId) || c.nombre === order.clientId);
            const clientName = clientMatch ? clientMatch.nombre : (order.clientId || '(Desconocido)');

            const workerMatch = employees.find(e => String(e.id) === String(order.employeeId) || e.nombre === order.employeeId);
            const workerName = workerMatch ? workerMatch.nombre : (order.employeeId || '—');

            const row = document.createElement('div');
            row.className = 'o-table__row';

            row.innerHTML = `
                <div class="o-table__item o-cell__id">${order.id}</div>
                <div class="o-table__item o-cell__date">${order.date}</div>
                <div class="o-table__item o-table__item--blue o-cell__status">${order.status}</div>
                <div class="o-table__item o-cell__client">${clientName}</div>
                <div class="o-table__item o-cell__worker">${workerName}</div>
                <div class="o-table__item o-cell__amount" style="font-weight: 600;">${Number(order.totalAmount).toFixed(2)} €</div>
            `;

            const actionsCell = document.createElement('div');
            actionsCell.className = 'o-table__item o-cell__actions o-dropdown';

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

            tableBody.appendChild(row);
        });

        createPaginationControls(totalPages);
    }

    function createPaginationControls(totalPages) {
        const paginationContainer = document.querySelector('.o-table__pagination');
        if (!paginationContainer) return;

        paginationNumbers.innerHTML = '';

        if (totalPages <= 1) {
            paginationContainer.style.display = 'none';
            return;
        } else {
            paginationContainer.style.display = 'flex';
        }

        for (let i = 1; i <= totalPages; i++) {
            const pageButton = document.createElement('button');
            pageButton.className = `o-pagination__btn ${i === currentPage ? 'active' : ''}`;
            pageButton.textContent = i;

            pageButton.addEventListener('click', () => {
                currentPage = i;
                createOrdersTable();
            });

            paginationNumbers.appendChild(pageButton);
        }

        prevPageBtn.disabled = (currentPage === 1);
        nextPageBtn.disabled = (currentPage === totalPages);
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

    prevPageBtn.addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            createOrdersTable();
        }
    });

    nextPageBtn.addEventListener('click', () => {
        const orders = read(KEYS.orders);
        let itemsPerPage = window.innerWidth <= 480 ? 3 : window.innerWidth <= 900 ? 5 : 8;
        const totalPages = Math.ceil(orders.length / itemsPerPage);

        if (currentPage < totalPages) {
            currentPage++;
            createOrdersTable();
        }
    });

    let resizeTimeout;
    window.addEventListener('resize', () => {
        clearTimeout(resizeTimeout);
        resizeTimeout = setTimeout(() => {
            createOrdersTable();
        }, 150);
    });

    searchOrderId.addEventListener('input', () => {
        currentPage = 1;
        createOrdersTable();
    });

    createOrdersTable();
});
