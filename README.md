
# TIENDA API

## Autores

* Eva Gómez
* Jaime Medina
* David Jaraba
* Jorge Benavente

## Arquitectura seguida del código

...

## Auth

Esta carpeta contiene las implementaciones relacionadas con la autenticación y autorización mediante JWT y Spring Security en nuestro proyecto. Con ello  garantizamos un acceso seguro a las distintas partes del proyeto y protegemos las operaciones de nuestra aplicación.

Contamos con 5 paquetes:

En **dto** tenemos las clases; `JwtAuthResponse`, encargada de generar el token. `UserSignInRequest`, encargada de recibir los datos de inicio de sesión. Y `UserSignUpRequest`, encargada de recibir y validar los datos de registro de usuario.

Después tenemos el paquete **exceptions** encargado de manejar las distintas excepciones, tenemos `AuthException` que extiende de `RuntimeException`, `AuthSingInInvalid` del tipo *NOT_FOUND*, y la excepción `PasswordNotMatch` del tipo *BAD_REQUEST*.

En **repositories** nos encontramos con la interfaz `AuthUsersRepository`, que extiende de `JpaRepository` siendo su clave `Users` y su valor el ID de tipo `Long`.

En la carpeta **services** nos encontramos tres paquetes; **authentication** con la clase `AuthenticationServiceImpl` que implementa la interfaz `AuthenticationService`, en ella gestionamos los métodos de registro de un usuario (void signUp), y su inicio de sesión (void signIn). En el paquete **jwt** tenemos la clase `JwtServiceImpl` que implementa la interfaz `JwtService`, en la que generamos el token, lo validamos y extraemos el nombre del usuario. Finalmente en el paquete **users** tendríamos la clase `AuthUsersServiceImpl`, implementando la interfaz `AuthUsersService`, encargada de cargar los datos de un usuario.

Por último tendríamos el controlador en la carpeta **controllers**, esta clase se encarga de gestionar las operaciones relacionadas con la autenticación de usuarios.

## Suppliers

En este endpoint realizaremos los métodos relacionados con el proveedor, para gestionarlo ha sido dividido en 7 paquetes:

- **models**: En él tenemos la clase `Suppliers`, esta clase es la que se encarga de crear la tabla en la base de datos, en ella tenemos todos los atributos de la entidad `Supplier`, estos tienen distintas
- **dto**: Contamos con tres clases en este paquete; `SupplierCreateDto`, es una clase de tipo record, tiene atributos con validaciones y serán los que se van a recibir en el body de la peticición a la hora de crear un proveedor. Después está `SupplierResponseDto`, no tiene validaciones porque no se recibe en el body, la usaremos cuando se haga un get de un proveedor. Finalmente la clase `SupplierUpdateDto`, usada con el mismo fin que `SupplierCreateDto`, pero en su caso será a la hora de actualizar un proveedor, sí tiene validaciones.
- **mapper**: Tendremos la clase `SupplierMapper` encargada de mapear entre objetos DTO y entidades Supplier, tiene 4 métodos;

- *public Supplier toSupplier(SupplierCreateDto supplierCreateDto)* para convertir un objeto SupplierCreateDto a una entidad Supplier.
- *public Supplier toSupplier(SupplierUpdateDto supplierUpdateDto, Supplier supplier)* para convertir un objeto SupplierUpdateDto y una entidad Supplier a una nueva entidad Supplier actualizada
- *public Supplier toSupplier(SupplierResponseDto supplierResponseDto)* para convertir un objeto SupplierResponseDto a una entidad Supplier.
- *public SupplierResponseDto toSupplierDto(Supplier supplier)* para convertir una entidad Supplier a un objeto SupplierResponseDto.

- **exceptions**: Encargado de manejar las distintas excepciones, tenemos `SupplierException` que extiende de `RuntimeException`, `SupplierBadRequest` del tipo *BAD_REQUEST*, y `SupplierNotFound` del tipo *NOT_FOUND*.
- **repositories**: En el que tenemos la interfaz `SupplierRepository` que extiende de `JpaRepository` siendo su clave `Supplier` y su valor el ID de tipo `UUID`, y también extiende de `JpaSpecificationExecutor` para poder hacer consultas con filtros y paginación.
- **services**: Tenemos la clase `SupplierServiceImpl` que implementa a la interfaz `SupplierService`, tenemos 8 métodos;

## Product

En el endpoint `Product` contamos con distintos paquetes en el que se divide la información. Los productos cuentan con distintos atributos como Id, name, price, o su proveedor y categoría.

El repositorio de este endpoint extiende de **JpaRepository** y **JpaSpecificationExecutor**, para así poder usar todos sus métodos. Hemos añadido algunos personalizados como el deleteById, que en lugar de eliminar el producto nos permite cambiar su atributo isDeleted a true.

También contamos con `ProductService`, esta clase utiliza caché y llama a los métodos del `ProductRepository`, utilizando la clase `ProductMappers` y los DTO correspondiente. Cuando hay algún error de consulta o de búsqueda hacemos uso de excepciones personalizadas.

