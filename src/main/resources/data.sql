INSERT INTO categories (uuid, name)
VALUES ('d69cf3db-b77d-4181-b3cd-5ca8107fb6a9', 'DEPORTES'),
       ('6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154', 'COMIDA'),
       ('9def16db-362b-44c4-9fc9-77117758b5b0', 'BEBIDA'),
       ('8c5c06ba-49d6-46b6-85cc-8246c0f362bc', 'COMPLEMENTOS'),
       ('bb51d00d-13fb-4b09-acc9-948185636f79', 'OTROS');

INSERT INTO proveedores(id_Proveedor, name, contact, address, date_Of_Hire, category_id)
VALUES ('f47a2544-5b87-49c7-8931-1b9d5cfbdf01', 'Proveedor 1', 1, 'Direccion 1', CURRENT_TIMESTAMP(), 'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9'),
         ('f47a2544-5b87-49c7-8931-1b9d5cfbdf02', 'Proveedor 2', 2, 'Direccion 2', CURRENT_TIMESTAMP(), '8c5c06ba-49d6-46b6-85cc-8246c0f362bc');

INSERT INTO PRODUCTS (id, NAME, WEIGHT, PRICE, IMG, STOCK, DESCRIPTION, CATEGORY_ID)
VALUES ('d69cf3db-b77d-4181-b3cd-5ca8107fb6a0', 'Producto A', 1, 100, 'hola jaja img', 10, 'Descripción del producto A', '9def16db-362b-44c4-9fc9-77117758b5b0');


