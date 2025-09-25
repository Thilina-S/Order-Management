package com.example.inventory.service;

import com.example.inventory.dto.InventoryDTO;
import com.example.inventory.model.Inventory;
import com.example.inventory.repo.InventoryRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {
    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    // Get all items
    @Transactional(readOnly = true)
    public List<InventoryDTO> getAllItems(){
        List<Inventory> inventoryList = inventoryRepo.findAll();
        return modelMapper.map(inventoryList, new TypeToken<List<InventoryDTO>>(){}.getType());
    }

    // Get one item
    @Transactional(readOnly = true)
    public InventoryDTO getItemById(Integer itemId) {
        Optional<Inventory> inventoryOpt = inventoryRepo.findById(itemId);

        if (inventoryOpt.isPresent()) {
            return modelMapper.map(inventoryOpt.get(), InventoryDTO.class);
        } else {
            throw new RuntimeException("Item not found with id: " + itemId);
        }
    }

    // Save item - ENHANCED for manual IDs
    @Transactional
    public InventoryDTO saveItem(InventoryDTO inventoryDTO){
        // Validate required fields
        if (inventoryDTO.getItemId() == null) {
            throw new RuntimeException("Item ID is required");
        }

        if (inventoryDTO.getProductId() == null) {
            throw new RuntimeException("Product ID is required");
        }

        // Check if item already exists
        if (inventoryRepo.existsById(inventoryDTO.getItemId())) {
            throw new RuntimeException("Item with ID " + inventoryDTO.getItemId() + " already exists");
        }

        // Set default availability if not provided
        if (inventoryDTO.getAvailability() == null) {
            inventoryDTO.setAvailability(1);
        }

        // Convert DTO to Entity
        Inventory inventory = modelMapper.map(inventoryDTO, Inventory.class);

        // Save and get the persisted entity
        Inventory savedInventory = inventoryRepo.save(inventory);

        // Convert back to DTO and return
        return modelMapper.map(savedInventory, InventoryDTO.class);
    }

    // Update item - ENHANCED for manual IDs
    @Transactional
    public InventoryDTO updateItem(InventoryDTO inventoryDTO){
        if (inventoryDTO.getItemId() == null) {
            throw new RuntimeException("Item ID is required for update operation");
        }

        // Check if item exists
        Optional<Inventory> existingItem = inventoryRepo.findById(inventoryDTO.getItemId());
        if (!existingItem.isPresent()) {
            throw new RuntimeException("Item not found with id: " + inventoryDTO.getItemId());
        }

        // Update existing item
        Inventory inventory = existingItem.get();
        inventory.setProductId(inventoryDTO.getProductId());
        inventory.setItemName(inventoryDTO.getItemName());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setPrice(inventoryDTO.getPrice());
        inventory.setAvailability(inventoryDTO.getAvailability());

        Inventory updatedInventory = inventoryRepo.save(inventory);
        return modelMapper.map(updatedInventory, InventoryDTO.class);
    }

    // Delete item
    @Transactional
    public String deleteItem(int id){
        if(inventoryRepo.existsById(id)){
            inventoryRepo.deleteById(id);
            return "Item Deleted Successfully !!!";
        } else {
            return "Item Not Found !!!";
        }
    }

    // Update availability status
    @Transactional
    public String updateAvailability(Integer itemId, Integer availability) {
        Optional<Inventory> inventoryOpt = inventoryRepo.findById(itemId);

        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            inventory.setAvailability(availability);
            inventoryRepo.save(inventory);
            return "Availability updated successfully! Status: " + (availability == 1 ? "Available" : "Unavailable");
        } else {
            return "Item not found with id: " + itemId;
        }
    }

    // Get only available items
    @Transactional(readOnly = true)
    public List<InventoryDTO> getAvailableItems() {
        List<Inventory> availableItems = inventoryRepo.findByAvailability(1);
        return modelMapper.map(availableItems, new TypeToken<List<InventoryDTO>>(){}.getType());
    }

    // Get unavailable items
    @Transactional(readOnly = true)
    public List<InventoryDTO> getUnavailableItems() {
        List<Inventory> unavailableItems = inventoryRepo.findByAvailability(0);
        return modelMapper.map(unavailableItems, new TypeToken<List<InventoryDTO>>(){}.getType());
    }

    // NEW: Check if product exists
    @Transactional(readOnly = true)
    public boolean productExists(Integer productId) {
        return inventoryRepo.findByProductId(productId).size() > 0;
    }

    // NEW: Get items by product ID
    @Transactional(readOnly = true)
    public List<InventoryDTO> getItemsByProductId(Integer productId) {
        List<Inventory> items = inventoryRepo.findByProductId(productId);
        return modelMapper.map(items, new TypeToken<List<InventoryDTO>>(){}.getType());
    }
}