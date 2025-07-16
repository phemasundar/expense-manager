package com.expensetracker.expensetracker.controller;

import com.expensetracker.expensetracker.dto.ReceiptDTO;
import com.expensetracker.expensetracker.dto.UploadResponse;
import com.expensetracker.expensetracker.entity.Receipt;
import com.expensetracker.expensetracker.repository.ReceiptRepository;
import com.expensetracker.expensetracker.service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;
    private final ReceiptRepository receiptRepository;

    @Operation(summary = "Create a new receipt")
    @PostMapping
    public ResponseEntity<Map<String, Long>> createReceipt(@RequestBody ReceiptDTO receiptDTO) {
        Receipt receipt = receiptService.createReceipt(receiptDTO);
        return ResponseEntity.ok(Map.of("receipt_id", receipt.getId()));
    }

    @Operation(summary = "Upload a receipt image for OCR processing")
    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadReceipt(@RequestParam("file") MultipartFile file) {
        try {
            UploadResponse response = receiptService.processReceipt(file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "Get a receipt by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Receipt> getReceipt(@PathVariable Long id) {
        return receiptRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
