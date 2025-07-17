package com.expensetracker.expensetracker.service.impl;

import com.expensetracker.expensetracker.dto.ReceiptItemDTO;
import com.expensetracker.expensetracker.dto.SaveReceiptRequestDTO;
import com.expensetracker.expensetracker.entity.Item;
import com.expensetracker.expensetracker.entity.Receipt;
import com.expensetracker.expensetracker.entity.ReceiptItem;
import com.expensetracker.expensetracker.entity.Store;
import com.expensetracker.expensetracker.repository.ItemRepository;
import com.expensetracker.expensetracker.repository.ReceiptRepository;
import com.expensetracker.expensetracker.repository.StoreRepository;
import com.expensetracker.expensetracker.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final StoreRepository storeRepository;
    private final ItemRepository itemRepository;

    @Override
    public Receipt createReceipt(SaveReceiptRequestDTO saveReceiptRequestDTO) {
        Store store = storeRepository.findByName(saveReceiptRequestDTO.getStoreName())
                .orElseGet(() -> {
                    Store newStore = new Store();
                    newStore.setName(saveReceiptRequestDTO.getStoreName());
                    return storeRepository.save(newStore);
                });

        Receipt receipt = new Receipt();
        receipt.setStore(store);
        receipt.setPurchaseDate(saveReceiptRequestDTO.getPurchaseDate());
        receipt.setCreatedAt(ZonedDateTime.now());

        List<ReceiptItem> receiptItems = new ArrayList<>();
        for (ReceiptItemDTO itemDTO : saveReceiptRequestDTO.getItems()) {
            Item item = itemRepository.findByName(itemDTO.getProductName())
                    .orElseGet(() -> {
                        Item newItem = new Item();
                        newItem.setName(itemDTO.getProductName());
                        return itemRepository.save(newItem);
                    });

            ReceiptItem receiptItem = new ReceiptItem();
            receiptItem.setItem(item);
            receiptItem.setPrice(itemDTO.getPrice());
            receiptItem.setQuantity(itemDTO.getQuantity());
            receiptItem.setReceipt(receipt);
            receiptItems.add(receiptItem);
        }

        receipt.setReceiptItems(receiptItems);
        return receiptRepository.save(receipt);
    }
}
