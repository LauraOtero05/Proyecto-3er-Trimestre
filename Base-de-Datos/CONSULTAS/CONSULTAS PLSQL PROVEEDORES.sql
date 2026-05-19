SET SERVEROUTPUT ON;
-- #region --------CREACION, MODIFICACIÓN Y ELIMINACIÓN DE REGISTROS--------

--CREATE

-- Usamos una coleccion (varray) para cargar 5 proveedores mediante un buble

DECLARE

    TYPE t_registro_proveedor IS RECORD (
        nombre            proveedores.nombre%TYPE,
        direccion         proveedores.direccion%TYPE,
        id_codigo_postal  proveedores.id_codigo_postal%TYPE,
        telefono          VARCHAR2(20),
        email             VARCHAR2(150)
    );

    -- ----------------Creamos una colección (Varray) capaz de almacenar estos registros---------------------------
    TYPE t_lista_proveedores IS VARRAY(5) OF t_registro_proveedor;
    v_nuevos_prov t_lista_proveedores;
    
    v_id_generado NUMBER;
    
BEGIN

    v_nuevos_prov := t_lista_proveedores(
        t_registro_proveedor('Bizarro Records', 'Av. de la Música 40', 1, '600111222', 'info@bizarro.com'),
        t_registro_proveedor('PolyGram España', 'Calle del Ritmo 12', 2, '600333444', 'contacto@polygram.es'),
        t_registro_proveedor('Death Row Music', 'Sunset Boulevard 90', 10, '600555666', 'sales@deathrow.com'),
        t_registro_proveedor('Mute Records', 'Baker Street 221', 19, '600777888', 'demo@mute.co.uk'),
        t_registro_proveedor('Rough Trade', 'Camden High St 5', 19, '600999000', 'london@roughtrade.com')
    );

    DBMS_OUTPUT.PUT_LINE('Iniciando inserción de proveedores');

    FOR i IN 1..v_nuevos_prov.COUNT LOOP
        BEGIN
            
            INSERT INTO proveedores (nombre, direccion, id_codigo_postal)
            VALUES (v_nuevos_prov(i).nombre, v_nuevos_prov(i).direccion, v_nuevos_prov(i).id_codigo_postal)
            RETURNING id_proveedor INTO v_id_generado;

            -- Insertamos en la tabla hija (telefonos_proveedor)
            INSERT INTO telefonos_proveedor (id_proveedor, telefono)
            VALUES (v_id_generado, v_nuevos_prov(i).telefono);

            -- Insertamos en la tabla tabla hija (emails_proveedor)
            INSERT INTO emails_proveedor (id_proveedor, email)
            VALUES (v_id_generado, v_nuevos_prov(i).email);

            DBMS_OUTPUT.PUT_LINE('- Proveedor [' || v_id_generado || '] ' || v_nuevos_prov(i).nombre || ' insertado');
            
        EXCEPTION
            WHEN DUP_VAL_ON_INDEX THEN
                DBMS_OUTPUT.PUT_LINE('x Error: El proveedor ' || v_nuevos_prov(i).nombre || ' ya existe o sus datos de contacto están duplicados.');
        END;
    END LOOP;
    
    COMMIT;
END;
/

-- UPDATE

-- Identifica los proveedores que pertenecen a españa y les actualiza la dirección

DECLARE
    
    CURSOR c_prov_nacionales IS
        SELECT p.id_proveedor, p.nombre, p.direccion, pa.nombre AS nombre_pais
        FROM proveedores p
        JOIN codigos_postales cp ON p.id_codigo_postal = cp.id_codigo_postal
        JOIN paises pa ON cp.codigo_pais = pa.codigo_pais
        WHERE pa.codigo_pais = 'ES';
        
    v_registro c_prov_nacionales%ROWTYPE;
    v_contador NUMBER := 0;
    
BEGIN

    OPEN c_prov_nacionales;
    
    LOOP
        FETCH c_prov_nacionales INTO v_registro;
        EXIT WHEN c_prov_nacionales%NOTFOUND OR v_contador >= 10;

        v_contador := v_contador + 1;

        IF v_registro.direccion NOT LIKE '%(Zona UE)%' THEN -- El NOT LIKE es para decir que si "NO contiene" esa palabra en ninguna parte que entonces entre en el bucle
            
            UPDATE proveedores
            SET direccion = v_registro.direccion || ' - (Zona UE)',
                nombre = UPPER(v_registro.nombre)
            WHERE id_proveedor = v_registro.id_proveedor;
            
            DBMS_OUTPUT.PUT_LINE('Modificado ID: ' || v_registro.id_proveedor || ' con nombre: ' || v_registro.nombre || ' - País: ' || v_registro.nombre_pais);
        ELSE
            DBMS_OUTPUT.PUT_LINE(' ID ' || v_registro.id_proveedor || ' ya había sido modificado previamente');
        END IF;

    END LOOP;
    CLOSE c_prov_nacionales;

    DBMS_OUTPUT.PUT_LINE('Total de registros modificados: ' || v_contador);
    COMMIT;
