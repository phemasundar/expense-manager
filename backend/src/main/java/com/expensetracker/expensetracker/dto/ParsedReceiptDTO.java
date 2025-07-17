package com.expensetracker.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedReceiptDTO {
    private String storeName;
    private LocalDate purchaseDate;
    private List<ReceiptItemDTO> items;
    private String rawText;
}
