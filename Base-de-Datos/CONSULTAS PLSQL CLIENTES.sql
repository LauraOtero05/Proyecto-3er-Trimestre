-- ============================================================
-- IMPORTANTE: Ejecutar esta línea PRIMERO en tu sesión
-- ============================================================
SET SERVEROUTPUT ON
 


-- ============================================================
-- INSERTAR 5 CLIENTES NUEVOS
-- ============================================================

BEGIN
  INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash)
  VALUES ('Carlos', 'Muñoz', 'Calle Alpino', 1, 'carlos@gmail.com', 'hashA');

  INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash)
  VALUES ('Daniel', 'Garcia', 'Calle Goya', 2, 'danie@gmail.com', 'hashB');

  INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash)
  VALUES ('Sergio', 'Romero', 'Calle Lirios', 3, 'sergio@gmail.com', 'hashC');

  INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash)
  VALUES ('Maria', 'Ruiz', 'Calle Huertas', 4, 'maria@gmail.com', 'hashD');

  INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash)
  VALUES ('Pedro', 'Diaz', 'Calle Victoria ', 5, 'pedro@mail.com', 'hashE');

  COMMIT;
END;
/
-- ============================================================
-- MODIFICAR EMAIL DE 5 CLIENTES
-- ============================================================

BEGIN
  UPDATE clientes SET email = 'nuevo1@gmail.com' WHERE id_cliente = 1;
  UPDATE clientes SET email = 'nuevo2@gmail.com' WHERE id_cliente = 2;
  UPDATE clientes SET email = 'nuevo3@gmail.com' WHERE id_cliente = 3;
  UPDATE clientes SET email = 'nuevo4@gmail.com' WHERE id_cliente = 4;
  UPDATE clientes SET email = 'nuevo5@gmail.com' WHERE id_cliente = 5;
  
  
  COMMIT;
END;
/

-- ============================================================
-- ELIMINAR 5 CLIENTES
-- ============================================================
BEGIN

  DELETE FROM pedidos
  WHERE id_cliente IN (6,7,8,9,10);

  DELETE FROM clientes
  WHERE id_cliente IN (6,7,8,9,10);

  COMMIT;

END;
/
-- ============================================================
-- CURSOR 1
-- MOSTRAR CLIENTES ORDENADOS POR APELLIDO
-- ============================================================

DECLARE

    CURSOR c_apellidos IS
        SELECT id_cliente, nombre, apellido
        FROM clientes
        ORDER BY apellido;

BEGIN

    DBMS_OUTPUT.PUT_LINE('Clientes ordenados por apellido');

    FOR v_cliente IN c_apellidos LOOP

        DBMS_OUTPUT.PUT_LINE(
            v_cliente.id_cliente || ' Nombre: ' ||
            v_cliente.nombre || ' ' ||
            v_cliente.apellido
        );

    END LOOP;

END;
/
-- ============================================================
-- CURSOR 2
-- CANTIDAD DE CLIENTES POR CIUDAD
-- ============================================================


DECLARE

    CURSOR c_ciudades IS
        SELECT cp.ciudad, COUNT(*) AS total_clientes
        FROM clientes c
        INNER JOIN codigos_postales cp
        ON c.id_codigo_postal = cp.id_codigo_postal
        GROUP BY cp.ciudad
        ORDER BY total_clientes DESC;

    v_total NUMBER := 0;

BEGIN

    DBMS_OUTPUT.PUT_LINE('Cantidad de clientes por ciudad');

    FOR v_cliente IN c_ciudades LOOP

        v_total := v_total + v_cliente.total_clientes;

        DBMS_OUTPUT.PUT_LINE(
            v_cliente.ciudad || ': ' ||
            v_cliente.total_clientes || ' clientes'
        );

    END LOOP;

    DBMS_OUTPUT.PUT_LINE(
        'Clientes registrados: ' || v_total
    );

END;
/

-- ============================================================
-- CURSOR 3
-- CANTIDAD DE CLIENTES POR DOMINIO DE CORREO
-- ============================================================


DECLARE

    CURSOR c_dominios IS
        SELECT SUBSTR(email, INSTR(email,'@') + 1) AS dominio,
               COUNT(*) AS total
        FROM clientes
        GROUP BY SUBSTR(email, INSTR(email,'@') + 1)
        ORDER BY total DESC;

    v_total_global NUMBER := 0;

BEGIN

    DBMS_OUTPUT.PUT_LINE('Clientes por dominio de email');

    FOR v_cliente IN c_dominios LOOP

        v_total_global := v_total_global + v_cliente.total;

        DBMS_OUTPUT.PUT_LINE(
            v_cliente.dominio || ': ' ||
            v_cliente.total || ' clientes'
        );

    END LOOP;

    DBMS_OUTPUT.PUT_LINE(
        'Total de clientes: ' || v_total_global
    );

END;
/

-- ============================================================
-- CURSOR 4
-- CLIENTES QUE HAN HECHO PEDIDOS
-- ============================================================

DECLARE

    CURSOR c_pedidos IS
        SELECT DISTINCT c.id_cliente,
               c.nombre,
               c.apellido
        FROM clientes c
        INNER JOIN pedidos p
        ON c.id_cliente = p.id_cliente;

    v_contador NUMBER := 0;