END;
/

-- DELETE

-- Primero eliminamos de las tablas hijas y luego de la tabla padre para que no haya errores

DECLARE

    CURSOR c_prov_a_eliminar IS
        SELECT id_proveedor, nombre 
        FROM proveedores 
        WHERE nombre IN ('Bizarro Records', 'PolyGram España', 'Death Row Music', 'Mute Records', 'Rough Trade', 'BIZARRO RECORDS', 'POLYGRAM ESPAÑA', 'DEATH ROW MUSIC', 'MUTE RECORDS', 'ROUGH TRADE');

    v_hijos_telefonos NUMBER;
    v_hijos_emails    NUMBER;
    
BEGIN

    FOR r_prov IN c_prov_a_eliminar LOOP
        
        SELECT COUNT(*) INTO v_hijos_telefonos FROM telefonos_proveedor WHERE id_proveedor = r_prov.id_proveedor;
        
        IF v_hijos_telefonos > 0 THEN
        
            DELETE 
                FROM telefonos_proveedor 
                WHERE id_proveedor = r_prov.id_proveedor;
            
        END IF;

        SELECT COUNT(*) INTO v_hijos_emails FROM emails_proveedor WHERE id_proveedor = r_prov.id_proveedor;
        
        IF v_hijos_emails > 0 THEN
        
            DELETE 
                FROM emails_proveedor 
                WHERE id_proveedor = r_prov.id_proveedor;
                
        END IF;


        DELETE FROM proveedores WHERE id_proveedor = r_prov.id_proveedor;
        
        DBMS_OUTPUT.PUT_LINE('Eliminado: ' || r_prov.nombre || ' con ID: ' || r_prov.id_proveedor );
        
    END LOOP;

    COMMIT;
EXCEPTION

    WHEN OTHERS THEN
    
        ROLLBACK;
        
        DBMS_OUTPUT.PUT_LINE('Error en el bloque de eliminación: ' || SQLERRM);
END;
/

-- #endregion

-- #region --------CONSULTAS CON CURSORES--------

-- Listado de proveedores en orden alfabético

BEGIN
    
    FOR r_prov IN (SELECT id_proveedor, nombre, direccion 
                   FROM proveedores 
                   ORDER BY nombre ASC) 
    LOOP
        DBMS_OUTPUT.PUT_LINE(r_prov.id_proveedor || ' | ' || UPPER(r_prov.nombre) || ' - Dir: ' || r_prov.direccion);
    END LOOP;
END;
/

-- Filtramos por los proveedores de una provincia

DECLARE
    
    CURSOR c_prov_sudogwon IS
        SELECT p.nombre, p.direccion 
        FROM proveedores p
        JOIN codigos_postales cp ON p.id_codigo_postal = cp.id_codigo_postal
        WHERE SUBSTR(cp.codigo_postal, 1, 2) = '04'; -- Extrae desde la posición 1, un largo de 2 caracteres
        
    v_nombre    proveedores.nombre%TYPE;
    v_direccion proveedores.direccion%TYPE;
    
BEGIN
    
    OPEN c_prov_sudogwon;
    LOOP
        
        FETCH c_prov_sudogwon INTO v_nombre, v_direccion;
        
        EXIT WHEN c_prov_sudogwon%NOTFOUND;
        
        DBMS_OUTPUT.PUT_LINE(v_nombre || ' - Dirección: ' || v_direccion);
        
    END LOOP;
    
    DBMS_OUTPUT.PUT_LINE('Total de proveedores encontrados en Sudogwon: ' || c_prov_sudogwon%ROWCOUNT);
    
    CLOSE c_prov_sudogwon;
    
END;
/

-- Proveedores con mucho stock

DECLARE
    
    CURSOR c_prov_por_stock (p_stock_minimo NUMBER) IS
        SELECT DISTINCT p.id_proveedor, p.nombre, p.direccion
        FROM proveedores p
        JOIN albumes a ON p.id_proveedor = a.id_proveedor
        WHERE a.stock >= p_stock_minimo;
        
    v_fila_prov c_prov_por_stock%ROWTYPE;
    
