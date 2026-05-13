package org.example.quantix.mapper;

import org.example.quantix.dto.request.InventoryItemRequest;
import org.example.quantix.dto.response.InventoryItemResponse;
import org.example.quantix.entity.InventoryItem;
import org.springframework.stereotype.Component;

@Component
public class InventoryItemMapper {

    public InventoryItem toEntity(InventoryItemRequest request) {
        return InventoryItem.builder()
                .name(request.name())
                .sku(request.sku())
                .category(request.category())
                .quantity(request.quantity())
                .price(request.price())
                .supplierName(request.supplierName())
                .build();
    }

    public InventoryItemResponse toResponse(InventoryItem item) {
        return new InventoryItemResponse(
                item.getId(),
                item.getName(),
                item.getSku(),
                item.getCategory(),
                item.getQuantity(),
                item.getPrice(),
                item.getSupplierName(),
                item.getCreatedAt()
        );
    }

    public void updateEntity(InventoryItem item, InventoryItemRequest request) {
        item.setName(request.name());
        item.setSku(request.sku());
        item.setCategory(request.category());
        item.setQuantity(request.quantity());
        item.setPrice(request.price());
        item.setSupplierName(request.supplierName());
    }
}