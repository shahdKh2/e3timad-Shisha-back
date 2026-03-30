package com.e3timad.shisha.repository;


import com.e3timad.shisha.model.InvoiceEditHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceEditHistoryRepository extends JpaRepository<InvoiceEditHistory, Long> {
    List<InvoiceEditHistory> findByInvoiceId(Long invoiceId);
}
