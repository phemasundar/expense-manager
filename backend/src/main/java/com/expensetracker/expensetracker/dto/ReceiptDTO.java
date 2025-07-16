package com.expensetracker.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDTO {

    private String storeName;
    private LocalDate purchaseDate;
    private String extractedText;
    private String imageUrl;

}
