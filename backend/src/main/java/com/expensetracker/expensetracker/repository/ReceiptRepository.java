package com.expensetracker.expensetracker.repository;

import com.expensetracker.expensetracker.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
