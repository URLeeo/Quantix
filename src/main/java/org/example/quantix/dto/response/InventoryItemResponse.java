package org.example.quantix.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryItemResponse(
        Long id,
        String name,
        String sku,
        String category,
        Integer quantity,
        BigDecimal price,
        String supplierName,
        LocalDateTime createdAt
) {
}
