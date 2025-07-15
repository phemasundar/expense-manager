package com.expensetracker.expensetracker.service;

import com.expensetracker.expensetracker.dto.ReceiptDTO;
import com.expensetracker.expensetracker.dto.UploadResponse;
import com.expensetracker.expensetracker.entity.Receipt;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ReceiptService {
    UploadResponse processReceipt(MultipartFile file) throws IOException;

    Receipt createReceipt(ReceiptDTO receiptDTO);
}
