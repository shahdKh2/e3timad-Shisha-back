package com.e3timad.shisha.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;   // تاريخ + وقت تلقائي
    private String adminName;           // اسم الأدمن
    private Double totalPrice;


    private Double paidAmount;
    private Double remainingDebt;
    private Boolean hasDebt;

    private String customerName;
    private String customerPhone;
    private Double manualDiscount;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InvoiceItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceEditHistory> editHistory = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (paidAmount == null) paidAmount = 0.0;
        if (remainingDebt == null) remainingDebt = totalPrice;
        if (hasDebt == null) hasDebt = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getRemainingDebt() {
        return remainingDebt;
    }

    public void setRemainingDebt(Double remainingDebt) {
        this.remainingDebt = remainingDebt;
    }

    public Boolean getHasDebt() {
        return hasDebt;
    }

    public void setHasDebt(Boolean hasDebt) {
        this.hasDebt = hasDebt;
    }

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

    public Double getManualDiscount() {
        return manualDiscount;
    }

    public void setManualDiscount(Double manualDiscount) {
        this.manualDiscount = manualDiscount;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public List<InvoiceEditHistory> getEditHistory() {
        return editHistory;
    }

    public void setEditHistory(List<InvoiceEditHistory> editHistory) {
        this.editHistory = editHistory;
    }
}