BEGIN

    DBMS_OUTPUT.PUT_LINE('Clientes con pedidos realizados');

    FOR v_cliente IN c_pedidos LOOP

        v_contador := v_contador + 1;

        DBMS_OUTPUT.PUT_LINE(
            v_cliente.id_cliente || ' Nombre: ' ||
            v_cliente.nombre || ' ' ||
            v_cliente.apellido
        );

    END LOOP;

    DBMS_OUTPUT.PUT_LINE(
        'Total clientes con pedidos: ' || v_contador
    );

END;
/

-- ============================================================
-- CONSULTA 5
-- CLIENTES SEGUN CODIGO POSTAL
-- ============================================================


DECLARE

    CURSOR c_codigo_postal (p_cp NUMBER) IS
        SELECT nombre, apellido, direccion
        FROM clientes
        WHERE id_codigo_postal = p_cp;

    v_cp NUMBER := 2;

BEGIN

    DBMS_OUTPUT.PUT_LINE(
        'Clientes del codigo postal ' || v_cp
    );

    FOR v_cliente IN c_codigo_postal(v_cp) LOOP

        DBMS_OUTPUT.PUT_LINE(
            v_cliente.nombre || ' ' ||
            v_cliente.apellido || ' Direccion: ' ||
            v_cliente.direccion
        );

    END LOOP;

END;
/

-- ============================================================
-- PROCEDIMIENTO PARA INSERTAR CLIENTES
-- ============================================================


CREATE OR REPLACE PROCEDURE insertar_cliente (

    p_nombre clientes.nombre%TYPE,
    p_apellido clientes.apellido%TYPE,
    p_direccion clientes.direccion%TYPE,
    p_cp clientes.id_codigo_postal%TYPE,
    p_email clientes.email%TYPE,
    p_password clientes.password_hash%TYPE

) IS

    v_existe NUMBER := 0;

BEGIN

    SELECT COUNT(*)
    INTO v_existe
    FROM clientes
    WHERE email = p_email;

    IF v_existe > 0 THEN

        DBMS_OUTPUT.PUT_LINE(
            'El email ya existe'
        );

    ELSE

        INSERT INTO clientes (
            nombre,
            apellido,
            direccion,
            id_codigo_postal,
            email,
            password_hash
        )
        VALUES (
            p_nombre,
            p_apellido,
            p_direccion,
            p_cp,
            p_email,
            p_password
        );

        COMMIT;

        DBMS_OUTPUT.PUT_LINE(
            'Cliente insertado correctamente'
        );

    END IF;

EXCEPTION

    WHEN OTHERS THEN

        DBMS_OUTPUT.PUT_LINE(
            'Error: ' || SQLERRM
        );

END;
/

--  Consulta para ejecutar el procedimiento de insertar cliente
BEGIN
  insertar_cliente('Juan', 'Perez', 'Calle Sol', 1, 'juan@email.com', 'hash123');
END;
/

-- ============================================================
-- PROCEDIMIENTO PARA ACTUALIZAR EMAIL
-- ============================================================

CREATE OR REPLACE PROCEDURE actualizar_email (

    p_id clientes.id_cliente%TYPE,
    p_email clientes.email%TYPE

) IS

    v_existe NUMBER := 0;

BEGIN

    SELECT COUNT(*)
    INTO v_existe
    FROM clientes
    WHERE id_cliente = p_id;

    IF v_existe = 0 THEN

        DBMS_OUTPUT.PUT_LINE(
            'Cliente no encontrado'
        );

    ELSE

        UPDATE clientes
        SET email = p_email
        WHERE id_cliente = p_id;

        COMMIT;

        DBMS_OUTPUT.PUT_LINE(
            'Email actualizado'
        );

    END IF;

EXCEPTION

    WHEN OTHERS THEN

        DBMS_OUTPUT.PUT_LINE(
            'Error: ' || SQLERRM
        );

END;
/

-- Consulta para ejecutar el procedimiento de actualizar email
BEGIN
  actualizar_email(1, 'nuevo@email.com');
END;
/

-- ============================================================
-- FUNCION PARA OBTENER EL NOMBRE COMPLETO
-- ============================================================


CREATE OR REPLACE FUNCTION nombre_completo (

    p_id clientes.id_cliente%TYPE

) RETURN VARCHAR2 IS

    v_nombre_completo VARCHAR2(200);

BEGIN

    SELECT nombre || ' ' || apellido
    INTO v_nombre_completo
    FROM clientes
    WHERE id_cliente = p_id;

    RETURN v_nombre_completo;

EXCEPTION

    WHEN NO_DATA_FOUND THEN

        RETURN 'Cliente no encontrado';

END;
/

-- Consulta para probar la función del nombre completo
SELECT nombre_completo(3)
FROM dual;

-- ============================================================
-- FUNCION PARA CONTAR PEDIDOS DE UN CLIENTE
-- ============================================================

CREATE OR REPLACE FUNCTION total_pedidos_cliente (

    p_id clientes.id_cliente%TYPE

) RETURN NUMBER IS

    v_total NUMBER := 0;

BEGIN

    SELECT COUNT(*)
    INTO v_total
    FROM pedidos
    WHERE id_cliente = p_id;

    RETURN v_total;

EXCEPTION

    WHEN OTHERS THEN

        RETURN -1;

END;
/


-- Consulta para probar la función de total de pedidos por cliente
SELECT total_pedidos_cliente(5)
FROM dual;