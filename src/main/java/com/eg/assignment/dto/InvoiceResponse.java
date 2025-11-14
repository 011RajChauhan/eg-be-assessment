package com.eg.assignment.dto;

import com.eg.assignment.domain.Invoice;
import com.eg.assignment.domain.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoiceResponse(String id, BigDecimal amount, BigDecimal paidAmount, LocalDate dueDate, InvoiceStatus status) {
    public static InvoiceResponse from(Invoice i) {
        return new InvoiceResponse(i.getId(), i.getAmount(), i.getPaidAmount(), i.getDueDate(), i.getStatus());
    }
}