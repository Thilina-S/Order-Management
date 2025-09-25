package com.example.inventory.dto;

public class InventoryDTO {
    private Integer itemId;
    private Integer productId;
    private String itemName;
    private Integer quantity;
    private Double price;
    private Integer availability;

    // Constructors
    public InventoryDTO() {}

    public InventoryDTO(Integer itemId, Integer productId, String itemName, Integer quantity, Double price, Integer availability) {
        this.itemId = itemId;
        this.productId = productId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.availability = availability;
    }

    // Getters and Setters
    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getAvailability() { return availability; }
    public void setAvailability(Integer availability) { this.availability = availability; }

    public boolean isAvailable() {
        return availability != null && availability == 1;
    }
}