También contamos con `ProductRestController`, esta contiene métodos que nos devuelven ResponseEntity con el tipo que corresponda. Por ejemplo, el método `getAllProducts` nos devuelve un PageResponse de ProductResponseDto. La ruta de esta clase es `/api/products`, pero añadiendo campos como el id podemos llamar el método `getProductById`. Estos son todos los métodos que contiene esta clase:
1.  **Obtener todos los productos:**

    -   **Endpoint:** `GET /api/products`
    -   **Descripción:** Obtiene una página paginada de productos según los parámetros de consulta proporcionados, como nombre, peso máximo, precio máximo, stock mínimo, categoría, etc.
2.  **Obtener un producto por ID:**

    -   **Endpoint:** `GET /api/products/{id}`
    -   **Descripción:** Obtiene un producto específico según su identificador.
3.  **Crear un nuevo producto:**

    -   **Endpoint:** `POST /api/products`
    -   **Descripción:** Crea un nuevo producto utilizando la información proporcionada en el DTO de creación.
4.  **Actualizar un producto por ID:**

    -   **Endpoint:** `PUT /api/products/{id}`
    -   **Descripción:** Actualiza un producto existente utilizando la información proporcionada en el DTO de actualización.
5.  **Actualizar parcialmente un producto por ID:**

    -   **Endpoint:** `PATCH /api/products/{id}`
    -   **Descripción:** Actualiza parcialmente un producto existente utilizando la información proporcionada en el DTO de actualización.
6.  **Actualizar la imagen de un producto:**

    -   **Endpoint:** `PATCH /api/products/{id}/image`
    -   **Descripción:** Actualiza la imagen de un producto existente cargando un archivo de imagen.
7.  **Eliminar un producto por ID:**

    -   **Endpoint:** `DELETE /api/products/{id}`
    -   **Descripción:** Elimina un producto según su identificador.

## Category

El endpoint `Category` contiene campos como uuid, name, createdAt, updatedAt e isDeleted. Contamos con la clase `CategoryRepository` que de igual manera que `ProductRepository`, hereda de **JpaRepository** y **JpaSpecificationExecutor**. En esta tenemos varias consultas personalizas como *existsProductsById* o *existsSupplierById*.

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.category.uuid = :id")  
    Boolean existsProductById(UUID id);  
      
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Supplier p WHERE p.category.uuid = :id")  
    Boolean existsSupplierById(UUID id);  
      
    @Modifying(flushAutomatically = true, clearAutomatically = true)  
    @Query("update Category c set c.isDeleted = true where c.uuid = ?1")  
    void deleteById(UUID id);

En la clase `CategoryServiceImpl` llamamos a los métodos del repository y utilizamos la clase `CategoryMapper` y el DTO cuando sea necesario. A la hora de eliminar una categoria comprobamos si esta tiene proveedores o productos, en el caso de que sí los tenga mostraremos un mensaje de advertencia y haremos un borrado lógico usando el campo *isDeleted*.
En el controlador, de igual manera que en el resto de controladores REST, devolveremos ResponseEntity con la entidad que corresponda, estos son los métodos:
1.  **Obtener todas las categorías:**

    -   **Endpoint:** `GET /api/categories`
    -   **Descripción:** Obtiene una página paginada de categorías, con la opción de filtrar por nombre y estado de eliminación.
2.  **Obtener una categoría por ID:**

    -   **Endpoint:** `GET /api/categories/{id}`
    -   **Descripción:** Obtiene una categoría específica según su identificador UUID.
3.  **Crear una nueva categoría:**

    -   **Endpoint:** `POST /api/categories`
    -   **Descripción:** Crea una nueva categoría utilizando la información proporcionada en el DTO de respuesta de categoría.
4.  **Actualizar una categoría por ID:**

    -   **Endpoint:** `PUT /api/categories/{id}`
    -   **Descripción:** Actualiza una categoría existente utilizando la información proporcionada en el DTO de respuesta de categoría.
5.  **Eliminar una categoría por ID:**

    -   **Endpoint:** `DELETE /api/categories/{id}`
    -   **Descripción:** Elimina una categoría según su identificador UUID.

## Orders

En el endpoint de `Orders` tenemos dos modelos, Orders, que cuenta con campos como el id del usuario, el cliente, el total y la linea de pedidos, que esta a su vez tiene cantidad, el id del producto, el precio y el total.
La clase `OrderRepository` extiende de **MongoRepository**, ya que este endpoint está hecho con Mongo en lugar de Postgree. En esta clase tenemos métodos como:

    Page<Order> findByIdUser(Long idUser, Pageable pageable);  
    List<Order> findOrderIdsByIdUser(Long idUser);  
    boolean existsByIdUser(Long idUser);
En la clase `OrderServiceImpl` llamamos a los métodos del repository, pero tambien hemos añadido 3 métodos nuevos, como son:

**reserveStockOrder**
Este método se encarga de reservar el stock para un pedido. Itera sobre las líneas de pedido, actualiza el stock de cada producto correspondiente y calcula el total del pedido. Retorna el pedido actualizado.

