package com.expensetracker.expensetracker.service.impl;

import com.expensetracker.expensetracker.dto.ReceiptDTO;
import com.expensetracker.expensetracker.dto.UploadResponse;
import com.expensetracker.expensetracker.entity.Receipt;
import com.expensetracker.expensetracker.entity.Store;
import com.expensetracker.expensetracker.repository.ReceiptRepository;
import com.expensetracker.expensetracker.repository.StoreRepository;
import com.expensetracker.expensetracker.service.FileStorageService;
import com.expensetracker.expensetracker.service.OcrService;
import com.expensetracker.expensetracker.service.ReceiptService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZonedDateTime;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final OcrService ocrService;
    private final ReceiptRepository receiptRepository;
    private final StoreRepository storeRepository;
    private final FileStorageService fileStorageService;

    public ReceiptServiceImpl(OcrService ocrService, ReceiptRepository receiptRepository, StoreRepository storeRepository, FileStorageService fileStorageService) {
        this.ocrService = ocrService;
        this.receiptRepository = receiptRepository;
        this.storeRepository = storeRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public UploadResponse processReceipt(MultipartFile file) throws IOException {
        try {
            String extractedText = ocrService.extractTextFromImage(file.getBytes());
            String imageUrl = fileStorageService.storeFile(file);
            return new UploadResponse(extractedText, imageUrl);
        } catch (IOException e) {
            throw new IOException("Error processing receipt image: " + e.getMessage(), e);
        }
    }

    @Override
    public Receipt createReceipt(ReceiptDTO receiptDTO) {
        Store store = storeRepository.findByName(receiptDTO.getStoreName())
                .orElseGet(() -> {
                    Store newStore = new Store();
                    newStore.setName(receiptDTO.getStoreName());
                    return storeRepository.save(newStore);
                });

        Receipt receipt = new Receipt();
        receipt.setStore(store);
        receipt.setPurchaseDate(receiptDTO.getPurchaseDate());
        receipt.setExtractedText(receiptDTO.getExtractedText());
        receipt.setImageUrl(receiptDTO.getImageUrl());
        receipt.setCreatedAt(ZonedDateTime.now());
        // Set other fields as needed

        return receiptRepository.save(receipt);
    }
}
