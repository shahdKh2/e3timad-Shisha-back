package com.e3timad.shisha.dto;

import java.util.List;

public class CreateInvoiceRequest {

    private String adminName;
    private List<Item> items;

    public static class Item {
        private Long productId;
        private Integer quantity;
        private Boolean isGift; // ✅ أضيفي هذا

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
