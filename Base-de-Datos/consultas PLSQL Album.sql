SET SERVEROUTPUT ON;
-- #region --------CREACION, MODIFICACIÓN Y ELIMINACIÓN DE REGISTROS--------

-- INSERT
BEGIN

    INSERT INTO Albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero)
    VALUES ('Future Nostalgia', 'Dua Lipa', 'Vinilo', 31.50, 40, 4, 5);
    
    INSERT INTO Albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero)
    VALUES ('Soda Stereo', 'Soda Stereo', 'CD', 15.99, 20, 5, 2);

    INSERT INTO Albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero)
    VALUES ('El Mal Querer', 'Rosalia', 'Vinilo', 29.99, 15, 3, 10);

    INSERT INTO Albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero)
    VALUES ('Proof', 'BTS', 'CD', 55.00, 100, 1, 1);

    INSERT INTO Albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero)
    VALUES ('Discovery', 'Daft Punk', 'CD', 19.99, 60, 6, 8);

    COMMIT;
END;
/

-- UPDATE

BEGIN
    UPDATE Albumes SET 
        precio = precio * 1.15 
        WHERE formato = 'Vinilo';

    UPDATE Albumes SET 
        stock = stock - 10 
        WHERE titulo = 'Wake Up';

    UPDATE Albumes SET 
        precio = 9.99 
        WHERE stock > 150;

    UPDATE Albumes SET 
        id_proveedor = 2 
        WHERE artista = 'Quevedo';

    UPDATE Albumes SET 
        stock = stock + 100
        WHERE artista = 'BTS';
    
    COMMIT;
END;
/

-- DELETE

BEGIN

    -- 1. ELIMINAR POR STOCK = 0
    
    DELETE FROM detalles_pedido 
    WHERE id_album IN (SELECT id_album FROM albumes WHERE stock = 0);

    DELETE FROM albumes 
    WHERE stock = 0;

    -- 2. ELIMINAR POR TÍTULO = 'Thriller'

    DELETE FROM detalles_pedido 
    WHERE id_album IN (SELECT id_album FROM albumes WHERE titulo = 'Thriller');

    DELETE FROM albumes 
    WHERE titulo = 'Thriller';

    -- 3. ELIMINAR POR ARTISTA Y FORMATO 

    DELETE FROM detalles_pedido 
    WHERE id_album IN (SELECT id_album FROM albumes WHERE artista = 'Norah Jones' AND formato = 'Vinilo');

    DELETE FROM albumes 
    WHERE artista = 'Norah Jones' AND formato = 'Vinilo';

    -- 4. ELIMINAR POR GÉNERO = 3

    DELETE FROM detalles_pedido 
    WHERE id_album IN (SELECT id_album FROM albumes WHERE id_genero = 3);

    DELETE FROM albumes 
    WHERE id_genero = 3;
    
    -- 5. ELIMINAR POR PRECIO > 50.00

    DELETE FROM detalles_pedido 
    WHERE id_album IN (SELECT id_album FROM albumes WHERE precio > 50.00);

    DELETE FROM albumes 
    WHERE precio > 50.00;

    -- COMMIT;
    
END;
/

-- #endregion


-- #region --------CONSULTAS CON CURSORES--------

-- Recorre todos los álbumes para calcular el valor total del stock en almacén.

DECLARE

    CURSOR c_inventario IS 
        SELECT titulo, artista, (precio * stock) AS valor_total 
        FROM Albumes;
    
    v_titulo Albumes.titulo%TYPE;
    v_artista Albumes.artista%TYPE;
    v_valor NUMBER;

BEGIN

    OPEN c_inventario;

    LOOP
        FETCH c_inventario INTO v_titulo, v_artista, v_valor;

        EXIT WHEN c_inventario%NOTFOUND;
        
        DBMS_OUTPUT.PUT_LINE('Disco: ' || v_titulo || ' - Artista: ' || v_artista || ' - Valor en Almacén: ' || v_valor || '€');
    
    END LOOP;

    CLOSE c_inventario;
END;
/

-- A través de un ID_GENERO mostramos solo los álbumes que pertenecen a esa categoría

DECLARE

    CURSOR c_genero (p_id_genero NUMBER) IS
        SELECT titulo, artista, precio 
        FROM Albumes
        WHERE id_genero = p_id_genero;

