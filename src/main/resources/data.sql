INSERT INTO categories (uuid, name, created_at, updated_at)
VALUES ('d69cf3db-b77d-4181-b3cd-5ca8107fb6a9', 'SOBREMESA', current_timestamp, current_timestamp),
       ('6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154', 'PORTATILES', current_timestamp, current_timestamp),
       ('9def16db-362b-44c4-9fc9-77117758b5b0', 'RATONES', current_timestamp, current_timestamp),
       ('8c5c06ba-49d6-46b6-85cc-8246c0f362bc', 'PLACAS BASE', current_timestamp, current_timestamp),
       ('bb51d00d-13fb-4b09-acc9-948185636f79', 'OTROS', current_timestamp, current_timestamp);

INSERT INTO supplier(id, name, contact, address, date_Of_Hire, category_id)
VALUES ('f47a2544-5b87-49c7-8931-1b9d5cfbdf01', 'Proveedor 1', 1, 'Direccion 1', current_timestamp,
        'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9'),
       ('f47a2544-5b87-49c7-8931-1b9d5cfbdf02', 'Proveedor 2', 2, 'Direccion 2', current_timestamp,
        '8c5c06ba-49d6-46b6-85cc-8246c0f362bc'),
       ('f47a2544-5b87-49c7-8931-1b9d5cfbdf03', 'Proveedor 3', 3, 'Direccion 3', current_timestamp,
        '9def16db-362b-44c4-9fc9-77117758b5b0'),
       ('f47a2544-5b87-49c7-8931-1b9d5cfbdf04', 'Proveedor 4', 4, 'Direccion 4', current_timestamp,
        '6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154');

INSERT INTO PRODUCTS (id, NAME, WEIGHT, PRICE, IMG, STOCK, DESCRIPTION, CATEGORY_ID, created_at, updated_at)
VALUES ('d69cf3db-b77d-4181-b3cd-5ca8107fb6a0', 'Producto A', 1, 100, 'productA.jpg', 10, 'Descripción del producto A',
        'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9', current_timestamp, current_timestamp),
       ('76549b87-23a2-4065-8a86-914207290329', 'Producto B', 2, 150, 'productB.jpg', 15, 'Descripción del producto B',
        '6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154', current_timestamp, current_timestamp),
       ('3512e012-7028-405c-8397-b39886006212', 'Producto C', 3, 200, 'productC.jpg', 20, 'Descripción del producto C',
        '9def16db-362b-44c4-9fc9-77117758b5b0', current_timestamp, current_timestamp),
       ('98765432-1234-5678-90ab-cdef01234567', 'Producto D', 4, 250, 'productD.jpg', 25, 'Descripción del producto D',
        '8c5c06ba-49d6-46b6-85cc-8246c0f362bc', current_timestamp, current_timestamp);

INSERT INTO employee (salary, name, position, created_at, updated_at)
values (1000, 'Paco Pancetas', 'Técnico de sistemas', current_timestamp, current_timestamp),
       (2000, 'Pedro Sánchez', 'Desarrollador web', current_timestamp, current_timestamp),
       (3000, 'Mariano Rajoy', 'Administrador de base de datos', current_timestamp, current_timestamp),
       (4000, 'Belén Esteban', 'Directora de Marketing', current_timestamp, current_timestamp);

INSERT INTO CLIENTS (USERNAME, NAME, BALANCE, EMAIL, ADDRESS, PHONE, BIRTHDATE, IMAGE, IS_DELETED, CREATED_AT,
                     UPDATED_AT)
VALUES ('cliente1', 'Cliente 1', 0, 'hola@gmail.com', 'Direccion 1', 123456789, CURRENT_TIMESTAMP, null, false,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('cliente2', 'Cliente 2', 0, 'adios@gmail.com', 'Direccion 2', 987654321, CURRENT_TIMESTAMP, null, false,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into USERS (name, last_name, username, email, password)
values ('Admin', 'Admin Admin', 'admin', 'admin@prueba.net',
        '$2a$10$vPaqZvZkz6jhb7U7k/V/v.5vprfNdOnh4sxi/qpPRkYTzPmFlI9p2');

insert into USER_ROLES (user_id, roles)
values (1, 'USER');
insert into USER_ROLES (user_id, roles)
values (1, 'ADMIN');

-- Contraseña: User1
insert into USERS (name, last_name, username, email, password)
values ('User', 'User User', 'user', 'user@prueba.net',
        '$2a$12$RUq2ScW1Kiizu5K4gKoK4OTz80.DWaruhdyfi2lZCB.KeuXTBh0S.');
insert into USER_ROLES (user_id, roles)
values (2, 'USER');

-- Contraseña: Test1
insert into USERS (name, last_name, username, email, password)
values ('Test', 'Test Test', 'test', 'test@prueba.net',
        '$2a$10$Pd1yyq2NowcsDf4Cpf/ZXObYFkcycswqHAqBndE1wWJvYwRxlb.Pu');
insert into USER_ROLES (user_id, roles)
values (2, 'USER');

-- Contraseña: Otro1
insert into USERS (name, last_name, username, email, password)
values ('otro', 'Otro Otro', 'otro', 'otro@prueba.net',
        '$2a$12$3Q4.UZbvBMBEvIwwjGEjae/zrIr6S50NusUlBcCNmBd2382eyU0bS');
insert into USER_ROLES (user_id, roles)
values (3, 'USER');
