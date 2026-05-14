package org.example.quantix.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import org.example.quantix.dto.request.InventoryItemRequest;
import org.example.quantix.dto.response.InventoryItemResponse;
import org.example.quantix.service.InventoryItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory-items")
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    @PostMapping
    public ResponseEntity<InventoryItemResponse> createItem(
           @Valid @RequestBody InventoryItemRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inventoryItemService.createItem(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemResponse> getItemById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                inventoryItemService.getItemById(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<InventoryItemResponse>> getAllItems(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                inventoryItemService.getAllItems(search, category, pageable)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemResponse> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody InventoryItemRequest request
    ) {
        return ResponseEntity.ok(
                inventoryItemService.updateItem(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long id
    ) {
        inventoryItemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/low-stock")
    public ResponseEntity<Page<InventoryItemResponse>> getLowStockItems(
            @RequestParam Integer threshold,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                inventoryItemService.getLowStockItems(threshold, pageable)
        );
    }
}