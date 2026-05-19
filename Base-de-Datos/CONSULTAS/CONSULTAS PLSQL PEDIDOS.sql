-- ============================================================
-- --------- TABLA PEDIDOS ---------
-- ============================================================

-- ============================================================
--  REGISTROS DE CREACIÓN
-- ============================================================

-- CONSULTA 1 - Insertar un pedido

DECLARE

    v_fecha PEDIDOS.FECHA%TYPE := SYSDATE;
    v_estado PEDIDOS.ESTADO%TYPE := 'Pendiente';
    v_importe PEDIDOS.IMPORTE_TOTAL%TYPE := 32.99;
    
BEGIN

    INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador)
    VALUES (v_fecha, v_estado, v_importe, 1, '11111111A');
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Se ha insertado un pedido nuevo');

END;
/

-- CONSULTA 2 - Insertar un pedido calculando el importe según los álbumes que incluye

DECLARE

    v_price ALBUMES.PRECIO%TYPE;
    v_total PEDIDOS.IMPORTE_TOTAL%TYPE := 0;
    v_amount NUMBER := 2;
    v_id_order PEDIDOS.ID_PEDIDO%TYPE;

BEGIN

    SELECT precio INTO v_price FROM albumes 
    WHERE id_album = 1;
    v_total := ROUND(v_price * v_amount, 2);
    
    INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador)
    VALUES (SYSDATE, 'Pendiente', v_total, 1, '22222222B')
    RETURNING id_pedido INTO v_id_order;
    
    INSERT INTO detalles_pedido (id_pedido, id_album, cantidad)
    VALUES (v_id_order, 1, v_amount);
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Se ha insertado el pedido #' || v_id_order || ' | Importe total: ' || TO_CHAR(v_total, '999.99'));

END;
/

-- CONSULTA 3 - Insertar un pedido cambiando el estado según la fecha

DECLARE

    v_date DATE := TO_DATE('2025-06-01', 'YYYY-MM-DD');
    v_status PEDIDOS.ESTADO%TYPE;
    v_days_left NUMBER;
    
BEGIN

    v_days_left := TRUNC(SYSDATE) - v_date;

    IF v_days_left > 30 THEN
        v_status := 'Entregado';
    ELSIF v_days_left > 10 THEN
        v_status := 'Enviado';
    ELSE
        v_status := 'Pendiente';
    END IF;

    INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador)
    VALUES (v_date, v_status, 47.98, 2, '33333333C');

    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Estado asignado: ' || v_status);
    
END;
/


-- CONSULTA 4 - Insertar un pedido y los detalles de pedido correspondientes

DECLARE

    v_id_order PEDIDOS.ID_PEDIDO%TYPE;
    v_date PEDIDOS.FECHA%TYPE := DATE '2025-04-01';
    v_total PEDIDOS.IMPORTE_TOTAL%TYPE := 55.00;

BEGIN

    INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador)
    VALUES (v_date, 'Pendiente', v_total, 3, '33333333C')
    RETURNING id_pedido INTO v_id_order;

    INSERT INTO detalles_pedido (id_pedido, id_album, cantidad)
    VALUES (v_id_order, 2, 3);

    -- COMMIT;

    FOR v_insert IN (SELECT p.id_pedido, p.fecha, p.estado, p.importe_total, d.id_album, d.cantidad FROM pedidos p 
                     JOIN detalles_pedido d ON p.id_pedido = d.id_pedido
                     WHERE p.id_pedido = v_id_order) LOOP

        DBMS_OUTPUT.PUT_LINE('ID Pedido: ' || v_insert.id_pedido || ' | Fecha: ' || TO_CHAR(v_insert.fecha, 'DD/MM/YYYY') || ' | Estado: ' || v_insert.estado || ' | Importe total: ' || v_insert.importe_total || ' | Álbum: ' || v_insert.id_album || ' | Cantidad: ' || v_insert.cantidad);

    END LOOP;
END;
/

-- CONSULTA 4 - Insertar un pedido con varios álbumes distintos, insertar sus detalles, y calcular el total del pedido

DECLARE

    v_total PEDIDOS.IMPORTE_TOTAL%TYPE := 0;
    v_id_order PEDIDOS.ID_PEDIDO%TYPE;
    
    CURSOR c_albums IS
        SELECT id_album, precio FROM albumes WHERE id_genero = 2; 
