package ecommerce.ecommerce.business.repository;

import ecommerce.ecommerce.domain.Product;

import java.util.Optional;

public interface ProductRepository {
    Product create(Product product);
    Product update(Product product);
    Optional<Product> findById(Long productId);
    Boolean existsByName(String name);
}
