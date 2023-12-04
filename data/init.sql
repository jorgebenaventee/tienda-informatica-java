
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;
--
-- Name: tienda-informatica; Type: DATABASE; Schema: -; Owner: postgres
--



ALTER DATABASE "tienda-informatica" OWNER TO postgres;

\connect -reuse-previous=on "dbname='tienda-informatica'"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categories (
    uuid uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    is_deleted boolean DEFAULT false,
    name character varying(50) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL
);


ALTER TABLE public.categories OWNER TO postgres;

--
-- Name: clients; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.clients (
    id bigint NOT NULL,
    address character varying(255),
    balance double precision,
    birthdate date,
    created_at timestamp(6) without time zone NOT NULL,
    email character varying(255),
    image character varying(255),
    is_deleted boolean DEFAULT false,
    name character varying(255),
    phone character varying(255),
    updated_at timestamp(6) without time zone NOT NULL,
    username character varying(255)
);


ALTER TABLE public.clients OWNER TO postgres;

--
-- Name: clients_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.clients_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.clients_id_seq OWNER TO postgres;

--
-- Name: clients_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.clients_id_seq OWNED BY public.clients.id;


--
-- Name: employee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee (
    id integer NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    name character varying(50),
    "position" character varying(50),
    salary double precision,
    updated_at timestamp(6) without time zone NOT NULL
);


ALTER TABLE public.employee OWNER TO postgres;

--
-- Name: employee_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.employee_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.employee_id_seq OWNER TO postgres;

--
-- Name: employee_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.employee_id_seq OWNED BY public.employee.id;


--
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    description character varying(255),
    img character varying(255) NOT NULL,
    is_deleted boolean DEFAULT false,
    name character varying(50),
    price double precision NOT NULL,
    stock integer NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    weight double precision,
    category_id uuid,
    supplier_id uuid,
    CONSTRAINT products_price_check CHECK ((price >= (0)::double precision)),
    CONSTRAINT products_stock_check CHECK ((stock >= 0)),
    CONSTRAINT products_weight_check CHECK ((weight >= (0)::double precision))
);


ALTER TABLE public.products OWNER TO postgres;

--
-- Name: supplier; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.supplier (
    id uuid NOT NULL,
    address character varying(255),
    contact integer,
    date_of_hire timestamp(6) without time zone,
    is_deleted boolean DEFAULT false,
    name character varying(255),
    category_id uuid NOT NULL,
    CONSTRAINT supplier_contact_check CHECK ((contact >= 1))
);


ALTER TABLE public.supplier OWNER TO postgres;

--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_roles (
    user_id bigint NOT NULL,
    roles character varying(255),
    CONSTRAINT user_roles_roles_check CHECK (((roles)::text = ANY ((ARRAY['USER'::character varying, 'ROLE_USER'::character varying, 'ADMIN'::character varying])::text[])))
);


ALTER TABLE public.user_roles OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    email character varying(255) NOT NULL,
    is_deleted boolean DEFAULT false,
    last_name character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    username character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: clients id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clients ALTER COLUMN id SET DEFAULT nextval('public.clients_id_seq'::regclass);


