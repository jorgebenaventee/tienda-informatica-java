package dev.clownsinformatics.tiendajava.proveedores.services;

import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorCreateDto;
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
    @Cacheable(key = "#id")
    public Proveedor findById(Long idProveedor) {
        return proveedorRepository.getById(idProveedor).orElseThrow(
                () -> new ProveedorNotFound(idProveedor)
        );
    }

    @Override
    @Cacheable(key = "#idEmpresa")
    public Proveedor findByUUID(String idEmpresa) {
        var uuid = UUID.fromString(idEmpresa);
        return proveedorRepository.getByUUID(uuid).orElseThrow(
                () -> new ProveedorNotFound(uuid)

                );
    }

    @Override
    @CachePut(key = "#result.id")
    public Proveedor save(ProveedorCreateDto proveedorCreateDto) {
        return proveedorRepository.save(proveedorMapper.toProveedor(proveedorCreateDto, proveedorRepository.incrementId()));
    }

    @Override
    @CachePut(key = "#id")
    public Proveedor update(ProveedorUpdateDto proveedorUpdateDto, Long idProveedor) {
        return proveedorRepository.update(proveedorMapper.toProveedor(proveedorUpdateDto, proveedorRepository.getById(idProveedor).orElseThrow(
                () -> new ProveedorNotFound(idProveedor)
        )));
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Long idProveedor) {
        proveedorRepository.deleteById(idProveedor);

    }
}
