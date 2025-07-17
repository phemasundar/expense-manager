package com.expensetracker.expensetracker.service;

import com.expensetracker.expensetracker.dto.SaveReceiptRequestDTO;
import com.expensetracker.expensetracker.entity.Receipt;

public interface ReceiptService {

    Receipt createReceipt(SaveReceiptRequestDTO saveReceiptRequestDTO);
}
