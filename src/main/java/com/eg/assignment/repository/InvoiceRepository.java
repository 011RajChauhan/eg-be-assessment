package com.eg.assignment.repository;

import com.eg.assignment.domain.Invoice;
import com.eg.assignment.domain.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    List<Invoice> findByStatus(InvoiceStatus status);
}