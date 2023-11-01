INSERT INTO CATEGORIES (name)
VALUES ('Categoría A');

INSERT INTO PRODUCTS (id, NAME, WEIGHT, PRICE, IMG, STOCK, DESCRIPTION, CATEGORY_ID)
VALUES (random_uuid(), 'Producto A', 1, 100, 'hola jaja img', 10, 'Descripción del producto A', random_uuid());


