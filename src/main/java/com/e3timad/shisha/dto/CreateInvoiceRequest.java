package com.e3timad.shisha.dto;

import java.util.List;

public class CreateInvoiceRequest {

    private String adminName;
    private List<Item> items;
    private Boolean hasDebt;
    private Double paidAmount;

    private String customerName;
    private String customerPhone;

    private Double manualDiscount;
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Boolean getHasDebt() {
        return hasDebt;
    }

    public void setHasDebt(Boolean hasDebt) {
        this.hasDebt = hasDebt;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }
    public Double getManualDiscount() {
        return manualDiscount;
    }

    public void setManualDiscount(Double manualDiscount) {
        this.manualDiscount = manualDiscount;
    }
    public static class Item {
        private Long productId;
        private Integer quantity;
        private Boolean isGift;

        public Boolean getGift() {
            return isGift;
        }

        public void setGift(Boolean gift) {
            isGift = gift;
        }


        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Boolean getIsGift() {   // ✅ أضيفي هذا
            return isGift;
        }

        public void setIsGift(Boolean isGift) { // ✅ أضيفي هذا
            this.isGift = isGift;
        }
    }

    // getters & setters
    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
