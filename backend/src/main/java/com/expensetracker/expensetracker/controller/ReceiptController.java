package com.expensetracker.expensetracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/receipts")
public class ReceiptController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadReceipt(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok("File uploaded successfully.");
    }
}