BEGIN

    INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador)
    VALUES (SYSDATE, 'Pendiente', 0, 4, '55555555E')
    RETURNING id_pedido INTO v_id_order;

    FOR v_album IN c_albums LOOP
        v_total := v_total + v_album.precio;
        INSERT INTO detalles_pedido (id_pedido, id_album, cantidad)
        VALUES (v_id_order, v_album.id_album, 1);
    END LOOP;

    UPDATE pedidos SET importe_total = ROUND(v_total, 2) WHERE id_pedido = v_id_order;

    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Pedido #' || v_id_order || ' Importe total: ' || v_total);
    
EXCEPTION

    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('ERROR INSERT 4: ' || SQLERRM);
        ROLLBACK;
END;
/

-- ============================================================
--  REGISTROS DE MODIFICACIÓN
-- ============================================================

-- CONSULTA 1 - Cambiar el estado del pedido con ID 1

DECLARE 

    v_id_order PEDIDOS.ID_PEDIDO%TYPE := 1;
    v_status PEDIDOS.ESTADO%TYPE := 'Enviado';

BEGIN

    UPDATE pedidos
    SET estado = v_status
    WHERE id_pedido = v_id_order;

    IF SQL%ROWCOUNT = 0 THEN
    
        DBMS_OUTPUT.PUT_LINE('Pedido ' || v_id_order || ' no encontrado');
    ELSE
        -- COMMIT;
        DBMS_OUTPUT.PUT_LINE('Estado de pedido' || v_id_order || ' cambiado a: ' || v_status);
    END IF;

END;
/

-- CONSULTA 2 - Actualizar el estado de un pedido de Pendiente a Enviado

DECLARE 

    v_id_order PEDIDOS.ID_PEDIDO%TYPE := 2;
    v_current_status PEDIDOS.ESTADO%TYPE;

BEGIN

    SELECT estado INTO v_current_status FROM pedidos 
    WHERE id_pedido = v_id_order;
    
    IF v_current_status = 'Pendiente' THEN
    
        UPDATE pedidos 
        SET estado = 'Enviado' 
        WHERE id_pedido = v_id_order;
        
        -- COMMIT;
        
        DBMS_OUTPUT.PUT_LINE('Pedido Pendiente actualizado a Enviado');
        
    ELSE
    
        DBMS_OUTPUT.PUT_LINE('El pedido no estaba en Pendiente, estaba en: ' || v_current_status);
    
    END IF;
    
END;
/

-- CONSULTA 3 - Reasignar trabajador a todos los pedidos de un cliente

DECLARE

    v_id_client CLIENTES.ID_CLIENTE%TYPE := 3;
    v_new_worker TRABAJADORES.DNI%TYPE := '77777777G';
    v_count NUMBER;
    
BEGIN

    UPDATE pedidos 
    SET DNI_trabajador = v_new_worker 
    WHERE id_cliente = v_id_client;
    
    v_count := SQL%ROWCOUNT;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE(v_count || ' pedidos reasignados');
    
END;
/

-- CONSULTA 4 - Aplicar descuento del 10% a pedidos con importe mayor de 50

DECLARE

    v_discount PEDIDOS.IMPORTE_TOTAL%TYPE := 0.10;
    v_count PEDIDOS.IMPORTE_TOTAL%TYPE := 0;
    v_original_price PEDIDOS.IMPORTE_TOTAL%TYPE;
    
    CURSOR c_orders IS
        SELECT id_pedido, importe_total FROM pedidos
        WHERE importe_total > 50 FOR UPDATE;
        
BEGIN

    FOR v_order IN c_orders LOOP
    
        v_original_price := v_order.importe_total;
    
        UPDATE pedidos
        SET importe_total = ROUND(v_order.importe_total * (1 - v_discount), 2)
        WHERE CURRENT OF c_orders;
        
        v_count := v_count + 1;
        
        DBMS_OUTPUT.PUT_LINE('Pedido #' || v_order.id_pedido || ' | Antes: ' || v_original_price || ' | Despues: ' || v_order.importe_total);
    
    END LOOP;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Descuento aplicado a ' || v_count || ' pedidos');

END;
/

-- CONSULTA 5 - Marcar como Entregado los pedidos Enviados de hace más de 7 días

DECLARE

    v_count NUMBER := 0;
    
BEGIN

    UPDATE pedidos
    SET estado = 'Entregado'
    WHERE estado = 'Enviado'
    AND TRUNC(SYSDATE) - fecha > 7;
 
    v_count := SQL%ROWCOUNT;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE(v_count || ' pedidos marcados como Entregado');

END;
/

-- ============================================================
--  REGISTROS DE ELIMINACIÓN
-- ============================================================

-- CONSULTA 1 - Eliminar el pedido 11 si existe

