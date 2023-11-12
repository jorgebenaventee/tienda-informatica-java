INSERT INTO categories (uuid, name)
VALUES ('d69cf3db-b77d-4181-b3cd-5ca8107fb6a9', 'SOBREMESA'),
       ('6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154', 'PORTATILES'),
       ('9def16db-362b-44c4-9fc9-77117758b5b0', 'RATONES'),
       ('8c5c06ba-49d6-46b6-85cc-8246c0f362bc', 'PLACAS BASE'),
       ('bb51d00d-13fb-4b09-acc9-948185636f79', 'OTROS');

INSERT INTO proveedores(id_Proveedor, name, contact, address, date_Of_Hire, category_id)
VALUES ('f47a2544-5b87-49c7-8931-1b9d5cfbdf01', 'Proveedor 1', 1, 'Direccion 1', CURRENT_TIMESTAMP(),
        'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9'),
       ('f47a2544-5b87-49c7-8931-1b9d5cfbdf02', 'Proveedor 2', 2, 'Direccion 2', CURRENT_TIMESTAMP(),
        '8c5c06ba-49d6-46b6-85cc-8246c0f362bc');

INSERT INTO PRODUCTS (id, NAME, WEIGHT, PRICE, IMG, STOCK, DESCRIPTION, CATEGORY_ID)
VALUES ('d69cf3db-b77d-4181-b3cd-5ca8107fb6a0', 'Producto A', 1, 100, 'productA.jpg', 10, 'Descripción del producto A',
        'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9'),
       ('76549b87-23a2-4065-8a86-914207290329', 'Producto B', 2, 150, 'productB.jpg', 15, 'Descripción del producto B',
        '6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154'),
       ('3512e012-7028-405c-8397-b39886006212', 'Producto C', 3, 200, 'productC.jpg', 20, 'Descripción del producto C',
        '9def16db-362b-44c4-9fc9-77117758b5b0'),
       ('98765432-1234-5678-90ab-cdef01234567', 'Producto D', 4, 250, 'productD.jpg', 25, 'Descripción del producto D',
        'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9');

INSERT INTO CLIENTS (USERNAME, NAME, BALANCE, EMAIL, ADDRESS, PHONE, BIRTHDATE, IMAGE, IS_DELETED)
VALUES ('cliente1', 'Cliente 1', 0, 'hola@gmail.com', 'Direccion 1', 123456789, CURRENT_TIMESTAMP(), null, 0),
       ('cliente2', 'Cliente 2', 0, 'adios@gmail.com' , 'Direccion 2', 987654321, CURRENT_TIMESTAMP(), null, 0);