BEGIN

    DBMS_OUTPUT.PUT_LINE('--- Listado de Álbumes por género ---');

    FOR reg IN c_genero(1) LOOP
    
        DBMS_OUTPUT.PUT_LINE('Álbum: ' || reg.titulo || ' - Artista: ' || reg.artista || ' - Precio: ' || reg.precio);
    
    END LOOP;
END;
/

-- Buscamos los discos con un precio superior a 25€ y devolvemos los que cumplen esa condición

DECLARE

    CURSOR c_caros IS 
        SELECT titulo, precio FROM Albumes WHERE precio > 25;

    v_reg c_caros%ROWTYPE;

BEGIN

    OPEN c_caros;

    DBMS_OUTPUT.PUT_LINE('Buscando ediciones especiales (>25€)');

    LOOP

        FETCH c_caros INTO v_reg;

        EXIT WHEN c_caros%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Item ' || c_caros%ROWCOUNT || ': ' || v_reg.titulo ||  v_reg.precio || '€');

    END LOOP;
    
    IF c_caros%ROWCOUNT = 0 THEN

        DBMS_OUTPUT.PUT_LINE('No se encontraron discos que superen ese precio.');

    END IF;

    CLOSE c_caros;
END;
/

-- Alerta de Stock Bajo

BEGIN
    DBMS_OUTPUT.PUT_LINE('ALERTA DE REPOSICIÓN (Stock < 60 unidades):');

    FOR r_stock IN (SELECT titulo, artista, stock FROM Albumes WHERE stock < 60) LOOP

        DBMS_OUTPUT.PUT_LINE('¡ATENCIÓN! -> ' || r_stock.titulo || ' de ' || r_stock.artista || ' tiene solo ' || r_stock.stock || ' unidades');

    END LOOP;
END;
/

-- Relación de distribución de los albumes con sus proveedores

DECLARE

    CURSOR c_logistica IS
        SELECT a.titulo, a.formato, p.nombre AS proveedor
        FROM Albumes a
        INNER JOIN Proveedores p ON a.id_proveedor = p.id_proveedor
        ORDER BY p.nombre;

BEGIN

    DBMS_OUTPUT.PUT_LINE('Relación de Distribución de Álbumes:');

    FOR item IN c_logistica LOOP

        DBMS_OUTPUT.PUT_LINE('Proveedor: ' || item.proveedor || ' | Formato: ' || item.formato || ' | Título: ' || item.titulo);
    
    END LOOP;
END;
/

-- #endregion


-- #region --------PROCEDIMIENTOS Y FUNCIONES--------

-- Calcular el valor del inventario por genero

CREATE OR REPLACE FUNCTION fn_valor_inventario_genero(p_id_genero NUMBER) 

RETURN NUMBER IS
    
    v_total NUMBER := 0;
    v_encontrado BOOLEAN := FALSE;

    CURSOR c_productos IS 
        SELECT precio, stock 
        FROM Albumes 
        WHERE id_genero = p_id_genero;
    
    e_genero_vacio EXCEPTION;

BEGIN

    FOR reg IN c_productos LOOP

        v_encontrado := TRUE;

        IF reg.stock > 0 THEN

            v_total := v_total + (NVL(reg.precio, 0) * reg.stock);

        END IF;
    END LOOP;

    IF NOT v_encontrado THEN
        RAISE e_genero_vacio;
    END IF;

    RETURN ROUND(v_total, 2);

EXCEPTION

    WHEN e_genero_vacio THEN

        DBMS_OUTPUT.PUT_LINE('Error: El género ' || p_id_genero || ' no tiene álbumes.');

        RETURN 0;

    WHEN OTHERS THEN

        DBMS_OUTPUT.PUT_LINE('Error inesperado en la función.');

        RETURN -1;
END;
/

-- Ejecutar la función de Calcular el valor del inventario por genero
    SELECT fn_valor_inventario_genero(1) FROM DUAL;


-- Clasificar Popularidad de Artista basado en su stock total

CREATE OR REPLACE FUNCTION fn_estatus_artista(p_nombre_artista VARCHAR2) 

RETURN VARCHAR2 IS

    v_stock_total NUMBER;
    v_resultado VARCHAR2(50);

