package org.example.quantix.repository;

import org.example.quantix.entity.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Page<InventoryItem> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(
            String name,
            String sku,
            Pageable pageable
    );

    Page<InventoryItem> findByCategoryIgnoreCase(
            String category,
            Pageable pageable
    );

    Page<InventoryItem> findByQuantityLessThanEqual(
            Integer threshold,
            Pageable pageable
    );

    boolean existsBySkuIgnoreCase(String sku);

    boolean existsBySkuIgnoreCaseAndIdNot(String sku, Long id);
}