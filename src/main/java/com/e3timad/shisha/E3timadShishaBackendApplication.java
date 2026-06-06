package com.e3timad.shisha;

import com.e3timad.shisha.model.OfferType;
import com.e3timad.shisha.model.Product;
import com.e3timad.shisha.model.ProductOffer;
import com.e3timad.shisha.model.User;
import com.e3timad.shisha.repository.ProductOfferRepository;
import com.e3timad.shisha.repository.ProductRepository;
import com.e3timad.shisha.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

@SpringBootApplication
public class E3timadShishaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(E3timadShishaBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner initAdmins(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("حمزه").isEmpty()) {
                userRepository.save(new User("حمزه", "hamzah123", "ADMIN"));
            }
            if (userRepository.findByUsername("علي").isEmpty()) {
                userRepository.save(new User("علي", "ali123", "ADMIN"));
            }
            System.out.println("Admin users Hamzah & Ali initialized.");
        };
    }

    @Bean
    CommandLineRunner initProductsAndOffers(ProductRepository productRepository,
                                            ProductOfferRepository productOfferRepository) {
        return args -> {
            loadProducts(productRepository);
            loadOffers(productRepository, productOfferRepository);
        };
    }

    private void loadProducts(ProductRepository productRepository) throws Exception {
        var resource = new ClassPathResource("products.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);

                String category = getString(values, 0);
                String type = getString(values, 1);
                if (category == null || type == null) continue;

                String barcode = getString(values, 2);
                Integer quantity = getInt(values, 3);
                Double purchasePrice = getDouble(values, 4);
                Double sellingPrice = getDouble(values, 5);
                Double minSellingPrice = getDouble(values, 6);
                Boolean hasOffer = getBoolean(values, 7);
                Boolean hasGift = getBoolean(values, 8);

                // ─── loadProducts ─────────────────────────────
                Optional<Product> optionalProduct = productRepository.findByBarcode(barcode);
                Product product;
                if (optionalProduct.isEmpty()) {
                    product = new Product(category, type, barcode, quantity,
                            purchasePrice, sellingPrice, minSellingPrice, hasOffer, hasGift);
                } else {
                    product = optionalProduct.get();
                    // Update existing product if needed
                    product.setCategory(category);
                    product.setType(type);
                    product.setQuantity(quantity);
                    product.setPurchasePrice(purchasePrice);
                    product.setSellingPrice(sellingPrice);
                    product.setMinSellingPrice(minSellingPrice);
                    System.out.println(
                            "Product: " + type +
                                    " | Selling: " + sellingPrice +
                                    " | Min: " + minSellingPrice
                    );
                    product.setHasOffer(hasOffer);
                    product.setHasGift(hasGift);
                }
                productRepository.save(product);
            }
        }
        System.out.println("Products initialized from CSV ✅");
    }
    private void loadOffers(ProductRepository productRepository,
                            ProductOfferRepository productOfferRepository) throws Exception {

        var resource = new ClassPathResource("product_offers.csv");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length < 4) continue;

                String barcode = getString(fields, 0);

                OfferType offerType;
                try {
                    offerType = OfferType.valueOf(getString(fields, 1).toUpperCase());
                } catch (Exception e) {
                    System.out.println("❌ Invalid offer type: " + getString(fields, 1));
                    continue;
                }

                Integer quantity = getInt(fields, 2);
                Double price = getDouble(fields, 3);

                Optional<Product> optionalProduct = productRepository.findByBarcode(barcode);
                if (optionalProduct.isEmpty()) continue;

                Product product = optionalProduct.get();

                Optional<ProductOffer> optionalOffer =
                        productOfferRepository.findByProductAndOfferType(product, offerType);

                ProductOffer offer;

                if (optionalOffer.isEmpty()) {
                    offer = new ProductOffer(product, offerType, quantity, price);
                } else {
                    offer = optionalOffer.get();
                    offer.setQuantity(quantity);
                    offer.setPrice(price);
                }

                productOfferRepository.save(offer);
            }
        }

        System.out.println("Product offers loaded from CSV ✅");
    }

    // ─── Helper methods ───────────────────────────────
    private String getString(String[] values, int index) {
        return (index < values.length && !values[index].trim().isEmpty())
                ? values[index].trim() : null;
    }

    private Integer getInt(String[] values, int index) {
        try {
            return (index < values.length && !values[index].trim().isEmpty())
                    ? Integer.parseInt(values[index].trim()) : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private Double getDouble(String[] values, int index) {
        try {
            return (index < values.length && !values[index].trim().isEmpty())
                    ? Double.parseDouble(values[index].trim()) : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Boolean getBoolean(String[] values, int index) {
        return (index < values.length && !values[index].trim().isEmpty())
                ? Boolean.parseBoolean(values[index].trim()) : false;
    }
}