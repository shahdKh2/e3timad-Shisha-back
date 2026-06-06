package com.e3timad.shisha.repository;


import com.e3timad.shisha.model.OfferType;
import com.e3timad.shisha.model.Product;
import com.e3timad.shisha.model.ProductOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductOfferRepository extends JpaRepository<ProductOffer, Long> {
    Optional<ProductOffer> findByProductAndOfferType(Product product, OfferType offerType);

    List<ProductOffer> findByProduct(Product product);}