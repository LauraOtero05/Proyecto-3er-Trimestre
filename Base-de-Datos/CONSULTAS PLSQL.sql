SET SERVEROUTPUT ON; 

-- ============================================================
-- --------- TABLA TRABAJADORES ---------
-- ============================================================

-- ============================================================
--  REGISTROS DE CREACIÓN
-- ============================================================

-- CONSULTA 1 - Insertar un trabajador

DECLARE

    v_dni TRABAJADORES.DNI%TYPE := 'A1234567Z';
    v_worker NUMBER;
    
BEGIN
    SELECT COUNT(*) INTO v_worker FROM trabajadores WHERE DNI = v_dni;

    IF v_worker > 0 THEN
    
        DBMS_OUTPUT.PUT_LINE('Trabajador con el DNI ' || v_dni || ' ya registrado');
        
    ELSE
    
        INSERT INTO trabajadores VALUES (
            v_dni, 'Juan', 'Vega', 'Ventas',
            'juan.vega@74minutes.com', 'whash050'
        );
        
        -- COMMIT;
        
        DBMS_OUTPUT.PUT_LINE('Trabajador con el DNI ' || v_dni || ' añadido');
        
    END IF;
END;
/

-- CONSULTA 2 - Insertar un trabajador comprobando que el DNI no exista antes

DECLARE

    v_dni TRABAJADORES.DNI%TYPE := 'X2222222B';
    v_worker NUMBER;
    
BEGIN

    SELECT COUNT(*) INTO v_worker FROM trabajadores WHERE DNI = v_dni;

    IF v_worker > 0 THEN
    
        DBMS_OUTPUT.PUT_LINE('DNI ya registrado');
        
    ELSE
    
        INSERT INTO trabajadores VALUES (
            v_dni, 'Sara', 'Molina', 'Almacen',
            'sara.molina@74minutes.com', 'whash051'
        );
        
        -- COMMIT;
        
        DBMS_OUTPUT.PUT_LINE('Sara Molina añadida');
        
    END IF;
END;
/

-- CONSULTA 3 - Insertar un trabajador con un email generado según su nombre

DECLARE

    v_dni TRABAJADORES.DNI%TYPE := 'X3333333C';
    v_name TRABAJADORES.NOMBRE%TYPE := 'Marta';
    v_last_name TRABAJADORES.APELLIDO%TYPE := 'Leon';
    v_email TRABAJADORES.EMAIL%TYPE;
    
BEGIN
    
    v_email := LOWER(v_name) || '.' || LOWER(v_last_name) || '@74minutes.com';

    INSERT INTO trabajadores VALUES (
        v_dni, v_name, v_last_name, 'Almacen', v_email, 'whash052'
    );
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Email generado: ' || v_email);

END;
/

-- CONSULTA 4 - Insertar varios trabajadores con un bucle

DECLARE

    TYPE t_name IS TABLE OF VARCHAR2(100) INDEX BY PLS_INTEGER;
    v_names t_name;
    
BEGIN

    v_names(1) := 'Pedro';
    v_names(2) := 'Rosa';
    v_names(3) := 'Tomas';

    FOR i IN 1..3 LOOP
    
        INSERT INTO trabajadores VALUES (
            'X444444' || i || 'D', 
            v_names(i), 'Apellido' || i, 'Ventas', LOWER(v_names(i)) || i || '@74min.com',
            'whash053' || i
        );
        
        DBMS_OUTPUT.PUT_LINE('Alta: ' || v_names(i));
        
    END LOOP;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('3 trabajadores insertados');
END;
/

-- CONSULTA 5 - Insertar un trabajador y su número de teléfono

DECLARE

    v_dni TRABAJADORES.DNI%TYPE := 'X5555555E';
    v_phone TELEFONOS_TRABAJADOR.TELEFONO%TYPE := '699000099';
    
BEGIN

    INSERT INTO trabajadores VALUES (
        v_dni, 'Alicia', 'Ruiz', 'Administrador',
        'alicia.ruiz@74min.com', 'whash054'
    );
    
    INSERT INTO telefonos_trabajador VALUES (v_dni, v_phone);
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Alicia con telefono ' || v_phone);
END;
/


-- ============================================================
--  REGISTROS DE MODIFICACIÓN
-- ============================================================

-- CONSULTA 1 - Cambiar el rol del trabajador con DNI 22222222B

DECLARE

    v_dni TRABAJADORES.DNI%TYPE := '22222222B';
    v_new_rol TRABAJADORES.ROL%TYPE := 'Almacen';
