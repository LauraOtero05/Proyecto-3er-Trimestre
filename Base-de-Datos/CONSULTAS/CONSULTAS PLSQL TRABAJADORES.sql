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
