package com.expensetracker.expensetracker.service.impl;

import com.expensetracker.expensetracker.dto.UploadResponse;
import com.expensetracker.expensetracker.entity.Receipt;
import com.expensetracker.expensetracker.repository.ReceiptRepository;
import com.expensetracker.expensetracker.service.OcrService;
import com.expensetracker.expensetracker.service.ReceiptService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final OcrService ocrService;
    private final ReceiptRepository receiptRepository;

    public ReceiptServiceImpl(OcrService ocrService, ReceiptRepository receiptRepository) {
        this.ocrService = ocrService;
        this.receiptRepository = receiptRepository;
    }

    @Override
    public UploadResponse processReceipt(MultipartFile file) throws IOException {
        try {
            String extractedText = ocrService.extractTextFromImage(file.getBytes());

            Receipt receipt = new Receipt();
            receipt.setImageUrl(""); // Placeholder for image URL
            receipt.setExtractedText(extractedText);
            receipt.setCreatedAt(ZonedDateTime.now());
            receipt.setPurchaseDate(LocalDate.now()); // Placeholder for purchase date

            Receipt savedReceipt = receiptRepository.save(receipt);

            return new UploadResponse(savedReceipt.getId(), extractedText);
        } catch (IOException e) {
            // Log the error and re-throw a more specific exception
            throw new IOException("Error processing receipt image: " + e.getMessage(), e);
        }
    }
}
