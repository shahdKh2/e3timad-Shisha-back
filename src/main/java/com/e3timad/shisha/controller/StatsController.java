package com.e3timad.shisha.controller;

import com.e3timad.shisha.dto.StatsResponse;
import com.e3timad.shisha.model.Invoice;
import com.e3timad.shisha.model.InvoiceItem;
import com.e3timad.shisha.repository.InvoiceRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final InvoiceRepository invoiceRepository;

    public StatsController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping
    public StatsResponse getStats() {

        List<Invoice> invoices = invoiceRepository.findAll();

        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.now();
        int currentYear = today.getYear();

        double todayRevenue = 0, monthRevenue = 0, yearRevenue = 0;
        long todayInvoices = 0, monthInvoices = 0, yearInvoices = 0;

        Map<String, Long> productSales = new HashMap<>();

        for (Invoice invoice : invoices) {

            LocalDate invoiceDate = invoice.getCreatedAt().toLocalDate();

            if (invoiceDate.equals(today)) {
                todayRevenue += invoice.getTotalPrice();
                todayInvoices++;
            }

            if (YearMonth.from(invoiceDate).equals(currentMonth)) {
                monthRevenue += invoice.getTotalPrice();
                monthInvoices++;
            }

            if (invoiceDate.getYear() == currentYear) {
                yearRevenue += invoice.getTotalPrice();
                yearInvoices++;
            }

            for (InvoiceItem item : invoice.getItems()) {
                String productType = item.getProduct().getType();
                productSales.put(
                        productType,
                        productSales.getOrDefault(productType, 0L) + item.getQuantity()
                );
            }
        }

        // 🔥 Top 5 products
        Map<String, Long> topProducts = productSales.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        // ✅ RETURN DTO USING CONSTRUCTOR
        return new StatsResponse(
                todayInvoices,
                todayRevenue,
                monthInvoices,
                monthRevenue,
                yearInvoices,
                yearRevenue,
                topProducts
        );
    }

}