BEGIN
    UPDATE trabajadores SET rol = v_new_rol WHERE DNI = v_dni;

    IF SQL%ROWCOUNT = 0 THEN
    
        DBMS_OUTPUT.PUT_LINE('DNI no encontrado');
    ELSE
    
        -- COMMIT;
        DBMS_OUTPUT.PUT_LINE('Rol cambiado a: ' || v_new_rol);
        
    END IF;
END;
/

-- CONSULTA 2 - Cambiar el rol del trabajador 33333333C, comprobando su rol actual

DECLARE

    v_dni TRABAJADORES.DNI%TYPE := '33333333C';
    v_rol TRABAJADORES.ROL%TYPE;
    v_new_rol TRABAJADORES.ROL%TYPE := 'Ventas';
    
BEGIN
    SELECT rol INTO v_rol FROM trabajadores WHERE DNI = v_dni;

    IF v_rol = v_new_rol THEN
    
        DBMS_OUTPUT.PUT_LINE('El trabajador ya tiene ese rol');
        
    ELSE
    
        UPDATE trabajadores SET rol = v_new_rol WHERE DNI = v_dni;
        
        -- COMMIT;
        
        DBMS_OUTPUT.PUT_LINE('Rol: ' || v_rol || ' Cambiado a: ' || v_new_rol);
        
    END IF;
END;
/

-- CONSULTA 3 - Actualizar email de todos los trabajadores del departamento de Ventas

DECLARE

    v_rol TRABAJADORES.ROL%TYPE := 'Ventas';
    v_shop_email VARCHAR2(50) := '@74minutes.com';
    v_count NUMBER := 0;
    
    CURSOR c_ventas IS
        SELECT DNI, nombre, apellido FROM trabajadores WHERE rol = v_rol FOR UPDATE;
        
BEGIN

    FOR v_worker IN c_ventas LOOP

        UPDATE trabajadores
        SET email = LOWER(v_worker.nombre) || '.' || LOWER(v_worker.apellido) || v_shop_email
        WHERE CURRENT OF c_ventas;
        
        v_count := v_count + 1;
        
    END LOOP;

    -- COMMIT;

    DBMS_OUTPUT.PUT_LINE(v_count || ' emails actualizados');

END;
/

-- CONSULTA 4 - Normalizar nombre y apellido con INITCAP en todos

DECLARE

    v_count NUMBER := 0;
    CURSOR c_all IS
        SELECT DNI, nombre, apellido FROM trabajadores FOR UPDATE;

BEGIN

    FOR v_worker IN c_all LOOP

        UPDATE trabajadores
        SET nombre = INITCAP(v_worker.nombre), apellido = INITCAP(v_worker.apellido)
        WHERE CURRENT OF c_all;
        
        v_count := v_count + 1;

    END LOOP;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE( v_count || ' nombres normalizados');
    
END;
/

-- CONSULTA 5 - Actualizar el password hash de todos los trabajadores de Almacen

DECLARE

    v_new_passwd VARCHAR2(255) := 'HASH_RESET_' || TO_CHAR(SYSDATE, 'DDMMYYYY');
    v_count NUMBER;
BEGIN

    UPDATE trabajadores 
    SET password_hash = v_new_passwd 
    WHERE rol = 'Almacen';
    
    v_count := SQL%ROWCOUNT;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE(v_count || ' contraseñas actualizadas');
    
END;
/

-- ============================================================
--  REGISTROS DE ELIMINACIÓN
-- ============================================================

-- CONSULTA 1 - Eliminar el trabajador X1111111A si no tiene pedidos activos

DECLARE

    v_dni TRABAJADORES.DNI%TYPE := 'X1111111A';
    v_active NUMBER;
    
BEGIN

    SELECT COUNT(*) INTO v_active FROM pedidos 
    WHERE DNI_trabajador = v_dni AND estado IN ('Pendiente','Enviado');

    IF v_active > 0 THEN
    
        DBMS_OUTPUT.PUT_LINE('Tiene ' || v_active || ' pedidos activos, no se puede borrar');
        
    ELSE
    
        DELETE FROM telefonos_trabajador WHERE DNI = v_dni;
        DELETE FROM trabajadores WHERE DNI = v_dni;
        
        -- COMMIT;
        DBMS_OUTPUT.PUT_LINE('Trabajador ' || v_dni || ' eliminado');
        
    END IF;
    
END;
/

-- CONSULTA 2 - Eliminar varios trabajadores con un bucle

DECLARE

    TYPE t_list IS TABLE OF VARCHAR2(20) INDEX BY PLS_INTEGER;
    v_list t_list;
    v_count NUMBER := 0;
    
