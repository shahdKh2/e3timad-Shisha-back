package com.e3timad.shisha.service;

import com.e3timad.shisha.dto.StatsResponse;
import com.e3timad.shisha.repository.InvoiceItemRepository;
import com.e3timad.shisha.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;

    public StatsService(
            InvoiceRepository invoiceRepository,
            InvoiceItemRepository invoiceItemRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
    }

    public StatsResponse getStats() {

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfYear = Year.now().atDay(1).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        long todayInvoices = invoiceRepository.countByCreatedAtBetween(startOfToday, now);
        double todayRevenue = invoiceRepository.sumRevenueBetween(startOfToday, now);

        long monthInvoices = invoiceRepository.countByCreatedAtBetween(startOfMonth, now);
        double monthRevenue = invoiceRepository.sumRevenueBetween(startOfMonth, now);

        long yearInvoices = invoiceRepository.countByCreatedAtBetween(startOfYear, now);
        double yearRevenue = invoiceRepository.sumRevenueBetween(startOfYear, now);

        // Top products
        List<Object[]> results = invoiceItemRepository.findTopProducts();
        Map<String, Long> topProducts = new HashMap<>();

        for (Object[] row : results) {
            topProducts.put((String) row[0], (Long) row[1]);
        }

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
