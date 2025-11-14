package com.eg.assignment.controller;

import com.eg.assignment.domain.Invoice;
import com.eg.assignment.dto.InvoiceResponse;
import com.eg.assignment.dto.ProcessOverdueRequest;
import com.eg.assignment.service.InvoiceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/invoices")
public class OverdueController {
    private final InvoiceService svc;

    public OverdueController(InvoiceService svc) {
        this.svc = svc;
    }

    @PostMapping("/process-overdue")
    public List<InvoiceResponse> process(@RequestBody ProcessOverdueRequest req) {
        List<Invoice> processed = svc.processOverdue(req.lateFee(), req.overdueDays());
        return processed.stream().map(InvoiceResponse::from).collect(Collectors.toList());
    }
}
