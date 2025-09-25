package com.example.inventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "item_id")
    private Integer itemId; // Removed @GeneratedValue

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "item_name")
    private String itemName;

    private Integer quantity;
    private Double price;

    @Column(name = "availability", nullable = false)
    private Integer availability = 1;

    // Constructors
    public Inventory() {}

    public Inventory(Integer itemId, Integer productId, String itemName, Integer quantity, Double price, Integer availability) {
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

    @Override
    public String toString() {
        return "Inventory{itemId=" + itemId + ", productId=" + productId +
                ", itemName='" + itemName + "', quantity=" + quantity +
                ", price=" + price + ", availability=" + availability + "}";
    }
}