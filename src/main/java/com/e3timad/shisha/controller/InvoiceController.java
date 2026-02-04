package com.e3timad.shisha.controller;

import com.e3timad.shisha.model.Invoice;
import com.e3timad.shisha.model.InvoiceItem;
import com.e3timad.shisha.model.Product;
import com.e3timad.shisha.repository.InvoiceItemRepository;
import com.e3timad.shisha.repository.InvoiceRepository;
import com.e3timad.shisha.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.e3timad.shisha.dto.CreateInvoiceRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(
        origins = "http://localhost:5173",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final ProductRepository productRepository;

    public InvoiceController(InvoiceRepository invoiceRepository, InvoiceItemRepository invoiceItemRepository, ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @PostMapping
    public Invoice createInvoice(@RequestBody CreateInvoiceRequest request, HttpServletRequest httpRequest) {
        String adminName = (String) httpRequest.getAttribute("username");
        System.out.println("\n0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n\nadminName: " + adminName);
        Invoice invoice = new Invoice();
        invoice.setAdminName(adminName);
        List<InvoiceItem> items = new ArrayList<>();
        double totalPrice = 0.0;
        for (CreateInvoiceRequest.Item i : request.getItems()) {
            if (i.getProductId() == null) {
                throw new RuntimeException("Product ID cannot be null");
            }
            Product product = productRepository.findById(i.getProductId()).orElseThrow(() -> new RuntimeException("\n\n\n✅✅✅✅✅\nProduct not found with id: " + i.getProductId()));
            if (product.getQuantity() < i.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getType());
            }
            InvoiceItem item = new InvoiceItem();
            item.setProduct(product);
            item.setQuantity(i.getQuantity());
            item.setInvoice(invoice);
            item.setIsGift(i.getIsGift());
            if (Boolean.TRUE.equals(i.getIsGift())) {
                item.setPriceAtSale(0.0);
            } else {
                double price = product.getSellingPrice() * i.getQuantity();
                item.setPriceAtSale(price);
                totalPrice += price;
            }
            product.setQuantity(product.getQuantity() - i.getQuantity());
            productRepository.save(product);
            items.add(item);
        }
        invoice.setItems(items);
        invoice.setTotalPrice(totalPrice);
        invoiceRepository.save(invoice);
        invoiceItemRepository.saveAll(items);
        return invoice;
    }

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Long id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        // 1️⃣ Restore product quantities (IMPORTANT)
        for (InvoiceItem item : invoice.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        // 2️⃣ Delete invoice items
        invoiceItemRepository.deleteAll(invoice.getItems());

        // 3️⃣ Delete invoice
        invoiceRepository.delete(invoice);

        return ResponseEntity.ok("Invoice deleted successfully");
    }

    //====================
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(
            @PathVariable Long id,
            @RequestBody CreateInvoiceRequest request) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.getItems().clear();

        double total = 0.0;

        for (CreateInvoiceRequest.Item reqItem : request.getItems()) {

            Product product = productRepository.findById(reqItem.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException("Product not found: " + reqItem.getProductId())
                    );

            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setProduct(product);
            item.setQuantity(reqItem.getQuantity());
            item.setIsGift(reqItem.getIsGift());
            item.setPriceAtSale(product.getSellingPrice());

            if (!Boolean.TRUE.equals(item.getIsGift())) {
                total += item.getPriceAtSale() * item.getQuantity();
            }

            invoice.getItems().add(item);
        }

        invoice.setTotalPrice(total);

        invoiceRepository.save(invoice);
        return ResponseEntity.ok(invoice);
    }



}
