package com.example.inventory.controller;

import com.example.inventory.dto.InventoryDTO;
import com.example.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    // Get all items
    @GetMapping("/getitems")
    public ResponseEntity<List<InventoryDTO>> getItems(){
        try {
            List<InventoryDTO> items = inventoryService.getAllItems();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get one item
    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> getItemById(@PathVariable Integer itemId){
        try {
            InventoryDTO item = inventoryService.getItemById(itemId);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Item not found with id: " + itemId);
        }
    }

    // Save item
    @PostMapping("/additem")
    public ResponseEntity<?> saveItem(@RequestBody InventoryDTO inventoryDTO){
        try {
            // Validate input
            if (inventoryDTO.getItemId() == null) {
                return ResponseEntity.badRequest().body("Item ID is required");
            }
            if (inventoryDTO.getProductId() == null) {
                return ResponseEntity.badRequest().body("Product ID is required");
            }
            if (inventoryDTO.getItemName() == null || inventoryDTO.getItemName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Item name is required");
            }

            InventoryDTO savedItem = inventoryService.saveItem(inventoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving item: " + e.getMessage());
        }
    }

    // Update item
    @PutMapping("/updateitem")
    public ResponseEntity<?> updateItem(@RequestBody InventoryDTO inventoryDTO){
        try {
            if (inventoryDTO.getItemId() == null) {
                return ResponseEntity.badRequest().body("Item ID is required for update");
            }

            InventoryDTO updatedItem = inventoryService.updateItem(inventoryDTO);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating item: " + e.getMessage());
        }
    }

    // Delete item
    @DeleteMapping("/deleteitem/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable int id){
        try {
            String result = inventoryService.deleteItem(id);
            if (result.contains("Successfully")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting item: " + e.getMessage());
        }
    }

    // Update availability
    @PatchMapping("/item/{itemId}/availability/{status}")
    public ResponseEntity<String> updateAvailability(@PathVariable Integer itemId, @PathVariable Integer status) {
        if (status != 0 && status != 1) {
            return ResponseEntity.badRequest()
                    .body("Invalid status. Use 0 for unavailable or 1 for available.");
        }

        try {
            String result = inventoryService.updateAvailability(itemId, status);
            if (result.contains("successfully")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating availability: " + e.getMessage());
        }
    }

    // Get available items only
    @GetMapping("/items/available")
    public ResponseEntity<List<InventoryDTO>> getAvailableItems() {
        try {
            List<InventoryDTO> items = inventoryService.getAvailableItems();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get unavailable items only
    @GetMapping("/items/unavailable")
    public ResponseEntity<List<InventoryDTO>> getUnavailableItems() {
        try {
            List<InventoryDTO> items = inventoryService.getUnavailableItems();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // NEW: Get items by product ID
    @GetMapping("/product/{productId}/items")
    public ResponseEntity<List<InventoryDTO>> getItemsByProductId(@PathVariable Integer productId) {
        try {
            List<InventoryDTO> items = inventoryService.getItemsByProductId(productId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // NEW: Check if product exists
    @GetMapping("/product/{productId}/exists")
    public ResponseEntity<Boolean> productExists(@PathVariable Integer productId) {
        try {
            boolean exists = inventoryService.productExists(productId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}