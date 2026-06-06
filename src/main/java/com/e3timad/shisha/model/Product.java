package com.e3timad.shisha.model;

import jakarta.persistence.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
        name = "products",
        uniqueConstraints = @UniqueConstraint(columnNames = {"category", "type"})
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Basic info
    private String category;     // category name (أراجيل، معسل...)
    private String type;         // type / subtype
    private String barcode;      // barcode

    // 🔹 Stock & prices
    private Integer quantity;
    private Double purchasePrice;     // سعر الشراء
    private Double sellingPrice;      // سعر البيع
    private Double profit;            // الربح
    private Double minSellingPrice;   // الحد الأدنى للبيع

    // 🔹 Flags
    private Boolean hasOffer;          // عروض
    private Boolean hasGift;           // هدية

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductOffer> offers;

    // 🔹 Constructors
    public Product() {
    }

    public Product(
            String category,
            String type,
            String barcode,
            Integer quantity,
            Double purchasePrice,
            Double sellingPrice,
            Double minSellingPrice,
            Boolean hasOffer,
            Boolean hasGift
    ) {
        this.category = category;
        this.type = type;
        this.barcode = barcode;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.minSellingPrice = minSellingPrice;
        this.hasOffer = hasOffer;
        this.hasGift = hasGift;
        this.profit = (sellingPrice != null && purchasePrice != null)
                ? sellingPrice - purchasePrice
                : 0.0;
    }

    // 🔹 Getters & Setters
    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
        recalcProfit();
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
        recalcProfit();
    }

    public Double getProfit() {
        return profit;
    }

    public Double getMinSellingPrice() {
        return minSellingPrice;
    }

    public void setMinSellingPrice(Double minSellingPrice) {
        this.minSellingPrice = minSellingPrice;
    }

    public Boolean getHasOffer() {
        return hasOffer;
    }

    public void setHasOffer(Boolean hasOffer) {
        this.hasOffer = hasOffer;
    }

    public Boolean getHasGift() {
        return hasGift;
    }

    public void setHasGift(Boolean hasGift) {
        this.hasGift = hasGift;
    }

    // 🔹 Helper
    private void recalcProfit() {
        if (sellingPrice != null && purchasePrice != null) {
            this.profit = sellingPrice - purchasePrice;
        }
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public List<ProductOffer> getOffers() {
        return offers;
    }

    public void setOffers(List<ProductOffer> offers) {
        this.offers = offers;
    }
}
