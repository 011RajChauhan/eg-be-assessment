package com.eg.assignment;

import com.eg.assignment.domain.Invoice;
import com.eg.assignment.domain.InvoiceStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class EgInvoiceServiceApplicationTests {

	@Test
	void contextLoads() {
	}

    @Test
    void testCreateAndPaymentFlow() {
        Invoice inv = new Invoice(new BigDecimal("100.00"), LocalDate.now().plusDays(5));
        assertEquals(BigDecimal.ZERO, inv.getPaidAmount());
        assertEquals(new BigDecimal("100.00"), inv.getRemaining());
        inv.applyPayment(new BigDecimal("30.00"));
        assertEquals(new BigDecimal("30.00"), inv.getPaidAmount());
        assertEquals(new BigDecimal("70.00"), inv.getRemaining());
        inv.applyPayment(new BigDecimal("70.00"));
        assertEquals(new BigDecimal("100.00"), inv.getPaidAmount());
        assertEquals(InvoiceStatus.PAID, inv.getStatus());
    }

    @Test
    void testApplyInvalidPayment() {
        Invoice inv = new Invoice(new BigDecimal("50"), LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> inv.applyPayment(new BigDecimal("-10")));
    }

    @Test
    void testMarkVoided() {
        Invoice inv = new Invoice(new BigDecimal("50"), LocalDate.now());
        inv.markVoided();
        assertEquals(InvoiceStatus.VOID, inv.getStatus());
    }

}
