package com.eg.assignment.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 4, name = "paid_amount")
    private BigDecimal paidAmount;

    @Column(nullable = false, name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private InvoiceStatus status;

    @Column(name = "overdue_days")
    private Integer overdueDays;

    protected Invoice() {
        // JPA
    }

    public Invoice(BigDecimal amount, LocalDate dueDate) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.paidAmount = BigDecimal.ZERO;
        this.dueDate = dueDate;
        this.status = InvoiceStatus.PENDING;
        this.overdueDays = 0;
    }

    public String getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getPaidAmount() { return paidAmount; }
    public LocalDate getDueDate() { return dueDate; }
    public InvoiceStatus getStatus() { return status; }
    public Integer getOverdueDays() { return overdueDays; }

    public BigDecimal getRemaining() {
        return amount.subtract(paidAmount).max(BigDecimal.ZERO);
    }

    public void applyPayment(BigDecimal payment) {
        if (status != InvoiceStatus.PENDING) {
            throw new IllegalStateException("Cannot pay an invoice that is not pending");
        }
        if (payment.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment must be positive");
        }
        this.paidAmount = this.paidAmount.add(payment);
        if (this.paidAmount.compareTo(this.amount) >= 0) {
            this.paidAmount = this.amount;
            this.status = InvoiceStatus.PAID;
        }
    }

    public void markVoided() {
        this.status = InvoiceStatus.VOID;
    }

    public void markPaid() {
        this.paidAmount = this.amount;
        this.status = InvoiceStatus.PAID;
    }

    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setOverdueDays(int days) { this.overdueDays = days; }
}