BEGIN

    SELECT SUM(stock) INTO v_stock_total 
    FROM Albumes 
    WHERE UPPER(artista) = UPPER(p_nombre_artista);

    CASE 

        WHEN v_stock_total IS NULL THEN v_resultado := 'No registrado';
        WHEN v_stock_total > 150 THEN v_resultado := 'Súper Ventas (Alto Stock)';
        WHEN v_stock_total BETWEEN 50 AND 150 THEN v_resultado := 'Promedio';
        ELSE v_resultado := 'Edición Limitada';

    END CASE;

    RETURN v_resultado;

EXCEPTION

    WHEN NO_DATA_FOUND THEN

        RETURN 'Artista no encontrado';

    WHEN OTHERS THEN

        RETURN 'Error de consulta';
END;
/

-- Ejecutar la función de Estatus de Rosalia
SELECT fn_estatus_artista('Rosalia') FROM DUAL;


-- Aplicar descuento por formato a grandes stock

CREATE OR REPLACE PROCEDURE pr_descuento_por_formato(

    p_formato VARCHAR2, 
    p_porcentaje NUMBER

) IS
    
    CURSOR c_albumes(p_fmt VARCHAR2) IS
        SELECT id_album, titulo, precio, stock 
        FROM Albumes 
        WHERE UPPER(formato) = UPPER(p_fmt);
    
    v_nuevo_precio NUMBER;
    v_contador NUMBER := 0;

BEGIN

    FOR reg IN c_albumes(p_formato) LOOP
        
        IF reg.stock > 100 THEN

            v_nuevo_precio := TRUNC(reg.precio * (1 - (p_porcentaje/100)), 2);
            
            UPDATE Albumes 
            SET precio = v_nuevo_precio 
            WHERE id_album = reg.id_album;
            
            v_contador := v_contador + 1;
            DBMS_OUTPUT.PUT_LINE('Descuento aplicado a: ' || reg.titulo);

        END IF;
    END LOOP;

    IF v_contador = 0 THEN

        DBMS_OUTPUT.PUT_LINE('No se encontraron álbumes con sobre-stock para este formato.');

    ELSE

        DBMS_OUTPUT.PUT_LINE('Proceso terminado. Registros afectados: ' || v_contador);

    END IF;
    
    COMMIT;

EXCEPTION
    WHEN OTHERS THEN

        ROLLBACK;

        DBMS_OUTPUT.PUT_LINE('Error en la transacción de descuento.');
END;
/

-- Ejecutamos un descuento del 15% a los CD con mucho stock
EXECUTE pr_descuento_por_formato('CD', 15);


-- Incrementa el stock de los álbumes de un proveedor específico que estén por debajo de un umbral.

CREATE OR REPLACE PROCEDURE pr_reponer_stock_proveedor(

    p_id_prov NUMBER, 
    p_cantidad_sumar NUMBER

) IS

    CURSOR c_stock_bajo IS 
        SELECT id_album, titulo, stock 
        FROM Albumes 
        WHERE id_proveedor = p_id_prov AND stock < 50;
        
    v_reg_album c_stock_bajo%ROWTYPE;

BEGIN

    OPEN c_stock_bajo;

    FETCH c_stock_bajo INTO v_reg_album;
    
    WHILE c_stock_bajo%FOUND LOOP
        
        UPDATE Albumes 
        SET stock = stock + p_cantidad_sumar 
        WHERE id_album = v_reg_album.id_album;
        
        DBMS_OUTPUT.PUT_LINE('Reponiendo: ' || v_reg_album.titulo);
        
        FETCH c_stock_bajo INTO v_reg_album;

    END LOOP;

    DBMS_OUTPUT.PUT_LINE('Total de modelos de álbumes repuestos: ' || c_stock_bajo%ROWCOUNT);
    
    CLOSE c_stock_bajo;
    COMMIT;

EXCEPTION

    WHEN INVALID_CURSOR THEN

        DBMS_OUTPUT.PUT_LINE('Error: El cursor no es válido.');

    WHEN OTHERS THEN

        IF c_stock_bajo%ISOPEN THEN CLOSE c_stock_bajo; END IF;

        DBMS_OUTPUT.PUT_LINE('Error crítico en la reposición.');
END;
/


-- Probar a sumar 20 unidades al proveedor 3 si hay poco stock
EXECUTE pr_reponer_stock_proveedor(3, 20);


