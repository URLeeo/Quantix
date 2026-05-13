package org.example.quantix.service;

import lombok.RequiredArgsConstructor;
import org.example.quantix.dto.request.InventoryItemRequest;
import org.example.quantix.dto.response.InventoryItemResponse;
import org.example.quantix.entity.InventoryItem;
import org.example.quantix.exception.DuplicateResourceException;
import org.example.quantix.exception.ResourceNotFoundException;
import org.example.quantix.mapper.InventoryItemMapper;
import org.example.quantix.repository.InventoryItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryItemMapper inventoryItemMapper;

    public InventoryItemResponse createItem(InventoryItemRequest request) {

        validateSkuForCreate(request.sku());

        InventoryItem item = inventoryItemMapper.toEntity(request);

        InventoryItem savedItem = inventoryItemRepository.save(item);

        return inventoryItemMapper.toResponse(savedItem);
    }

    @Transactional(readOnly = true)
    public InventoryItemResponse getItemById(Long id) {

        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory item not found with id: " + id
                        )
                );

        return inventoryItemMapper.toResponse(item);
    }

    @Transactional(readOnly = true)
    public Page<InventoryItemResponse> getAllItems(
            String search,
            String category,
            Pageable pageable
    ) {

        Page<InventoryItem> items;

        if (search != null && !search.isBlank()) {

            items = inventoryItemRepository
                    .findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(
                            search,
                            search,
                            pageable
                    );

        } else if (category != null && !category.isBlank()) {

            items = inventoryItemRepository
                    .findByCategoryIgnoreCase(
                            category,
                            pageable
                    );

        } else {

            items = inventoryItemRepository.findAll(pageable);
        }

        return items.map(inventoryItemMapper::toResponse);
    }

    public InventoryItemResponse updateItem(
            Long id,
            InventoryItemRequest request
    ) {

        InventoryItem existingItem = inventoryItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory item not found with id: " + id
                        )
                );

        validateSkuForUpdate(request.sku(), id);

        inventoryItemMapper.updateEntity(existingItem, request);

        InventoryItem updatedItem = inventoryItemRepository.save(existingItem);

        return inventoryItemMapper.toResponse(updatedItem);
    }

    public void deleteItem(Long id) {

        InventoryItem item = inventoryItemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory item not found with id: " + id
                        )
                );

        inventoryItemRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public Page<InventoryItemResponse> getLowStockItems(
            Integer threshold,
            Pageable pageable
    ) {

        Page<InventoryItem> lowStockItems =
                inventoryItemRepository.findByQuantityLessThanEqual(
                        threshold,
                        pageable
                );

        return lowStockItems.map(inventoryItemMapper::toResponse);
    }

    private void validateSkuForCreate(String sku) {

        boolean skuExists =
                inventoryItemRepository.existsBySkuIgnoreCase(sku);

        if (skuExists) {
            throw new DuplicateResourceException(
                    "Inventory item with SKU already exists: " + sku
            );
        }
    }

    private void validateSkuForUpdate(String sku, Long id) {

        boolean skuExists =
                inventoryItemRepository
                        .existsBySkuIgnoreCaseAndIdNot(sku, id);

        if (skuExists) {
            throw new DuplicateResourceException(
                    "Inventory item with SKU already exists: " + sku
            );
        }
    }
}