BEGIN
    v_list(1) := 'X2222222B';
    v_list(2) := 'X3333333C';
    v_list(3) := 'X5555555E';

    FOR v_i IN 1..3 LOOP
    
        DELETE FROM telefonos_trabajador WHERE DNI = v_list(v_i);
        DELETE FROM trabajadores WHERE DNI = v_list(v_i);
        
        IF SQL%ROWCOUNT > 0 THEN
        
            v_count := v_count + 1;
            
            DBMS_OUTPUT.PUT_LINE('Eliminado: ' || v_list(v_i));
            
        END IF;
        
    END LOOP;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE(v_count || ' trabajadores eliminados');
    
END;
/

-- CONSULTA 3 - Eliminar trabajadores sin ningún pedido asignado

DECLARE

    v_count NUMBER := 0;
    v_delete_dni TRABAJADORES.DNI%TYPE;
    
    CURSOR c_no_orders IS
        SELECT DNI FROM trabajadores
        WHERE DNI NOT IN (
            SELECT DISTINCT DNI_trabajador FROM pedidos 
            WHERE DNI_trabajador IS NOT NULL
        )
        AND rol != 'Gerente';
        
BEGIN

    FOR v_worker IN c_no_orders LOOP
    
        v_delete_dni := v_worker.DNI;
    
        DELETE FROM telefonos_trabajador WHERE DNI = v_worker.DNI;
        DELETE FROM trabajadores WHERE DNI = v_worker.DNI;
        
        v_count := v_count + 1;

        DBMS_OUTPUT.PUT_LINE('Baja: ' || v_worker.DNI);
        
    END LOOP;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE(v_count || ' trabajadores sin pedidos eliminados');
    
END;
/

-- CONSULTA 4 - Eliminar los números de teléfonos del trabajador 88888888H

DECLARE

    v_dni TRABAJADORES.DNI%TYPE := '88888888H';
    v_count NUMBER;
    
BEGIN

    DELETE FROM telefonos_trabajador WHERE DNI = v_dni;
    
    v_count := SQL%ROWCOUNT;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE(v_count || ' telefonos eliminados del trabajador ' || v_dni);
    
END;
/

-- CONSULTA 5 - Eliminar trabajadores y su número de teléfono con un bucle

DECLARE

    v_count NUMBER := 0;
    
BEGIN

    FOR i IN 1..3 LOOP
    
        DELETE FROM telefonos_trabajador WHERE DNI = 'X444444' || i || 'D';
        DELETE FROM trabajadores WHERE DNI = 'X444444' || i || 'D';
        
        IF SQL%ROWCOUNT > 0 THEN
        
            v_count := v_count + 1;
            
            DBMS_OUTPUT.PUT_LINE('Eliminado: X444444' || i || 'D');
            
        END IF;
    END LOOP;
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE(v_count || ' trabajadores eliminados');
    
END;
/

-- ============================================================
--  CONSULTAS CON CURSORES
-- ============================================================

-- CONSULTA 1 - Listar todos los trabajadores y cuántos pedidos gestionan

DECLARE

    CURSOR c_workers IS
        SELECT t.DNI, t.nombre, t.apellido, t.rol, COUNT(p.id_pedido) AS total_pedidos FROM trabajadores t
        LEFT JOIN pedidos p ON p.DNI_trabajador = t.DNI
        GROUP BY t.DNI, t.nombre, t.apellido, t.rol
        ORDER BY total_pedidos DESC;
BEGIN

    DBMS_OUTPUT.PUT_LINE('Trabajadores y pedidos');
    
    FOR v_worker IN c_workers LOOP
    
        DBMS_OUTPUT.PUT_LINE(v_worker.DNI || ' Nombre: ' || v_worker.nombre || ' ' || v_worker.apellido || ' Rol: ' || v_worker.rol || ' Total de pedidos: ' || v_worker.total_pedidos);
    
    END LOOP;
    
END;
/

-- CONSULTA 2 - Listar la cantidad de trabajadores por rol y el total de trabajadores

DECLARE

    CURSOR c_roles IS
        SELECT rol, COUNT(*) AS total FROM trabajadores
        GROUP BY rol ORDER BY total DESC;
    v_total_global NUMBER := 0;
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('Cantidad de trabajadores por rol');
    
    FOR v_worker IN c_roles LOOP
    
        v_total_global := v_total_global + v_worker.total;
        
        DBMS_OUTPUT.PUT_LINE(v_worker.rol || ': ' || v_worker.total || ' trabajadores');
        
    END LOOP;
    
    DBMS_OUTPUT.PUT_LINE('Total de trabajadores: ' || v_total_global);
    
END;
/

-- CONSULTA 3 - Listar todos los trabajadores con el rol Ventas

