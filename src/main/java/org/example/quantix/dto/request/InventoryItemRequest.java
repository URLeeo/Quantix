package org.example.quantix.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record InventoryItemRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "SKU is required")
        String sku,

        @NotBlank(message = "Category is required")
        String category,

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity cannot be negative")
        Integer quantity,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,

        @NotBlank(message = "Supplier name is required")
        String supplierName
) {
}
