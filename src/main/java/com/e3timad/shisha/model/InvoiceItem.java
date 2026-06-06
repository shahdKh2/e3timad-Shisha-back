package com.e3timad.shisha.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private Integer quantity;
    private Double priceAtSale; // سعر المنتج وقت الشراء


    // ********* POS System ********
    private Double originalPrice;
    private Double offerDiscount;
    private Double manualDiscountShare;
    //

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    private Invoice invoice;

    private Boolean isGift;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getOfferDiscount() {
        return offerDiscount;
    }

    public void setOfferDiscount(Double offerDiscount) {
        this.offerDiscount = offerDiscount;
    }

    public Double getManualDiscountShare() {
        return manualDiscountShare;
    }

    public void setManualDiscountShare(Double manualDiscountShare) {
        this.manualDiscountShare = manualDiscountShare;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPriceAtSale() {
        return priceAtSale;
    }

    public void setPriceAtSale(Double priceAtSale) {
        this.priceAtSale = priceAtSale;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Boolean getGift() {
        return isGift;
    }

    public void setIsGift(Boolean gift) {
        isGift = gift;
    }
}