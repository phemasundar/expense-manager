package com.expensetracker.expensetracker.service;

import com.expensetracker.expensetracker.dto.ParsedReceiptDTO;

public interface ParsingService {

    ParsedReceiptDTO parseReceiptText(String rawText);

}
