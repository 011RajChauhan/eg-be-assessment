package com.eg.assignment.dto;

import java.math.BigDecimal;

public record ProcessOverdueRequest(BigDecimal lateFee, int overdueDays) {}
