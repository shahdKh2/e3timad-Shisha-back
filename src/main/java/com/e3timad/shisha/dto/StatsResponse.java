package com.e3timad.shisha.dto;

import java.util.Map;

public class StatsResponse {

    private long todayInvoices;
    private double todayRevenue;

    private long monthInvoices;
    private double monthRevenue;

    private long yearInvoices;
    private double yearRevenue;

    private Map<String, Long> topProducts;

    // Constructor
    public StatsResponse(
            long todayInvoices,
            double todayRevenue,
            long monthInvoices,
            double monthRevenue,
            long yearInvoices,
            double yearRevenue,
            Map<String, Long> topProducts
    ) {
        this.todayInvoices = todayInvoices;
        this.todayRevenue = todayRevenue;
        this.monthInvoices = monthInvoices;
        this.monthRevenue = monthRevenue;
        this.yearInvoices = yearInvoices;
        this.yearRevenue = yearRevenue;
        this.topProducts = topProducts;
    }

    public long getTodayInvoices() {
        return todayInvoices;
    }

    public void setTodayInvoices(long todayInvoices) {
        this.todayInvoices = todayInvoices;
    }

    public double getTodayRevenue() {
        return todayRevenue;
    }

    public void setTodayRevenue(double todayRevenue) {
        this.todayRevenue = todayRevenue;
    }

    public long getMonthInvoices() {
        return monthInvoices;
    }

    public void setMonthInvoices(long monthInvoices) {
        this.monthInvoices = monthInvoices;
    }

    public double getMonthRevenue() {
        return monthRevenue;
    }

    public void setMonthRevenue(double monthRevenue) {
        this.monthRevenue = monthRevenue;
    }

    public long getYearInvoices() {
        return yearInvoices;
    }

    public void setYearInvoices(long yearInvoices) {
        this.yearInvoices = yearInvoices;
    }

    public double getYearRevenue() {
        return yearRevenue;
    }

    public void setYearRevenue(double yearRevenue) {
        this.yearRevenue = yearRevenue;
    }

    public Map<String, Long> getTopProducts() {
        return topProducts;
    }

    public void setTopProducts(Map<String, Long> topProducts) {
        this.topProducts = topProducts;
    }
}
