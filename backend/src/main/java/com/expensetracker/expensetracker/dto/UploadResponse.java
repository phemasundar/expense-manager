package com.expensetracker.expensetracker.dto;

public class UploadResponse {
    private Long id;
    private String extractedText;

    public UploadResponse(Long id, String extractedText) {
        this.id = id;
        this.extractedText = extractedText;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }
}
