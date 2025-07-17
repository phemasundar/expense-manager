package com.expensetracker.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptItemDTO {
    private String category;
    private String brandName;
    private String productName;
    private BigDecimal quantity;
    private String weight;
    private BigDecimal price;
}