**checkOrder**
Este método verifica la validez de un pedido, asegurándose de que contenga elementos, de que los productos asociados tengan suficiente stock y precios coincidentes. Lanza excepciones específicas en caso de problemas.

**returnStockOrders**
Este método devuelve el stock de los productos asociados a un pedido. Itera sobre las líneas de pedido y actualiza el stock de cada producto correspondiente. Retorna el pedido actualizado con el stock devuelto.

Éstos los usamos en los métodos para añadir, modificar o eliminar pedidos.

En la clase `OrderRestController` contamos con los siguientes métodos:

1.  **Obtener todos los pedidos:**

    -   **Endpoint:** `GET /api/pedidos`
    -   **Descripción:** Obtiene una página paginada de pedidos con enlaces de paginación. Permite filtrar por número de página, tamaño, campo de orden y dirección.
2.  **Obtener un pedido por ID:**

    -   **Endpoint:** `GET /api/pedidos/{id}`
    -   **Descripción:** Obtiene un pedido específico según su identificador UUID.
3.  **Obtener pedidos por ID de usuario:**

    -   **Endpoint:** `GET /api/pedidos/user/{id}`
    -   **Descripción:** Obtiene una página paginada de pedidos asociados a un usuario, con enlaces de paginación. Permite filtrar por número de página, tamaño, campo de orden y dirección.
4.  **Crear un nuevo pedido:**

    -   **Endpoint:** `POST /api/pedidos`
    -   **Descripción:** Crea un nuevo pedido utilizando la información proporcionada en el cuerpo de la solicitud.
5.  **Actualizar un pedido por ID:**

    -   **Endpoint:** `PUT /api/pedidos/{id}`
    -   **Descripción:** Actualiza un pedido existente según su identificador UUID, utilizando la información proporcionada en el cuerpo de la solicitud.
6.  **Eliminar un pedido por ID:**

    -   **Endpoint:** `DELETE /api/pedidos/{id}`
    -   **Descripción:** Elimina un pedido según su identificador UUID.
7.  **Manejar excepciones de validación:**

    -   **Descripción:** Maneja excepciones de validación y devuelve un mapa de errores con nombres de campo y mensajes asociados.

## Storage

En el Storage tenemos una clase de configuración, llamada `StorageConfig`, esta tiene el método *deleteAll*, que nos permite coger del archivo **application.properties** la propiedad "upload.delete", y nos permite seleccionar si queremos borrar todas las imagenes al iniciar la aplicacion o no.
Tambien tenemos la interfaz `StorageService` que la implementaremos en la clase FileSystemStorageService.
El servicio `FileSystemStorageService` se encarga de gestionar el almacenamiento de archivos en el sistema de archivos local. A continuación, se presenta un resumen de sus métodos:

1.  **`init()`**
    -   Inicializa el servicio de almacenamiento creando el directorio raíz definido en la configuración.

2.  **`store(MultipartFile file)`**
    -   Almacena un archivo en el sistema de archivos local.
    -   Genera un nombre único para el archivo basado en la marca de tiempo y evita nombres de archivo duplicados.
    -   Realiza validaciones para asegurarse de que el archivo no esté vacío y no contenga rutas relativas peligrosas.

3.  **`loadAll()`**
    -   Carga todos los archivos almacenados en el directorio raíz.

4.  **`load(String filename)`**
    -   Carga un archivo específico según el nombre de archivo proporcionado.

5.  **`loadAsResource(String filename)`**
    -   Carga un archivo como recurso Spring `Resource`, permitiendo su manipulación.

6.  **`delete(String filename)`**
    -   Elimina un archivo específico según el nombre de archivo proporcionado.

7.  **`deleteAll()`**
    -   Elimina todos los archivos almacenados en el directorio raíz.

8.  **`getUrl(String filename)`**
    -   Obtiene la URL para acceder a un archivo mediante el controlador `StorageController` utilizando la marca de tiempo del archivo.

Utilizamos excepciones específicas (`StorageBadRequest`, `StorageConflict`, `StorageNotFound`) para gestionar situaciones inesperadas y proporcionar mensajes descriptivos en caso de errores.

En la carpeta de controllers tenemos un controlador llamado `FileExceptionsAdvice`, este sirve para capturar errores de otros controladores y mostrar el mensaje de error sin que la aplicación falle. En nuestro caso lo usamos para mostrar error cuando el archivo que introducimos para actualizar la imagen de un producto es demasiado grande.
También tenemos un controlador REST, este tiene los siguientes métodos:
1.  **`serveFile(@PathVariable String filename, HttpServletRequest request)`**

    -   **Endpoint:** `GET /api/storage/{filename:.+}`
    -   **Descripción:** Devuelve un archivo almacenado según el nombre de archivo proporcionado en la solicitud.
    -   **Funcionalidad adicional:** Determina y establece el tipo de contenido del archivo.
2.  **`uploadFile(@RequestPart("file") MultipartFile file)`**

    -   **Endpoint:** `POST /api/storage`
    -   **Descripción:** Sube un archivo al sistema de archivos y devuelve la URL para acceder a él.
    -   **Funcionalidad adicional:** Maneja la subida de archivos multipart y devuelve la URL del archivo almacenado.