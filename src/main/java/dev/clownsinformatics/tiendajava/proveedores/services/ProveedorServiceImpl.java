package dev.clownsinformatics.tiendajava.proveedores.services;

import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.proveedores.exceptions.ProveedorBadRequest;
import dev.clownsinformatics.tiendajava.proveedores.mapper.ProveedorMapper;
import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.proveedores.exceptions.ProveedorNotFound;
import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;
import dev.clownsinformatics.tiendajava.proveedores.repositories.ProveedorRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "proveedores")
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepositoryImpl proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    @Autowired
    public ProveedorServiceImpl(ProveedorRepositoryImpl proveedoresRepository, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedoresRepository;
        this.proveedorMapper = proveedorMapper;
    }

    @Override
    public List<Proveedor> findAll(String nombre, String direccion) {
        if ((nombre == null || nombre.isEmpty()) && (direccion == null || direccion.isEmpty())) {
            return proveedorRepository.getAll();
        }
        if ((nombre != null && !nombre.isEmpty()) && (direccion == null || direccion.isEmpty())) {
            return proveedorRepository.getByNombre(nombre);
        }
        if (nombre == null || nombre.isEmpty()) {
            return proveedorRepository.getByDireccion(direccion);
        }
        return proveedorRepository.getByNombreAndDireccion(nombre, direccion);
    }


    @Override
    @Cacheable(key = "#idProveedor")
    public Proveedor findByUUID(String idProveedor) {
        try {
            UUID uuid = UUID.fromString(idProveedor);
            return proveedorRepository.getByUUID(uuid).orElseThrow(
                    () -> new ProveedorNotFound(uuid)
            );
        } catch (IllegalArgumentException e) {
            throw new ProveedorBadRequest(idProveedor + " no es un UUID válido");
        }
    }

    @Override
    @CachePut(key = "#result.idProveedor")
    public Proveedor save(ProveedorCreateDto proveedorCreateDto) {
        return proveedorRepository.save(proveedorMapper.toProveedor(proveedorCreateDto, proveedorRepository.generateUUID()));
    }

    @Override
    @CachePut(key = "#idProveedor")
    public Proveedor update(ProveedorUpdateDto proveedorUpdateDto, String idProveedor) {
        return proveedorRepository.update(proveedorMapper.toProveedor(proveedorUpdateDto, findByUUID(idProveedor)));
    }

    @Override
    @CacheEvict(key = "#idProveedor")
    public void deleteByUUID(String idProveedor) {
        findByUUID(idProveedor);
        proveedorRepository.deleteByUUID(UUID.fromString(idProveedor));

    }
}
