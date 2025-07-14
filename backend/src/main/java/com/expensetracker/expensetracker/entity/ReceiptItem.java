package com.expensetracker.expensetracker.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "receipt_items")
public class ReceiptItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_item_id")
    private Long receiptItemId;

    @ManyToOne
    @JoinColumn(name = "receipt_id", referencedColumnName = "receipt_id")
    private Receipt receipt;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    // Getters and Setters
}
