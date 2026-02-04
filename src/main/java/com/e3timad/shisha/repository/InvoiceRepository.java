package com.e3timad.shisha.repository;


import com.e3timad.shisha.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
    SELECT COALESCE(SUM(i.totalPrice), 0)
    FROM Invoice i
    WHERE i.createdAt BETWEEN :start AND :end
""")
    double sumRevenueBetween(LocalDateTime start, LocalDateTime end);
}
