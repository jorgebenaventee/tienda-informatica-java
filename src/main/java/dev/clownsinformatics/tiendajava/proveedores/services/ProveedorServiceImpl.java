package dev.clownsinformatics.tiendajava.proveedores.services;

import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorCreateDto;
import dev.clownsinformatics.tiendajava.proveedores.dto.ProveedorUpdateDto;
import dev.clownsinformatics.tiendajava.proveedores.exceptions.ProveedorBadRequest;
import dev.clownsinformatics.tiendajava.proveedores.exceptions.ProveedorNotFound;
import dev.clownsinformatics.tiendajava.proveedores.mapper.ProveedorMapper;
import dev.clownsinformatics.tiendajava.proveedores.models.Proveedor;
import dev.clownsinformatics.tiendajava.proveedores.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "proveedores")
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    @Autowired
    public ProveedorServiceImpl(ProveedorRepository proveedoresRepository, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedoresRepository;
        this.proveedorMapper = proveedorMapper;
    }

    @Override
    public List<Proveedor> findAll(String name, String address) {
        if ((name == null || name.isEmpty()) && (address == null || address.isEmpty())) {
            return proveedorRepository.findAll();
        }
        if ((name != null && !name.isEmpty()) && (address == null || address.isEmpty())) {
            return proveedorRepository.getByNameContainingIgnoreCase(name);
        }
        if (name == null || name.isEmpty()) {
            return proveedorRepository.getByAddressContainingIgnoreCase(address);
        }
        return proveedorRepository.getByNameAndAddressContainingIgnoreCase(name, address);
    }


    @Override
    @Cacheable(key = "#idProveedor")
    public Proveedor findByUUID(String idProveedor) {
        try {
            UUID uuid = UUID.fromString(idProveedor);
            return proveedorRepository.getByIdProveedor(uuid).orElseThrow(
                    () -> new ProveedorNotFound(uuid)
            );
        } catch (IllegalArgumentException e) {
            throw new ProveedorBadRequest(idProveedor + " no es un UUID válido");
        }
    }

    @Transactional
    @Override
    @CachePut(key = "#result.idProveedor")
    public Proveedor save(ProveedorCreateDto proveedorCreateDto) {
        return proveedorRepository.save(proveedorMapper.toProveedor(proveedorCreateDto, UUID.randomUUID()));
    }

    @Override
    @CachePut(key = "#idProveedor")
    public Proveedor update(ProveedorUpdateDto proveedorUpdateDto, String idProveedor) {
        return proveedorRepository.save(proveedorMapper.toProveedor(proveedorUpdateDto, findByUUID(idProveedor)));
    }

    @Transactional
    @Override
    @CacheEvict(key = "#idProveedor")
    public void deleteByUUID(String idProveedor) {
        findByUUID(idProveedor);
        proveedorRepository.deleteByIdProveedor(UUID.fromString(idProveedor));

    }
}