--
-- Name: employee id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee ALTER COLUMN id SET DEFAULT nextval('public.employee_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.categories (uuid, created_at, is_deleted, name, updated_at) FROM stdin;
d69cf3db-b77d-4181-b3cd-5ca8107fb6a9	2023-12-02 11:06:32.6235	f	SOBREMESA	2023-12-02 11:06:32.6235
6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154	2023-12-02 11:06:32.6235	f	PORTATILES	2023-12-02 11:06:32.6235
9def16db-362b-44c4-9fc9-77117758b5b0	2023-12-02 11:06:32.6235	f	RATONES	2023-12-02 11:06:32.6235
8c5c06ba-49d6-46b6-85cc-8246c0f362bc	2023-12-02 11:06:32.6235	f	PLACAS BASE	2023-12-02 11:06:32.6235
bb51d00d-13fb-4b09-acc9-948185636f79	2023-12-02 11:06:32.6235	f	OTROS	2023-12-02 11:06:32.6235
\.


--
-- Data for Name: clients; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.clients (id, address, balance, birthdate, created_at, email, image, is_deleted, name, phone, updated_at, username) FROM stdin;
1	Direccion 1	0	2023-12-02	2023-12-02 11:06:32.644625	hola@gmail.com	\N	f	Cliente 1	123456789	2023-12-02 11:06:32.644625	cliente1
2	Direccion 2	0	2023-12-02	2023-12-02 11:06:32.644625	adios@gmail.com	\N	f	Cliente 2	987654321	2023-12-02 11:06:32.644625	cliente2
\.


--
-- Data for Name: employee; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.employee (id, created_at, name, "position", salary, updated_at) FROM stdin;
1	2023-12-02 11:06:32.640475	Paco Pancetas	Técnico de sistemas	1000	2023-12-02 11:06:32.640475
2	2023-12-02 11:06:32.640475	Pedro Sánchez	Desarrollador web	2000	2023-12-02 11:06:32.640475
3	2023-12-02 11:06:32.640475	Mariano Rajoy	Administrador de base de datos	3000	2023-12-02 11:06:32.640475
4	2023-12-02 11:06:32.640475	Belén Esteban	Directora de Marketing	4000	2023-12-02 11:06:32.640475
\.


--
-- Data for Name: supplier; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.supplier (id, address, contact, date_of_hire, is_deleted, name, category_id) FROM stdin;
f47a2544-5b87-49c7-8931-1b9d5cfbdf01	Direccion 1	1	2023-12-02 11:06:32.630909	f	Proveedor 1	d69cf3db-b77d-4181-b3cd-5ca8107fb6a9
f47a2544-5b87-49c7-8931-1b9d5cfbdf02	Direccion 2	2	2023-12-02 11:06:32.630909	f	Proveedor 2	8c5c06ba-49d6-46b6-85cc-8246c0f362bc
f47a2544-5b87-49c7-8931-1b9d5cfbdf03	Direccion 3	3	2023-12-02 11:06:32.630909	f	Proveedor 3	9def16db-362b-44c4-9fc9-77117758b5b0
f47a2544-5b87-49c7-8931-1b9d5cfbdf04	Direccion 4	4	2023-12-02 11:06:32.630909	f	Proveedor 4	6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.products (id, created_at, description, img, is_deleted, name, price, stock, updated_at, weight, category_id, supplier_id) VALUES ('d69cf3db-b77d-4181-b3cd-5ca8107fb6a0', '2023-12-02 11:06:32.635943', 'Descripción del producto A', 'productA.jpg', false, 'Producto A', 100, 10, '2023-12-02 11:06:32.635943', 1, 'd69cf3db-b77d-4181-b3cd-5ca8107fb6a9', 'f47a2544-5b87-49c7-8931-1b9d5cfbdf01');
INSERT INTO public.products (id, created_at, description, img, is_deleted, name, price, stock, updated_at, weight, category_id, supplier_id) VALUES ('98765432-1234-5678-90ab-cdef01234567', '2023-12-02 11:06:32.635943', 'Descripción del producto D', 'productD.jpg', false, 'Producto D', 250, 25, '2023-12-02 11:06:32.635943', 4, '8c5c06ba-49d6-46b6-85cc-8246c0f362bc', 'f47a2544-5b87-49c7-8931-1b9d5cfbdf01');
INSERT INTO public.products (id, created_at, description, img, is_deleted, name, price, stock, updated_at, weight, category_id, supplier_id) VALUES ('3512e012-7028-405c-8397-b39886006212', '2023-12-02 11:06:32.635943', 'Descripción del producto C', 'productC.jpg', false, 'Producto C', 200, 20, '2023-12-02 11:06:32.635943', 3, '9def16db-362b-44c4-9fc9-77117758b5b0', 'f47a2544-5b87-49c7-8931-1b9d5cfbdf01');
INSERT INTO public.products (id, created_at, description, img, is_deleted, name, price, stock, updated_at, weight, category_id, supplier_id) VALUES ('76549b87-23a2-4065-8a86-914207290329', '2023-12-02 11:06:32.635943', 'Descripción del producto B', 'https://localhost:3000/api/storage/1701700973738_fondo_de_pantalla.jpg', false, 'Producto B', 150, 15, '2023-12-02 11:06:32.635943', 2, '6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154', 'f47a2544-5b87-49c7-8931-1b9d5cfbdf01');
--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_roles (user_id, roles) FROM stdin;
1	USER
1	ADMIN
2	USER
2	USER
3	USER
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, created_at, email, is_deleted, last_name, name, password, updated_at, username) FROM stdin;
1	2023-12-02 11:06:32.649906	admin@prueba.net	f	Admin Admin	Admin	$2a$10$vPaqZvZkz6jhb7U7k/V/v.5vprfNdOnh4sxi/qpPRkYTzPmFlI9p2	2023-12-02 11:06:32.649906	admin
2	2023-12-02 11:06:32.661427	user@prueba.net	f	User User	User	$2a$12$RUq2ScW1Kiizu5K4gKoK4OTz80.DWaruhdyfi2lZCB.KeuXTBh0S.	2023-12-02 11:06:32.661427	user
3	2023-12-02 11:06:32.666161	test@prueba.net	f	Test Test	Test	$2a$10$Pd1yyq2NowcsDf4Cpf/ZXObYFkcycswqHAqBndE1wWJvYwRxlb.Pu	2023-12-02 11:06:32.666161	test
4	2023-12-02 11:06:32.670836	otro@prueba.net	f	Otro Otro	otro	$2a$12$3Q4.UZbvBMBEvIwwjGEjae/zrIr6S50NusUlBcCNmBd2382eyU0bS	2023-12-02 11:06:32.670836	otro
\.


--
-- Name: clients_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.clients_id_seq', 2, true);


--
-- Name: employee_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.employee_id_seq', 4, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 4, true);


--
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (uuid);


--
-- Name: clients clients_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (id);


--
-- Name: employee employee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (id);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: supplier supplier_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.supplier
    ADD CONSTRAINT supplier_pkey PRIMARY KEY (id);


--
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: users uk_r43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- Name: categories uk_t8o6pivur7nn124jehx7cygw5; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT uk_t8o6pivur7nn124jehx7cygw5 UNIQUE (name);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: user_roles fkhfh9dx7w3ubf1co1vdev94g3f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: supplier fkjte73m0n7lqfatf79ff8od05b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.supplier
    ADD CONSTRAINT fkjte73m0n7lqfatf79ff8od05b FOREIGN KEY (category_id) REFERENCES public.categories(uuid);


--
-- Name: products fkkxyc9lj0tpsrm6jpmf32jbub2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fkkxyc9lj0tpsrm6jpmf32jbub2 FOREIGN KEY (supplier_id) REFERENCES public.supplier(id);


--
-- Name: products fkog2rp4qthbtt2lfyhfo32lsw9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT fkog2rp4qthbtt2lfyhfo32lsw9 FOREIGN KEY (category_id) REFERENCES public.categories(uuid);


--
-- PostgreSQL database dump complete
--

