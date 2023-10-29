package dev.clownsinformatics.tiendajava.proveedores.repositories;

import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProveedorRepositoryImpl implements ProveedorRepository {

    HashMap<UUID, Proveedor> proveedores = new LinkedHashMap<>();

    public ProveedorRepositoryImpl() {
        for (int i = 0; i < 20; i++) {
            UUID id = UUID.randomUUID();
            proveedores.put(id, Proveedor.builder()
                    .idProveedor(id)
                    .nombre("Proveedor " + i)
                    .contacto(i)
                    .direccion("Calle " + i)
                    .build());
        }
    }


    @Override
    public List<Proveedor> getAll() {
        return proveedores.values().stream().toList();
    }

    @Override
    public Optional<Proveedor> getByUUID(UUID idProveedor) {
        return proveedores.values().stream()
                .filter(proveedor -> proveedor.getIdProveedor().equals(idProveedor)).findFirst();
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
    public Proveedor save(Proveedor proveedor) {
        return proveedores.put(proveedor.getIdProveedor(), proveedor);

    }

    @Override
    public Proveedor update(Proveedor proveedor) {
        return proveedores.put(proveedor.getIdProveedor(), proveedor);
    }

    @Override
    public void deleteByUUID(UUID idProveedor) {
        proveedores.remove(idProveedor);
    }

    @Override
    public UUID generateUUID() {
        return UUID.randomUUID();
    }
}