DECLARE

    v_id_order PEDIDOS.ID_PEDIDO%TYPE := 11;
    v_exists NUMBER;
    
BEGIN

    SELECT COUNT(*) INTO v_existv_exists FROM pedidos 
    WHERE id_pedido = v_id_order;

    IF v_exists = 0 THEN
    
        DBMS_OUTPUT.PUT_LINE('Pedido ' || v_id_order || ' no existe');
        
    ELSE
    
        DELETE FROM pedidos 
        WHERE id_pedido = v_id_order;
        
        -- COMMIT;
        
        DBMS_OUTPUT.PUT_LINE('Pedido ' || v_id_order || ' eliminado');
        
    END IF;
END;
/

-- CONSULTA 2 - Eliminar pedidos cancelados

DECLARE

    v_count NUMBER := 0;
    v_id PEDIDOS.ID_PEDIDO%TYPE;
    
    CURSOR c_canceled IS
        SELECT id_pedido FROM pedidos 
        WHERE estado = 'Cancelado';
        
BEGIN
    FOR v_order IN c_canceled LOOP
    
        v_id := v_order.id_pedido;
    
        DELETE FROM pedidos WHERE id_pedido = v_order.id_pedido;
        
        v_count := v_count + 1;
        
        DBMS_OUTPUT.PUT_LINE('Eliminado pedido #' || v_id);
        
    END LOOP;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE(v_count || ' pedidos cancelados eliminados');
    
END;
/

-- CONSULTA 3 - Eliminar pedidos con importe 0

DECLARE

    v_count NUMBER;
    
BEGIN

    DELETE FROM pedidos WHERE importe_total = 0;
    
    v_count := SQL%ROWCOUNT;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE(v_count || ' pedidos con importe 0 eliminados');

END;
/

-- CONSULTA 4 - Eliminar pedidos pendientes de hace más de 90 días

DECLARE

    v_days NUMBER := 90;
    v_count NUMBER;
    
BEGIN
    DELETE FROM pedidos
    WHERE estado = 'Pendiente'
    AND TRUNC(SYSDATE) - fecha > v_days;

    v_count := SQL%ROWCOUNT;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE(v_count || ' pedidos Pendiente de más de 90 días de antigüedad');
    
END;
/

-- CONSULTA 5 - Eliminar todos los pedidos del cliente 10

DECLARE

    v_id_client NUMBER := 10;
    v_count NUMBER;
    
BEGIN
    DELETE FROM pedidos WHERE id_cliente = v_id_client;
    v_count := SQL%ROWCOUNT;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE(v_count || ' pedidos del cliente ' || v_id_client || ' eliminados');

END;
/

-- ============================================================
--  CONSULTAS CON CURSORES
-- ============================================================

-- CONSULTA 1 - Listar todos los pedidos con datos del cliente

DECLARE

    CURSOR c_orders IS
        SELECT p.id_pedido, p.fecha, p.estado, p.importe_total, c.nombre, c.apellido FROM pedidos p
        JOIN clientes c ON c.id_cliente = p.id_cliente
        ORDER BY p.fecha DESC;
        
BEGIN

    DBMS_OUTPUT.PUT_LINE('Todos los pedidos');
    
    FOR v_order IN c_orders LOOP
    
        DBMS_OUTPUT.PUT_LINE('Pedido #' || v_order.id_pedido || ' Fecha: ' || TO_CHAR(v_order.fecha, 'DD/MM/YYYY') || ' Estado: ' || v_order.estado || ' Importe total: ' || v_order.importe_total || ' EUR' || ' Cliente: ' || v_order.nombre || ' ' || v_order.apellido);
            
    END LOOP;
END;
/

-- CONSULTA 2 - Contar pedidos y sumar su importe según su estado

DECLARE

    CURSOR c_estados IS
        SELECT estado, COUNT(*) AS total, SUM(importe_total) AS sum FROM pedidos
        GROUP BY estado
        ORDER BY total DESC;
        
BEGIN

    DBMS_OUTPUT.PUT_LINE('Pedidos por estado');
    
    FOR v_order IN c_estados LOOP
    
        DBMS_OUTPUT.PUT_LINE(v_order.estado || ' | Cantidad: ' || v_order.total || ' | Total: ' || v_order.suma || ' EUR');
    
    END LOOP;
END;
/

-- CONSULTA 3 - Listar todos los pedidos del cliente 1

