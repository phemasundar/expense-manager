package com.expensetracker.expensetracker.service.impl;

import com.expensetracker.expensetracker.dto.ParsedReceiptDTO;
import com.expensetracker.expensetracker.dto.ReceiptItemDTO;
import com.expensetracker.expensetracker.service.ParsingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParsingServiceImpl implements ParsingService {

    private static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy")
    );

    @Override
    public ParsedReceiptDTO parseReceiptText(String rawText) {
        ParsedReceiptDTO parsedReceipt = new ParsedReceiptDTO();
        parsedReceipt.setRawText(rawText);

        if (rawText == null || rawText.isEmpty()) {
            return parsedReceipt;
        }

        String[] lines = rawText.split("\\r?\\n");

        // 1. Extract Store Name (first line)
        if (lines.length > 0) {
            parsedReceipt.setStoreName(lines[0].trim());
        }

        // 2. Extract Purchase Date
        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            for (String part : parts) {
                for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                    try {
                        LocalDate date = LocalDate.parse(part, formatter);
                        parsedReceipt.setPurchaseDate(date);
                        break;
                    } catch (DateTimeParseException e) {
                        // Ignore
                    }
                }
                if (parsedReceipt.getPurchaseDate() != null) break;
            }
            if (parsedReceipt.getPurchaseDate() != null) break;
        }

        // 3. Extract Line Items
        List<ReceiptItemDTO> receiptItems = new ArrayList<>();
        Pattern pricePattern = Pattern.compile(".*?F\\s+\\$(\\d+\\.\\d{2})");
        Pattern categoryPattern = Pattern.compile("^[A-Z/\\s]+$");

        int startOfItems = findLineIndex(lines, "GROCERY");
        int endOfItems = findLineIndex(lines, "SUB TOTAL");

        if (startOfItems != -1 && endOfItems != -1) {
            StringBuilder currentItemNameBuilder = new StringBuilder();
            for (int i = startOfItems + 1; i < endOfItems; i++) {
                String currentLine = lines[i].trim();
                Matcher priceMatcher = pricePattern.matcher(currentLine);

                if (priceMatcher.matches()) {
                    String namePart = currentLine.substring(0, currentLine.indexOf('F')).trim();
                    if (currentItemNameBuilder.length() > 0) {
                        currentItemNameBuilder.append(" ");
                    }
                    currentItemNameBuilder.append(namePart);

                    String priceStr = priceMatcher.group(1);

                    ReceiptItemDTO itemDTO = new ReceiptItemDTO();
                    itemDTO.setProductName(currentItemNameBuilder.toString().trim());
                    itemDTO.setPrice(new BigDecimal(priceStr));
                    receiptItems.add(itemDTO);

                    currentItemNameBuilder = new StringBuilder(); // Reset for next item
                } else {
                    Matcher categoryMatcher = categoryPattern.matcher(currentLine);
                    if (categoryMatcher.matches() && currentLine.split("\\s+").length <= 3) {
                        // It's likely a category header, so ignore it.
                    } else {
                        if (currentItemNameBuilder.length() > 0) {
                            currentItemNameBuilder.append(" ");
                        }
                        currentItemNameBuilder.append(currentLine);
                    }
                }
            }
        }

        parsedReceipt.setItems(receiptItems);
        return parsedReceipt;
    }

    private int findLineIndex(String[] lines, String text) {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains(text)) {
                return i;
            }
        }
        return -1;
    }
}
