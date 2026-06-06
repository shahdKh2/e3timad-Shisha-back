package com.e3timad.shisha.controller;


import com.e3timad.shisha.model.Product;
import com.e3timad.shisha.model.ProductOffer;
import com.e3timad.shisha.repository.ProductOfferRepository;
import com.e3timad.shisha.repository.ProductRepository;
import com.e3timad.shisha.service.PricingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/offers")
public class OfferController {

    private final ProductRepository productRepository;
    private final ProductOfferRepository productOfferRepository;
    private final PricingService pricingService;

    public OfferController(ProductRepository productRepository,
                           ProductOfferRepository productOfferRepository,
                           PricingService pricingService) {
        this.productRepository = productRepository;
        this.productOfferRepository = productOfferRepository;
        this.pricingService = pricingService;
    }

    @GetMapping("/item-price")
    public double getItemPrice(
            @RequestParam String barcode,
            @RequestParam int quantity) {

        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<ProductOffer> offers = productOfferRepository.findByProduct(product);

        return pricingService.calculateItemPrice(product, quantity, offers);
    }
}