BEGIN
    
    OPEN c_prov_por_stock(100);
    
    LOOP
    
        FETCH c_prov_por_stock INTO v_fila_prov;
        
        EXIT WHEN c_prov_por_stock%NOTFOUND;
        
        DBMS_OUTPUT.PUT_LINE('Proveedor ID: ' || v_fila_prov.id_proveedor ||' | Nombre: ' || RPAD(v_fila_prov.nombre, 20) || ' | Dirección: ' || v_fila_prov.direccion);
        
    END LOOP;
    
    CLOSE c_prov_por_stock;
END;
/

-- Calculamos de cada proveedor cuantos albums tiene y cual es su precio medio

DECLARE
    
    CURSOR c_analisis_catalogo IS
        SELECT p.nombre AS proveedor,
               COUNT(a.id_album) AS total_albumes,
               NVL(AVG(a.precio), 0) AS precio_promedio
        FROM proveedores p
        LEFT JOIN albumes a ON p.id_proveedor = a.id_proveedor
        GROUP BY p.nombre
        ORDER BY total_albumes DESC;
BEGIN
    
    FOR r_analisis IN c_analisis_catalogo LOOP
        -- El RPAD es un padding right
        -- El LPAD es un paddin left
        -- El '99.99' es para formatear los números a ese formato
        DBMS_OUTPUT.PUT_LINE('Proveedor: ' || RPAD(r_analisis.proveedor, 22) || ' Álbumes: ' || LPAD(r_analisis.total_albumes, 3) ||'   Precio Medio: ' || TO_CHAR(ROUND(r_analisis.precio_promedio, 2), '99.99') || '€');    
    
    END LOOP;
END;
/

-- Clasificamos a los proveedores por sus ventas

DECLARE
    
    CURSOR c_impacto_ventas IS
        SELECT p.id_proveedor, 
               p.nombre AS proveedor,
               (SELECT NVL(SUM(dp.cantidad), 0) 
                FROM detalles_pedido dp
                JOIN albumes a ON dp.id_album = a.id_album
                WHERE a.id_proveedor = p.id_proveedor) AS unidades_vendidas
        FROM proveedores p
        ORDER BY unidades_vendidas DESC;
        
    v_clasificacion VARCHAR2(30);
BEGIN
    
    FOR r_prov IN c_impacto_ventas LOOP
        
        CASE 
            WHEN r_prov.unidades_vendidas >= 2 THEN 
                v_clasificacion := 'SUPERVENTAS';
            WHEN r_prov.unidades_vendidas = 1 THEN 
                v_clasificacion := 'PROVEEDOR ACTIVO';
            ELSE 
                v_clasificacion := 'SIN VENTAS REGISTRADAS';
        END CASE;
        
        DBMS_OUTPUT.PUT_LINE('ID: ' || r_prov.id_proveedor || ' | ' || RPAD(r_prov.proveedor, 20, ' ') || 
                             ' | Uds Vendidas: ' || r_prov.unidades_vendidas || 
                             ' | Rango: ' || v_clasificacion);
    END LOOP;
END;
/

-- #endregion

-- #region --------PROCEDIMIENTOS Y FUNCIONES--------

-- Inserta un proveedor validando si el código postal existe.

CREATE OR REPLACE PROCEDURE pr_insertar_proveedor_seguro(
    p_nombre VARCHAR2,
    p_direccion VARCHAR2,
    p_id_cp NUMBER
) AS
    v_existe_cp NUMBER := 0;
    e_cp_invalido EXCEPTION;
BEGIN
    
    SELECT COUNT(*) INTO v_existe_cp 
    FROM codigos_postales 
    WHERE id_codigo_postal = p_id_cp;
    
    IF v_existe_cp = 0 THEN
        RAISE e_cp_invalido;
    END IF;
    
    INSERT INTO proveedores (nombre, direccion, id_codigo_postal)
    VALUES (p_nombre, p_direccion, p_id_cp);
    
    DBMS_OUTPUT.PUT_LINE('Proveedor "' || p_nombre || '" añadido correctamente.');
    
EXCEPTION

    WHEN e_cp_invalido THEN
        DBMS_OUTPUT.PUT_LINE('Error: El ID de código postal ' || p_id_cp || ' no existe en la base de datos.');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error inesperado: ' || SQLERRM);
END;
/

-- Ejecutar el insertar un proveedor validando si el código postal existe.

