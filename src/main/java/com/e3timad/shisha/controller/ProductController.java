
package com.e3timad.shisha.controller;

import com.e3timad.shisha.model.Product;
import com.e3timad.shisha.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173") // Vite front port
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get products by category
    @GetMapping("/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }

    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @RequestBody Product updatedProduct
    ) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔹 Update editable fields
        product.setBarcode(updatedProduct.getBarcode());
        product.setQuantity(updatedProduct.getQuantity());
        product.setPurchasePrice(updatedProduct.getPurchasePrice());
        product.setSellingPrice(updatedProduct.getSellingPrice());
        product.setMinSellingPrice(updatedProduct.getMinSellingPrice());
        product.setHasOffer(updatedProduct.getHasOffer());
        product.setHasGift(updatedProduct.getHasGift());

        // 🔹 Auto-calculate profit
        if (product.getPurchasePrice() != null && product.getSellingPrice() != null) {
            product.setProfit(
                    product.getSellingPrice() - product.getPurchasePrice()
            );
        }

        return productRepository.save(product);
    }

    // ProductController.java
    @PostMapping
    public Product createProduct(@RequestBody Product newProduct) {
        // 🔹 حساب الربح تلقائي إذا موجود السعرين
        if (newProduct.getPurchasePrice() != null && newProduct.getSellingPrice() != null) {
            newProduct.setProfit(newProduct.getSellingPrice() - newProduct.getPurchasePrice());
        }
        return productRepository.save(newProduct);
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Product> getByBarcode(@PathVariable String barcode) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found with barcode: " + barcode));
        return ResponseEntity.ok(product);
    }

}
