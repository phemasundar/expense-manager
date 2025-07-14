package com.expensetracker.expensetracker.controller;

import com.expensetracker.expensetracker.service.OcrService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/receipts")
public class ReceiptController {

    private final OcrService ocrService;

    public ReceiptController(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @Operation(summary = "Upload a receipt image for OCR processing")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadReceipt(@RequestParam("file") MultipartFile file) {
        try {
            String extractedText = ocrService.extractTextFromImage(file.getBytes());
            System.out.println("Extracted Text: " + extractedText);
            return ResponseEntity.ok(extractedText);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing image.");
        }
    }
}
