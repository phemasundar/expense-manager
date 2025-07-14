package com.expensetracker.expensetracker.dto;

public class UploadResponse {
    private Long receiptId;
    private String extractedText;

    public UploadResponse(Long receiptId, String extractedText) {
        this.receiptId = receiptId;
        this.extractedText = extractedText;
    }

    // Getters and Setters
    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }
}
