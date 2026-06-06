package com.e3timad.shisha.dto;

import com.e3timad.shisha.model.Product;
import com.e3timad.shisha.model.ProductOffer;

import java.util.List;

public class ProductDTO {
    private Long id;
    private String type;
    private Double sellingPrice;
    private List<ProductOffer> offers;

    public ProductDTO(Product p) {
        this.id = p.getId();
        this.type = p.getType();
        this.sellingPrice = p.getSellingPrice();
        this.offers = p.getOffers();
    }
}