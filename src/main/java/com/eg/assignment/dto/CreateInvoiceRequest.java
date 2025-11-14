package com.eg.assignment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateInvoiceRequest(BigDecimal amount, LocalDate dueDate) {}
