package com.example.product.dto;

public class ProductDTO {

    private int id;
    private int productId;
    private String productName;
    private String description;
    private boolean active;

    // Default constructor
    public ProductDTO() {
    }

    // All-args constructor
    public ProductDTO(int id, int productId, String productName, String description, boolean active) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.active = active;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
