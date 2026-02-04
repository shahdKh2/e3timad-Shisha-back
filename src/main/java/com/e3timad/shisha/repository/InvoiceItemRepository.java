package com.e3timad.shisha.repository;

import com.e3timad.shisha.model.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    @Query("""
    SELECT ii.product.type, SUM(ii.quantity)
    FROM InvoiceItem ii
    GROUP BY ii.product.type
    ORDER BY SUM(ii.quantity) DESC
""")
    List<Object[]> findTopProducts();


}
