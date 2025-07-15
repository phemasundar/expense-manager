package com.expensetracker.expensetracker.dto;

public class UploadResponse {

    private String extractedText;
    private String imageUrl;

    public UploadResponse(String extractedText, String imageUrl) {
        this.extractedText = extractedText;
        this.imageUrl = imageUrl;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