BEGIN
    pr_insertar_proveedor_seguro('Discográfica Neptuno', 'Calle Falsa 123', 1);
    pr_insertar_proveedor_seguro('Discográfica Invalida', 'Avenida Ilegal', 999); -- Debe saltar la excepción controlada
END;
/

-- Muestra en pantalla todos los teléfonos y emails de un proveedor específico

CREATE OR REPLACE PROCEDURE pr_reporte_datos_proveedor(p_id_proveedor NUMBER) AS
    v_nombre_prov proveedores.nombre%TYPE;
    
    CURSOR c_telefonos IS 
        SELECT telefono FROM telefonos_proveedor WHERE id_proveedor = p_id_proveedor;
        
    CURSOR c_emails IS 
        SELECT email FROM emails_proveedor WHERE id_proveedor = p_id_proveedor;
BEGIN
    
    SELECT nombre INTO v_nombre_prov FROM proveedores WHERE id_proveedor = p_id_proveedor;
    
    DBMS_OUTPUT.PUT_LINE('Datos de contacto de: ' || UPPER(v_nombre_prov));
    
    FOR t IN c_telefonos LOOP
        DBMS_OUTPUT.PUT_LINE('Telefono: ' || t.telefono);
    END LOOP;
    
    FOR e IN c_emails LOOP
        DBMS_OUTPUT.PUT_LINE('Correo electronico: ' || e.email);
    END LOOP;
    
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Error: No se encontró ningún proveedor con el ID ' || p_id_proveedor);
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error en reporte: ' || SQLERRM);
END;
/

-- Ejecutar teléfonos y emails de un proveedor específico

BEGIN
    pr_reporte_datos_proveedor(1);
    DBMS_OUTPUT.PUT_LINE(chr(10)); -- Salto de línea
END;
/

-- Funcion para calcular cuánto dinero le hemos comprado en total a un proveedor sumando el coste de sus álbumes vendidos en los detalles de pedido.

CREATE OR REPLACE FUNCTION fn_calcular_gasto_proveedor(p_id_prov NUMBER) 

RETURN NUMBER IS

    v_total NUMBER := 0;
    v_existe NUMBER := 0;
    e_no_proveedor EXCEPTION;
    
BEGIN

    SELECT COUNT(*) INTO v_existe FROM proveedores WHERE id_proveedor = p_id_prov;
    
    IF v_existe = 0 THEN
        RAISE e_no_proveedor;
    END IF;

    SELECT NVL(SUM(dp.cantidad * al.precio), 0)
    INTO v_total
    FROM detalles_pedido dp
    JOIN albumes al ON dp.id_album = al.id_album
    WHERE al.id_proveedor = p_id_prov;
    
    RETURN ROUND(v_total, 2);
    
EXCEPTION

    WHEN e_no_proveedor THEN
        DBMS_OUTPUT.PUT_LINE('Error: El proveedor con ID ' || p_id_prov || ' no existe.');
        RETURN -1;
        
    WHEN OTHERS THEN
        RETURN 0;
        
END;
/

-- 3. Ejecutar la funcion para saber cuanto le hemos comprado a Universal Music
BEGIN
    DBMS_OUTPUT.PUT_LINE('Gasto Total Proveedor 4: ' || fn_calcular_gasto_proveedor(4) || '€');
    DBMS_OUTPUT.PUT_LINE(chr(10));
END;
/

-- Funcion para analizar los álbumes de un proveedor y devolver qué formato es el que más stock acumulado tiene.

CREATE OR REPLACE FUNCTION fn_proveedor_formato_top(p_id_prov NUMBER) 

RETURN VARCHAR2 IS
    v_formato_top VARCHAR2(50) := 'Sin Stock / Álbumes';
    
    CURSOR c_formatos IS
        SELECT formato, SUM(stock) as stock_total
        FROM albumes
        WHERE id_proveedor = p_id_prov
        GROUP BY formato
        ORDER BY stock_total DESC;
BEGIN

    FOR reg IN c_formatos LOOP
        IF reg.stock_total > 0 THEN
            v_formato_top := reg.formato || ' (Total: ' || reg.stock_total || ' uds)';
            EXIT;
        END IF;
    END LOOP;
    
    RETURN v_formato_top;
    
EXCEPTION

    WHEN OTHERS THEN
        RETURN 'Error al calcular';
        
END;
/

-- 4. Funcion para analizar los álbumes de un proveedor y devolver qué formato es el que más stock acumulado tiene.
BEGIN
    DBMS_OUTPUT.PUT_LINE('Formato estrella Proveedor 1: ' || fn_proveedor_formato_top(1));
END;
/

-- #endregion
