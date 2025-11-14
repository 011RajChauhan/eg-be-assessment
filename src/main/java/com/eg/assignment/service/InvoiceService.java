package com.eg.assignment.service;

import com.eg.assignment.domain.Invoice;
import com.eg.assignment.domain.InvoiceStatus;
import com.eg.assignment.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    private final InvoiceRepository repo;

    public InvoiceService(InvoiceRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Invoice createInvoice(BigDecimal amount, LocalDate dueDate) {
        Invoice inv = new Invoice(amount, dueDate);
        return repo.save(inv);
    }

    @Transactional(readOnly = true)
    public List<Invoice> listInvoices() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Invoice getInvoice(String id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
    }

    @Transactional
    public Invoice applyPayment(String id, BigDecimal amount) {
        Invoice invoice = getInvoice(id);
        invoice.applyPayment(amount);
        return repo.save(invoice);
    }

    /**
     * Process overdue invoices:
     * - For each pending invoice whose dueDate <= cutoff:
     *    - If partially paid: mark original as PAID, create a new invoice with remaining + lateFee
     *    - If unpaid: mark original as VOID, create new invoice with amount + lateFee
     */
    @Transactional
    public List<Invoice> processOverdue(BigDecimal lateFee, int overdueDays) {
        LocalDate cutoff = LocalDate.now().minusDays(overdueDays);
        List<Invoice> pending = repo.findByStatus(InvoiceStatus.PENDING).stream()
                .filter(i -> !i.getDueDate().isAfter(cutoff))
                .collect(Collectors.toList());

        for (Invoice inv : pending) {
            BigDecimal remaining = inv.getRemaining();
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
                inv.markPaid();
                repo.save(inv);
                continue;
            }
            if (inv.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
                inv.markPaid();
                repo.save(inv);
                Invoice newInv = new Invoice(remaining.add(lateFee), LocalDate.now().plusDays(overdueDays));
                newInv.setOverdueDays(overdueDays);
                repo.save(newInv);
            } else {
                inv.markVoided();
                repo.save(inv);
                Invoice newInv = new Invoice(inv.getAmount().add(lateFee), LocalDate.now().plusDays(overdueDays));
                newInv.setOverdueDays(overdueDays);
                repo.save(newInv);
            }
        }
        return pending;
    }
}
