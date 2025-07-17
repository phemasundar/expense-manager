package com.expensetracker.expensetracker.controller;

import com.expensetracker.expensetracker.dto.ParsedReceiptDTO;
import com.expensetracker.expensetracker.dto.SaveReceiptRequestDTO;
import com.expensetracker.expensetracker.entity.Receipt;
import com.expensetracker.expensetracker.repository.ReceiptRepository;
import com.expensetracker.expensetracker.service.OcrService;
import com.expensetracker.expensetracker.service.ParsingService;
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
    private final OcrService ocrService;
    private final ParsingService parsingService;

    @Operation(summary = "Create a new receipt")
    @PostMapping
    public ResponseEntity<Map<String, Long>> createReceipt(@RequestBody SaveReceiptRequestDTO saveReceiptRequestDTO) {
        Receipt receipt = receiptService.createReceipt(saveReceiptRequestDTO);
        return ResponseEntity.ok(Map.of("receipt_id", receipt.getId()));
    }

    @Operation(summary = "Upload a receipt image for OCR processing")
    @PostMapping("/upload")
    public ResponseEntity<ParsedReceiptDTO> uploadReceipt(@RequestParam("file") MultipartFile file) {
        try {
            String rawText = ocrService.extractTextFromImage(file.getBytes());
            ParsedReceiptDTO parsedReceipt = parsingService.parseReceiptText(rawText);
            return ResponseEntity.ok(parsedReceipt);
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
