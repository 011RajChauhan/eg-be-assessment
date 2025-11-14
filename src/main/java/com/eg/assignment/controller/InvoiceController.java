package com.eg.assignment.controller;

import com.eg.assignment.domain.Invoice;
import com.eg.assignment.dto.CreateInvoiceRequest;
import com.eg.assignment.dto.InvoiceResponse;
import com.eg.assignment.dto.PaymentRequest;
import com.eg.assignment.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    private final InvoiceService svc;

    public InvoiceController(InvoiceService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateInvoiceRequest req) {
        Invoice inv = svc.createInvoice(req.amount(), req.dueDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("id", inv.getId()));
    }

    @GetMapping
    public List<InvoiceResponse> list() {
        return svc.listInvoices().stream().map(InvoiceResponse::from).collect(Collectors.toList());
    }

    @PostMapping("/{id}/payments")
    public InvoiceResponse pay(@PathVariable String id, @RequestBody PaymentRequest req) {
        Invoice inv = svc.applyPayment(id, req.amount());
        return InvoiceResponse.from(inv);
    }

    @GetMapping("/{id}")
    public InvoiceResponse get(@PathVariable String id) {
        return InvoiceResponse.from(svc.getInvoice(id));
    }
}