DECLARE

    CURSOR c_client (p_id NUMBER) IS
        SELECT id_pedido, fecha, estado, importe_total FROM pedidos
        WHERE id_cliente = p_id
        ORDER BY fecha;
        
    v_client CLIENTES.ID_CLIENTE%TYPE := 1;
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('Pedidos del cliente ' || v_client);
    
    FOR v_order IN c_client(v_client) LOOP
    
        DBMS_OUTPUT.PUT_LINE('Pedido #' || v_order.id_pedido || ' Fecha: ' || TO_CHAR(v_order.fecha, 'DD/MM/YYYY') || ' Estado: ' || v_order.estado || ' Importe total: ' || v_order.importe_total || ' EUR');
            
    END LOOP;
END;
/

-- CONSULTA 4 - Mostrar pedidos sin entregar e indicar si llevan más de 15 días en curso

DECLARE
    CURSOR c_in_progress IS
        SELECT id_pedido, fecha, estado, id_cliente FROM pedidos
        WHERE estado IN ('Pendiente', 'Enviado');
        
    v_days NUMBER;
    v_alert VARCHAR2(20);
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('Pedidos abiertos');
    
    FOR v_order IN c_in_progress LOOP
    
        v_days := TRUNC(SYSDATE) - v_order.fecha;

        IF v_days > 15 THEN
        
            v_alert := 'REVISAR';
            
        ELSE
        
            v_alert := 'OK';
            
        END IF;

        DBMS_OUTPUT.PUT_LINE('Pedido #' || v_order.id_pedido ||' Estado: ' || v_order.estado ||' Días en progreso: ' || v_days || ' dias' ||' Alerta: ' || v_alert);
    
    END LOOP;
END;
/

-- CONSULTA 5 - Listar todos los pedidos ordenados por fecha

DECLARE

    CURSOR c_orders IS
        SELECT id_pedido, fecha, estado, importe_total FROM pedidos
        ORDER BY fecha;
        
BEGIN

    DBMS_OUTPUT.PUT_LINE('Listado de pedidos');
    
    FOR v_order IN c_orders LOOP
    
        DBMS_OUTPUT.PUT_LINE('Pedido #' || v_order.id_pedido || ' Fecha: ' || TO_CHAR(v_order.fecha, 'DD/MM/YYYY') || ' Estado: ' || v_order.estado || ' Importe: ' || v_order.importe_total || ' EUR');
        
    END LOOP;
END;
/

-- ============================================================
--  PROCEDIMIENTOS
-- ============================================================

-- PROCEDIMIENTO 1 - Insertar un pedido comprobando los datos

CREATE OR REPLACE PROCEDURE pr_insert_order (p_id_client IN CLIENTES.ID_CLIENTE%TYPE, p_dni_worker IN TRABAJADORES.DNI%TYPE, p_id_album IN ALBUMES.ID_ALBUM%TYPE, p_amount IN NUMBER, p_id_order OUT PEDIDOS.ID_PEDIDO%TYPE) 
AS
    v_price NUMBER;
    v_stock NUMBER;
    v_total NUMBER;

BEGIN

    SELECT precio, stock INTO v_price, v_stock FROM albumes 
    WHERE id_album = p_id_album;

    IF v_stock < p_amount THEN

        RAISE_APPLICATION_ERROR(-20001, 'Stock insuficiente: ' || v_stock);

    END IF;

    v_total := ROUND(v_price * p_amount, 2);

    INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador)
    VALUES (SYSDATE, 'Pendiente', v_total, p_id_client, p_dni_worker)
    RETURNING id_pedido INTO p_id_order;

    UPDATE albumes 
    SET stock = stock - p_amount 
    WHERE id_album = p_id_album;

    -- COMMIT;

    DBMS_OUTPUT.PUT_LINE('Pedido #' || p_id_order || ' creado. Importe: ' || v_total || ' EUR');

EXCEPTION

    WHEN NO_DATA_FOUND THEN

        DBMS_OUTPUT.PUT_LINE('ERROR: Album no encontrado');

        ROLLBACK;

    WHEN OTHERS THEN

        DBMS_OUTPUT.PUT_LINE('ERROR: ' || SQLERRM);
        ROLLBACK;
        
END pr_insert_order;
/

DECLARE

    v_id NUMBER;

BEGIN

    pr_insert_order(1, '11111111A', 2, 1, v_id);
    
    DBMS_OUTPUT.PUT_LINE('ID generado: ' || v_id);
    
END;
/

-- PROCEDIMIENTO 2 - Mostrar resumen de pedidos de un mes

