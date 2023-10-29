package dev.clownsinformatics.tiendajava.proveedores.repositories;

import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProveedorRepositoryImpl implements ProveedorRepository {

    HashMap<Long, Proveedor> proveedores = new LinkedHashMap<>();
    public final AtomicLong idProveedores = new AtomicLong(1);

    public ProveedorRepositoryImpl() {
        proveedores.put(idProveedores.get(), Proveedor.builder()
                .idEmpresa(UUID.randomUUID())
                .idProveedor(idProveedores.get())
                .nombre("Proveedor 1")
                .contacto(1)
                .direccion("Calle 1")
                .build());
        proveedores.put(idProveedores.get(), Proveedor.builder()
                .idEmpresa(UUID.randomUUID())
                .idProveedor(idProveedores.get())
                .nombre("Proveedor 2")
                .contacto(2)
                .direccion("Calle 2")
                .build());
        proveedores.put(idProveedores.get(), Proveedor.builder()
                .idEmpresa(UUID.randomUUID())
                .idProveedor(idProveedores.get())
                .nombre("Proveedor 3")
                .contacto(3)
                .direccion("Calle 3")
                .build());
    }

    public Long incrementId() {
        return idProveedores.getAndIncrement();
    }

    @Override
    public List<Proveedor> getAll() {
        return proveedores.values().stream().toList();
    }

    @Override
    public Optional<Proveedor> getById(Long idProveedor) {
        return Optional.ofNullable(proveedores.get(idProveedor));
    }

    @Override
    public List<Proveedor> getByNombre(String nombre) {
        return proveedores.values().stream()
                .filter(proveedor -> proveedor.getNombre().equals(nombre)).toList();
    }

    @Override
    public List<Proveedor> getByDireccion(String direccion) {
        return proveedores.values().stream()
                .filter(proveedor -> proveedor.getDireccion().equals(direccion)).toList();
    }

    @Override
    public List<Proveedor> getByNombreAndDireccion(String nombre, String direccion) {
        return proveedores.values().stream()
                .filter(proveedor -> proveedor.getNombre()
                        .equals(nombre) && proveedor.getDireccion().equals(direccion)).toList();
    }

    @Override
    public Optional<Proveedor> getByUUID(UUID idEmpresa) {
        return proveedores.values().stream()
                .filter(proveedor -> proveedor.getIdEmpresa().equals(idEmpresa)).findFirst();
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        return proveedores.put(proveedor.getIdProveedor(), proveedor);

    }

    @Override
    public Proveedor update(Proveedor proveedor) {
        return proveedores.put(proveedor.getIdProveedor(), proveedor);
    }

    @Override
    public void deleteById(Long idProveedor) {
        proveedores.remove(idProveedor);
    }

}
