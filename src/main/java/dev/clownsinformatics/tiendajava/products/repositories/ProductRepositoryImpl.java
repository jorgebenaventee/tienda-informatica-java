package dev.clownsinformatics.tiendajava.products.repositories;

import dev.clownsinformatics.tiendajava.products.models.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    HashMap<UUID, Product> products = new LinkedHashMap<>();

    public ProductRepositoryImpl() {
        for (int i = 1; i <= 50; i++) {
            UUID uuid = UUID.randomUUID();
            products.put(uuid, Product.builder()
                    .id(uuid)
                    .name("Product " + i)
                    .weight(2.5 + i)
                    .idCategory(UUID.randomUUID())
                    .price(50.0 + i)
                    .img("imagen" + i + ".jpg")
                    .stock(10 + i)
                    .description("Descripción del producto " + i)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build());
        }
    }

    @Override
    public List<Product> findAll() {
        log.info("Buscando todos los productos");
        return products.values().stream().toList();
    }

    @Override
    public List<Product> findAllByWeight(Double weight) {
        log.info("Buscando todos los productos de la categoría {}", weight);
        return products.values().stream()
                .filter(product -> product.getWeight().equals(weight)).toList();
    }

    @Override
    public List<Product> findAllByName(String name) {
        log.info("Buscando todos los productos con el nombre {}", name);
        return products.values().stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase())).toList();
    }

    @Override
    public List<Product> findAllByNameAndWeight(String name, Double weight) {
        log.info("Buscando todos los productos con el nombre {} y la categoría {}", name, weight);
        return products.values().stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()) &&
                        product.getWeight().equals(weight)).toList();
    }

    @Override
    public Optional<Product> findById(UUID id) {
        log.info("Buscando el producto con el id {}", id);
        return products.get(id) != null ? Optional.of(products.get(id)) : Optional.empty();
    }

    @Override
    public Optional<Product> findByIdCategory(UUID idCategory) {
        log.info("Buscando el producto con el id de categoría {}", idCategory);
        return products.values().stream()
                .filter(product -> product.getIdCategory().equals(idCategory)).findFirst();
    }

    @Override
    public Product save(Product product) {
        log.info("Guardando el producto {}", product);
        return products.put(product.getId(), product);
    }

    @Override
    public void deleteById(UUID id) {
        log.info("Eliminando el producto con el id {}", id);
        products.remove(id);
    }

    @Override
    public void deleteByIdCategory(UUID idCategory) {
        log.info("Eliminando el producto con el id de categoría {}", idCategory);
        products.values().removeIf(product -> product.getIdCategory().equals(idCategory));
    }

    @Override
    public void deleteAll() {
        log.info("Eliminando todos los productos");
        products.clear();
    }

    @Override
    public UUID getRandomUUID() {
        return UUID.randomUUID();
    }
}
