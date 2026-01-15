package com.e3timad.shisha.repository;


import com.e3timad.shisha.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