CREATE OR REPLACE PROCEDURE pr_month_summary (p_year IN NUMBER, p_month IN NUMBER)
AS
    v_total_orders NUMBER := 0;
    v_total_amount NUMBER := 0;

    CURSOR c_month IS
        SELECT p.id_pedido, p.fecha, p.estado, p.importe_total, c.nombre, c.apellido FROM pedidos p
        JOIN clientes c ON c.id_cliente = p.id_cliente
        WHERE EXTRACT(YEAR FROM p.fecha) = p_year
          AND EXTRACT(MONTH FROM p.fecha) = p_month
        ORDER BY p.fecha;
        
BEGIN

    IF p_month < 1 OR p_month > 12 THEN
    
        RAISE_APPLICATION_ERROR(-20002, 'Mes no valido: ' || p_month);
        
    END IF;

    DBMS_OUTPUT.PUT_LINE('Resumen pedidos ' || p_month || '/' || p_year || '');

    FOR v_order IN c_month LOOP
    
        v_total_orders := v_total_orders + 1;
        v_total_amount := v_total_amount + v_order.importe_total;
        
        DBMS_OUTPUT.PUT_LINE('Pedido #' || v_order.id_pedido || ' Fecha: ' || TO_CHAR(v_order.fecha, 'DD/MM') || ' Estado: ' || v_order.estado || ' Importe total: ' || v_order.importe_total || ' EUR' || ' Cliente: ' || v_order.nombre || ' ' || v_order.apellido);
            
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('Total pedidos : ' || v_total_orders);
    DBMS_OUTPUT.PUT_LINE('Total importe : ' || v_total_amount || ' EUR');
    
EXCEPTION

    WHEN OTHERS THEN
    
        DBMS_OUTPUT.PUT_LINE('ERROR: ' || SQLERRM);
        
END pr_month_summary;
/

BEGIN
    pr_month_summary(2025, 1);
    pr_month_summary(2025, 2);
END;
/

-- ============================================================
--  FUNCIONES
-- ============================================================

-- FUNCIÓN 1 - Devolver el número de pedidos de un cliente

CREATE OR REPLACE FUNCTION fn_num_orders (p_id_client IN CLIENTES.ID_CLIENTE%TYPE) 
RETURN NUMBER AS

    v_total NUMBER;
    
BEGIN

    SELECT COUNT(*) INTO v_total
    FROM pedidos WHERE id_cliente = p_id_client;

    RETURN v_total;
    
EXCEPTION

    WHEN OTHERS THEN
    
        RETURN -1;
        
END fn_num_orders;
/

DECLARE

    v_result NUMBER;
    
BEGIN

    FOR v_i IN 1..5 LOOP
    
        v_result := fn_num_orders(v_i);
        
        DBMS_OUTPUT.PUT_LINE('Cliente #' || v_i || ' tiene ' || v_result || ' pedidos');
        
    END LOOP;
END;
/

-- FUNCIÓN 2 - Devolver el estado de un pedido y si está atrasado

CREATE OR REPLACE FUNCTION fn_info_order(p_id_order IN PEDIDOS.ID_PEDIDO%TYPE)
RETURN VARCHAR2 AS

    v_status PEDIDOS.ESTADO%TYPE;
    v_date PEDIDOS.FECHA%TYPE;
    v_days NUMBER;
    v_text VARCHAR2(200);
    
BEGIN
    SELECT estado, fecha INTO v_status, v_date
    FROM pedidos WHERE id_pedido = p_id_order;

    v_days := TRUNC(SYSDATE) - v_date;

    IF v_status IN ('Pendiente', 'Enviado') AND v_days > 15 THEN
    
        v_text := 'ATRASADO - ' || v_days || ' dias en estado ' || v_status;
        
    ELSIF v_status = 'Entregado' OR v_status = 'Pagado' THEN
    
        v_text := 'COMPLETADO - Estado: ' || v_status;
        
    ELSIF v_status = 'Cancelado' THEN
    
        v_text := 'CANCELADO';
        
    ELSE
    
        v_text := 'EN CURSO - ' || v_days || ' dias en estado ' || v_status;
        
    END IF;

    RETURN v_text;
    
EXCEPTION

    WHEN NO_DATA_FOUND THEN
    
        RETURN 'ERROR: Pedido ' || p_id_order || ' no encontrado';
        
    WHEN OTHERS THEN
    
        RETURN 'ERROR: ' || SQLERRM;
        
END fn_info_order;
/

BEGIN

    FOR v_i IN 1..5 LOOP
    
        DBMS_OUTPUT.PUT_LINE('Pedido #' || v_i || ': ' || fn_info_order(v_i));
        
    END LOOP;
END;
/
