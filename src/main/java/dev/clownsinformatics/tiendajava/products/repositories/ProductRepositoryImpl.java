package dev.clownsinformatics.tiendajava.products.repositories;

import dev.clownsinformatics.tiendajava.products.models.Categories;
import dev.clownsinformatics.tiendajava.products.models.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final Map<Long, Product> products = new LinkedHashMap<>();

    public ProductRepositoryImpl() {
        for (int i = 1; i <= 50; i++) {
            products.put((long) i, Product.builder()
                    .id((long) i)
                    .uuid(UUID.randomUUID())
                    .name("Product " + i)
                    .weight(2.5)
                    .category(Categories.values()[i % Categories.values().length])
                    .price(50.0 + i)
                    .idCategory((long) i)
                    .img("imagen" + i + ".jpg")
                    .stock(10)
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
    public List<Product> findAllByCategory(Categories category) {
        log.info("Buscando todos los productos de la categoría {}", category);
        return products.values().stream()
                .filter(product -> product.getCategory().equals(category)).toList();
    }

    @Override
    public List<Product> findAllByName(String name) {
        log.info("Buscando todos los productos con el nombre {}", name);
        return products.values().stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase())).toList();
    }

    @Override
    public List<Product> findAllByNameAndCategory(String name, Categories category) {
        log.info("Buscando todos los productos con el nombre {} y la categoría {}", name, category);
        return products.values().stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()) &&
                        product.getCategory().equals(category)).toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        log.info("Buscando el producto con el id {}", id);
        return products.get(id) != null ? Optional.of(products.get(id)) : Optional.empty();
    }

    @Override
    public Optional<Product> findByIdCategory(Long idCategory) {
        log.info("Buscando el producto con el id de categoría {}", idCategory);
        return products.values().stream()
                .filter(product -> product.getIdCategory().equals(idCategory)).findFirst();
    }

    @Override
    public Optional<Product> findByUUID(UUID uuid) {
        log.info("Buscando el producto con el UUID {}", uuid);
        return products.values().stream()
                .filter(product -> product.getUuid().equals(uuid)).findFirst();
    }

    @Override
    public Product save(Product product) {
        log.info("Guardando el producto {}", product);
        return products.put(product.getId(), product);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Eliminando el producto con el id {}", id);
        products.remove(id);
    }

    @Override
    public void deleteByIdCategory(Long idCategory) {
        log.info("Eliminando el producto con el id de categoría {}", idCategory);
        products.values().removeIf(product -> product.getIdCategory().equals(idCategory));
    }

    @Override
    public void deleteByUUID(UUID uuid) {
        log.info("Eliminando el producto con el UUID {}", uuid);
        products.values().removeIf(product -> product.getUuid().equals(uuid));
    }

    @Override
    public void deleteAll() {
        log.info("Eliminando todos los productos");
        products.clear();
    }

    @Override
    public Long nextId() {
        log.info("Obteniendo el siguiente id");
        return products.keySet().stream().mapToLong(value -> value).max().orElse(0) + 1;
    }
}
