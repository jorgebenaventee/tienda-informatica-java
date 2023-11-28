# TIENDA API FUNKOS

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

1. **models**: En él tenemos la clase `Suppliers`, esta clase es la que se encarga de crear la tabla en la base de datos, en ella tenemos todos los atributos de la entidad `Supplier`, estos tienen distintas
2. **dto**: Contamos con tres clases en este paquete; `SupplierCreateDto`, es una clase de tipo record, tiene atributos con validaciones y serán los que se van a recibir en el body de la peticición a la hora de crear un proveedor. Después está `SupplierResponseDto`, no tiene validaciones porque no se recibe en el body, la usaremos cuando se haga un get de un proveedor. Finalmente la clase `SupplierUpdateDto`, usada con el mismo fin que `SupplierCreateDto`, pero en su caso será a la hora de actualizar un proveedor, sí tiene validaciones.
3. **mapper**: Tendremos la clase `SupplierMapper` encargada de mapear entre objetos DTO y entidades Supplier, tiene 4 métodos;

* *public Supplier toSupplier(SupplierCreateDto supplierCreateDto)*. Convierte un objeto SupplierCreateDto a una entidad Supplier.
* *public Supplier toSupplier(SupplierUpdateDto supplierUpdateDto, Supplier supplier)*. Convierte un objeto SupplierUpdateDto y una entidad Supplier a una nueva entidad Supplier actualizada
* *public Supplier toSupplier(SupplierResponseDto supplierResponseDto)*. Convierte un objeto SupplierResponseDto a una entidad Supplier.
* *public SupplierResponseDto toSupplierDto(Supplier supplier)*. Convierte una entidad Supplier a un objeto SupplierResponseDto.

4. **exceptions**: Encargado de manejar las distintas excepciones, tenemos `SupplierException` que extiende de `RuntimeException`, `SupplierBadRequest` del tipo *BAD_REQUEST*, y `SupplierNotFound` del tipo *NOT_FOUND*.
5. **repositories**: En el que tenemos la interfaz `SupplierRepository` que extiende de `JpaRepository` siendo su clave `Supplier` y su valor el ID de tipo `UUID`, y también extiende de `JpaSpecificationExecutor` para poder hacer consultas con filtros y paginación.
6. **services**: Tenemos la clase `SupplierServiceImpl` que implementa a la interfaz `SupplierService`, tenemos 8 métodos;

* *findAll()*. Obtiene todos los proveedores con opciones de filtrado y paginación.
* *findByUUID()*. Obtiene un proveedor por su UUID.
* *findByName()*. Obtiene un proveedor por su nombre.
* *getUUID()*. Comprueba si la cadena tiene un formato válido de UUID, de lo contrario, se lanza una excepción.
* *save()*. Guarda un nuevo proveedor.
* *update()*. Actualiza un proveedor ya existente.
* *deleteByUUID()*. Elimina un proveedor por su UUID.
* *sendNotification()*. Envía una notificación a través de WebSocket sobre la operación realizada en el proveedor.
* *setWebSocketService()*. Configura el servicio WebSocket para pruebas unitarias.

7. **controllers**: Gestiona las operaciones relacionadas con los proveedores, contamos con 7 métodos;

* *getAll()*. Recupera todos los proveedores con opciones de filtrado y paginación.
* *getSupplierByUUID()*. Obtiene un proveedor por su UUID.
* *createSupplier()*. Crea un nuevo proveedor.
* *updateSupplier()*. Actualiza un proveedor por su UUID.
* *updateSupplierPatch()*. Actualiza parcialmente un proveedor ya existente.
* *deleteSupplier()*. Elimina un proveedor por su UUID.
* *handleValidationExceptions()*. Este método lo usamos para manejar las excepciones de validación.
