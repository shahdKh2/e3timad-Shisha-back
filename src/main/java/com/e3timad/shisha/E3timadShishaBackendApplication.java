package com.e3timad.shisha;

import com.e3timad.shisha.model.Product;
import com.e3timad.shisha.model.User;
import com.e3timad.shisha.repository.ProductRepository;
import com.e3timad.shisha.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.FileReader;

@SpringBootApplication
public class E3timadShishaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(E3timadShishaBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner initAdmins(UserRepository userRepository) {
        return args -> {

            if (userRepository.findByUsername("حمزه").isEmpty()) {
                userRepository.save(
                        new User("حمزه", "hamzah123", "ADMIN")
                );
            }

            if (userRepository.findByUsername("علي").isEmpty()) {
                userRepository.save(
                        new User("علي", "ali123", "ADMIN")
                );
            }

            System.out.println("Admin users Hamzah & Ali initialized.");
        };
    }
    private String getString(String[] values, int index) {
        return (index < values.length && !values[index].trim().isEmpty())
                ? values[index].trim()
                : null;
    }

    private Integer getInt(String[] values, int index) {
        try {
            return (index < values.length && !values[index].trim().isEmpty())
                    ? Integer.parseInt(values[index].trim())
                    : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private Double getDouble(String[] values, int index) {
        try {
            return (index < values.length && !values[index].trim().isEmpty())
                    ? Double.parseDouble(values[index].trim())
                    : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Boolean getBoolean(String[] values, int index) {
        return (index < values.length && !values[index].trim().isEmpty())
                ? Boolean.parseBoolean(values[index].trim())
                : false;
    }

    @Bean
    CommandLineRunner initProducts(ProductRepository productRepository) {
        return args -> {

            String path = "src/main/resources/products.csv";

            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
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

                    if (!productRepository.existsByCategoryAndType(category, type)) {
                        productRepository.save(
                                new Product(
                                        category,
                                        type,
                                        barcode,           // null if missing
                                        quantity,          // 0 if missing
                                        purchasePrice,     // 0.0 if missing
                                        sellingPrice,      // 0.0 if missing
                                        minSellingPrice,   // 0.0 if missing
                                        hasOffer,          // false if missing
                                        hasGift            // false if missing
                                )
                        );
                    }


            }

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Products initialized from CSV with default values");
        };
    }


}
