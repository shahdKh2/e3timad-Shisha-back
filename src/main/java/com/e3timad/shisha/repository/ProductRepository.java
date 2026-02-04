package com.e3timad.shisha.repository;

import com.e3timad.shisha.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    boolean existsByCategoryAndType(String category, String type);
    Optional<Product> findByBarcode(String barcode);

}
