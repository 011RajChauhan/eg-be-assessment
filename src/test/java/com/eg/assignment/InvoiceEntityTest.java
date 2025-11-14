package com.eg.assignment;

import com.eg.assignment.domain.Invoice;
import com.eg.assignment.domain.InvoiceStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InvoiceEntityTest {
    @Test
    void testPaymentFlow() {
        Invoice inv = new Invoice(new BigDecimal("100.00"), LocalDate.now().plusDays(2));
        assertEquals(BigDecimal.ZERO, inv.getPaidAmount());
        inv.applyPayment(new BigDecimal("50.00"));
        assertEquals(new BigDecimal("50.00"), inv.getPaidAmount());
        assertEquals(InvoiceStatus.PENDING, inv.getStatus());
        inv.applyPayment(new BigDecimal("50.00"));
        assertEquals(InvoiceStatus.PAID, inv.getStatus());
        assertEquals(new BigDecimal("100.00"), inv.getPaidAmount());
    }

    @Test
    void invalidPaymentThrows() {
        Invoice inv = new Invoice(new BigDecimal("20.00"), LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> inv.applyPayment(new BigDecimal("-1")));
    }
}