DECLARE

    CURSOR c_by_rol (p_rol VARCHAR2) IS
        SELECT DNI, nombre, apellido, email FROM trabajadores WHERE rol = p_rol;
    v_rol VARCHAR2(20) := 'Ventas';
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('Trabajadores de ' || v_rol);
    
    FOR v_worker IN c_by_rol(v_rol) LOOP
    
        DBMS_OUTPUT.PUT_LINE(v_worker.DNI || ' Nombre: ' || v_worker.nombre || ' ' || v_worker.apellido || ' Email: ' || v_worker.email);
        
    END LOOP;
    
END;
/

-- CONSULTA 4 - Listar los trabajadores sin número de teléfono registrado

DECLARE

    CURSOR c_no_phone IS
        SELECT DNI, nombre, apellido, rol FROM trabajadores
        WHERE DNI NOT IN (SELECT DNI FROM telefonos_trabajador);
    v_count NUMBER := 0;
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('Trabajadores sin telefono');
    
    FOR v_worker IN c_no_phone LOOP
        v_count := v_count + 1;
        DBMS_OUTPUT.PUT_LINE(v_worker.DNI || ' Nombre: ' || v_worker.nombre || ' ' || v_worker.apellido || ' Rol: ' || v_worker.rol);
        
    END LOOP;
    
    IF v_count = 0 THEN
    
        DBMS_OUTPUT.PUT_LINE('Todos tienen telefono registrado');
        
    END IF;
END;
/

-- CONSULTA 5 - Ranking de trabajadores por volumen de ventas gestionadas

DECLARE

    CURSOR c_sales IS
        SELECT t.nombre, t.apellido, t.rol, COUNT(p.id_pedido) AS num_pedidos, NVL(SUM(p.importe_total), 0) AS ventas_total FROM trabajadores t
        LEFT JOIN pedidos p ON p.DNI_trabajador = t.DNI
        GROUP BY t.nombre, t.apellido, t.rol
        ORDER BY ventas_total DESC;
    v_top NUMBER := 0;
    
BEGIN
    DBMS_OUTPUT.PUT_LINE('Ranking de ventas');
    
    FOR v_worker IN c_sales LOOP
        v_top := v_top + 1;
        DBMS_OUTPUT.PUT_LINE(v_top || '. ' || v_worker.nombre || ' ' || v_worker.apellido || ' Rol: ' || v_worker.rol || ' Pedidos: ' || v_worker.num_pedidos || ' Ventas: ' || v_worker.ventas_total || ' EUR');
        
    END LOOP;
END;
/

-- ============================================================
--  PROCEDIMIENTOS
-- ============================================================

-- PROCEDIMIENTO 1 - Dar de alta a un trabajador comprobando su DNI y rol

CREATE OR REPLACE PROCEDURE pr_sign_worker (p_dni IN TRABAJADORES.DNI%TYPE, p_name IN TRABAJADORES.NOMBRE%TYPE, p_last_name IN TRABAJADORES.APELLIDO%TYPE, p_rol IN TRABAJADORES.ROL%TYPE, p_email IN TRABAJADORES.EMAIL%TYPE) 
AS
    v_exists NUMBER;
    
BEGIN

    SELECT COUNT(*) INTO v_exists FROM trabajadores WHERE DNI = p_dni;
    
    IF v_exists > 0 THEN
    
        RAISE_APPLICATION_ERROR(-20001, 'DNI ya registrado: ' || p_dni);
        
    END IF;

    IF p_rol NOT IN ('Administrador','Ventas','Almacen','Gerente') THEN
    
        RAISE_APPLICATION_ERROR(-20002, 'Rol no valido: ' || p_rol);
        
    END IF;

    INSERT INTO trabajadores VALUES (
        UPPER(p_dni),
        INITCAP(p_name),
        INITCAP(p_last_name),
        p_rol,
        LOWER(p_email),
        'HASH_PENDIENTE'
    );
    
    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Se ha dado de alta a: ' || p_name || ' ' || p_last_name || ' Rol: ' || p_rol);
    
EXCEPTION

    WHEN OTHERS THEN
    
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR pr_sign_trabajador: ' || SQLERRM);
        
END pr_sign_worker;
/

BEGIN
    pr_sign_worker('Y1234567Z', 'Jorge', 'Blanco', 'Ventas', 'jorge.blanco@74minutes.com');
    pr_sign_worker('Y1234567Z', 'Otro', 'Test', 'Ventas', 'otro@74minutes.com');
END;
/

-- PROCEDIMIENTO 2 - Reasignar pedidos de un trabajador a otro

