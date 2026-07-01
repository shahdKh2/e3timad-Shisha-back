package com.e3timad.shisha.controller;

import com.e3timad.shisha.model.*;
import com.e3timad.shisha.repository.*;
import com.e3timad.shisha.service.PricingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.e3timad.shisha.dto.CreateInvoiceRequest;

import java.time.LocalDateTime;
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
    @Autowired
    private InvoiceEditHistoryRepository historyRepository;
    @Autowired
    private ProductOfferRepository productOfferRepository;

    @Autowired
    private PricingService pricingService;

    public InvoiceController(InvoiceRepository invoiceRepository, InvoiceItemRepository invoiceItemRepository, ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @PostMapping
    public Invoice createInvoice(@RequestBody CreateInvoiceRequest request,
                                 HttpServletRequest httpRequest) {

        String adminName = (String) httpRequest.getAttribute("username");

        Invoice invoice = new Invoice();
        invoice.setAdminName(adminName);

        List<InvoiceItem> items = new ArrayList<>();

        double totalPrice = buildInvoiceItems(invoice, request.getItems(), items);

        double manualDiscount = request.getManualDiscount() != null
                ? request.getManualDiscount()
                : 0.0;

        double finalTotal = Math.max(totalPrice - manualDiscount, 0);

        invoice.setItems(items);
        invoice.setTotalPrice(finalTotal);
        invoice.setManualDiscount(manualDiscount);

        double paidAmount = request.getPaidAmount() != null
                ? request.getPaidAmount()
                : finalTotal;

        double remaining = finalTotal - paidAmount;

        invoice.setPaidAmount(paidAmount);
        invoice.setRemainingDebt(Math.max(remaining, 0));
        invoice.setHasDebt(remaining > 0);

        if (remaining > 0) {
            invoice.setCustomerName(request.getCustomerName());
            invoice.setCustomerPhone(request.getCustomerPhone());
        }

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

        // 1⃣ Restore product quantities (IMPORTANT)
        for (InvoiceItem item : invoice.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        // 2️ Delete invoice items
        invoiceItemRepository.deleteAll(invoice.getItems());

        // 3️ Delete invoice
        invoiceRepository.delete(invoice);

        return ResponseEntity.ok("Invoice deleted successfully");
    }


    //=========
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(
            @PathVariable Long id,
            @RequestBody CreateInvoiceRequest request,
            HttpServletRequest httpRequest
    ) {

        String adminName = (String) httpRequest.getAttribute("username");

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // restore old stock BEFORE modifying
        for (InvoiceItem oldItem : invoice.getItems()) {
            Product p = oldItem.getProduct();
            p.setQuantity(p.getQuantity() + oldItem.getQuantity());
            productRepository.save(p);
        }

        // clear old items properly (IMPORTANT)
        invoice.getItems().clear();

        List<InvoiceItem> newItems = new ArrayList<>();

        double total = buildInvoiceItems(invoice, request.getItems(), newItems);

        double manualDiscount = request.getManualDiscount() != null
                ? request.getManualDiscount()
                : 0.0;

        double finalTotal = Math.max(total - manualDiscount, 0);

        double paidAmount = request.getPaidAmount() != null
                ? request.getPaidAmount()
                : finalTotal;

        double remaining = finalTotal - paidAmount;

        invoice.setAdminName(adminName);
        invoice.setTotalPrice(finalTotal);
        invoice.setManualDiscount(manualDiscount);
        invoice.setPaidAmount(paidAmount);
        invoice.setRemainingDebt(Math.max(remaining, 0));
        invoice.setHasDebt(remaining > 0);

        if (remaining > 0) {
            invoice.setCustomerName(request.getCustomerName());
            invoice.setCustomerPhone(request.getCustomerPhone());
        }

        // attach new items safely
        invoice.getItems().addAll(newItems);

//    000000000000--HISTORY--0000000000000000
        //SAVE FIRST
        invoiceRepository.save(invoice);

        //  THEN HISTORY (IMPORTANT)
        InvoiceEditHistory history = new InvoiceEditHistory();
        history.setInvoice(invoice);
        history.setAdminName(adminName);
        history.setEditedAt(LocalDateTime.now());
//    00000000000000000000000000000000000
//        invoiceRepository.save(invoice);
        historyRepository.save(history);

        return ResponseEntity.ok(invoice);
    }

//    ===========

    private double buildInvoiceItems(
            Invoice invoice,
            List<CreateInvoiceRequest.Item> requestItems,
            List<InvoiceItem> itemsList
    ) {

        double totalPrice = 0.0;

        for (CreateInvoiceRequest.Item i : requestItems) {

            Product product = productRepository.findById(i.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getQuantity() < i.getQuantity()) {
                throw new RuntimeException("Not enough stock for: " + product.getType());
            }

            InvoiceItem item = new InvoiceItem();
            item.setProduct(product);
            item.setQuantity(i.getQuantity());
            item.setInvoice(invoice);
            item.setIsGift(i.getIsGift());

            if (Boolean.TRUE.equals(i.getIsGift())) {
                item.setPriceAtSale(0.0);
            } else {

                List<ProductOffer> offers =
                        productOfferRepository.findByProduct(product);

                double finalPrice =
                        pricingService.calculateItemPrice(product, i.getQuantity(), offers);

                item.setPriceAtSale(finalPrice);

                totalPrice += finalPrice;
            }

            //  reduce stock ONLY here (ok)
            product.setQuantity(product.getQuantity() - i.getQuantity());
            productRepository.save(product);

            itemsList.add(item);
        }

        return totalPrice;
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<InvoiceEditHistory>> getInvoiceHistory(@PathVariable Long id) {

        if (!invoiceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        List<InvoiceEditHistory> history = historyRepository.findByInvoiceId(id);

        return ResponseEntity.ok(history != null ? history : new ArrayList<>());
    }
}