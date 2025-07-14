package com.expensetracker.expensetracker.service;

import com.expensetracker.expensetracker.dto.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ReceiptService {
    UploadResponse processReceipt(MultipartFile file) throws IOException;
}