CREATE OR REPLACE PROCEDURE pr_reasign_orders (p_dni_origin IN TRABAJADORES.DNI%TYPE, p_dni_destination IN TRABAJADORES.DNI%TYPE) 
AS
    v_exists_origin NUMBER;
    v_exists_destination NUMBER;
    v_count NUMBER := 0;
    
    CURSOR c_orders IS
        SELECT id_pedido FROM pedidos
        WHERE DNI_trabajador = p_dni_origin AND estado IN ('Pendiente', 'Enviado') FOR UPDATE;
        
BEGIN

    SELECT COUNT(*) INTO v_exists_origin FROM trabajadores WHERE DNI = p_dni_origin;
    SELECT COUNT(*) INTO v_exists_destination FROM trabajadores WHERE DNI = p_dni_destination;

    IF v_exists_origin = 0 THEN
    
        RAISE_APPLICATION_ERROR(-20003, 'Trabajador de origen no existe');
        
    END IF;
    
    IF v_exists_destination = 0 THEN
    
        RAISE_APPLICATION_ERROR(-20004, 'Trabajador de destino no existe');
        
    END IF;

    FOR v_order IN c_orders LOOP
    
        UPDATE pedidos SET DNI_trabajador = p_dni_destination
        WHERE CURRENT OF c_orders;
        
        v_count := v_count + 1;
        
        DBMS_OUTPUT.PUT_LINE('Pedido #' || v_order.id_pedido || ' reasignado');
        
    END LOOP;

    -- COMMIT;
    
    DBMS_OUTPUT.PUT_LINE(v_count || ' pedidos reasignados a ' || p_dni_destination);
    
EXCEPTION

    WHEN OTHERS THEN
    
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR pr_reasign_orders: ' || SQLERRM);
        
END pr_reasign_orders;
/

BEGIN

    pr_reasign_orders('11111111A', '22222222B');
    
END;
/

-- ============================================================
--  FUNCIONES
-- ============================================================

-- FUNCIONES 1 - Devolver cuántos pedidos activos tiene un trabajador

CREATE OR REPLACE FUNCTION fn_active_orders (p_dni IN TRABAJADORES.DNI%TYPE) 
RETURN NUMBER AS

    v_total NUMBER;
    
BEGIN

    SELECT COUNT(*) INTO v_total FROM pedidos
    WHERE DNI_trabajador = p_dni AND estado IN ('Pendiente', 'Enviado');

    RETURN v_total;
    
EXCEPTION

    WHEN OTHERS THEN
    
        RETURN -1;
        
END fn_active_orders;
/

DECLARE

    v_result NUMBER;
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('Pedidos Activos');
    
    FOR v_worker IN (SELECT DNI, nombre FROM trabajadores) LOOP
    
        v_result := fn_active_orders(v_worker.DNI);
        
        IF v_result > 0 THEN
        
            DBMS_OUTPUT.PUT_LINE(v_worker.nombre || ': ' || v_result || ' pedidos activos');
            
        END IF;
    END LOOP;
END;
/

-- FUNCIONES 2 - Devolver si un trabajador puede ser dado de baja 

CREATE OR REPLACE FUNCTION fn_can_deactivate (p_dni IN TRABAJADORES.DNI%TYPE) 
RETURN VARCHAR2 AS

    v_rol VARCHAR2(20);
    v_active NUMBER;
    
BEGIN

    SELECT rol INTO v_rol FROM trabajadores WHERE DNI = p_dni;

    IF v_rol = 'Gerente' THEN
    
        RETURN 'No se puede dar de baja, es Gerente';
        
    END IF;

    SELECT COUNT(*) INTO v_active FROM pedidos
    WHERE DNI_trabajador = p_dni AND estado IN ('Pendiente','Enviado');

    IF v_active > 0 THEN
    
        RETURN 'No se puede dar de baja, tiene ' || v_active || ' pedidos activos';
        
    END IF;

    RETURN 'El trabajador seleccionado puede darse de baja';
    
EXCEPTION

    WHEN NO_DATA_FOUND THEN
    
        RETURN 'ERROR: Trabajador no encontrado';
        
    WHEN OTHERS THEN
        
        RETURN 'ERROR: ' || SQLERRM;
        
END fn_can_deactivate;
/

BEGIN

    DBMS_OUTPUT.PUT_LINE('Trabajadores que pueden darse de baja');
    
    FOR v_worker IN (SELECT DNI, nombre FROM trabajadores) LOOP
    
        DBMS_OUTPUT.PUT_LINE(v_worker.nombre || ': ' || fn_can_deactivate(v_worker.DNI));
        
    END LOOP;
END;
/

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
