package com.example.base.dto;

public class GoodEventDTO {
    private String message;
    private String status;

    public GoodEventDTO() {}

    public GoodEventDTO(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
