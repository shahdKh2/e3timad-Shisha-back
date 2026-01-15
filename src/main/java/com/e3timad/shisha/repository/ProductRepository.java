package com.e3timad.shisha.repository;

import com.e3timad.shisha.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    boolean existsByCategoryAndType(String category, String type);
}
