package com.e3timad.shisha.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "product_offers")
public class ProductOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 🔹 نوع العرض
    @Enumerated(EnumType.STRING)
    private OfferType offerType;

    // 🔹 تفاصيل العرض
    private Integer quantity;   // مثال: 3 (كل 3)
    private Double price;       // مثال: 11 شيكل

    // 🔹 Constructors
    public ProductOffer() {
    }

    public ProductOffer(Product product, OfferType offerType, Integer quantity, Double price) {
        this.product = product;
        this.offerType = offerType;
        this.quantity = quantity;
        this.price = price;
    }


    @Transient
    public String getDisplayText() {
        if (offerType == OfferType.UNIT) {
            return "الحبة بـ " + price;
        } else if (offerType == OfferType.BULK) {
            return "كل " + quantity + " بـ " + price;
        } else {
            return "كروز بـ " + price;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OfferType getOfferType() {
        return offerType;
    }

    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}