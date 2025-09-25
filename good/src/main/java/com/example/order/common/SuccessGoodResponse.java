package com.example.order.common;

import lombok.Getter;

@Getter
public class SuccessGoodResponse implements GoodResponse {
    private final String message;
    private final Object data; // Can hold either GoodDTO or String message

    // Constructor for success with data (like GoodDTO)
    public SuccessGoodResponse(Object data) {
        this.message = "Success";
        this.data = data;
    }

    // Constructor for success with message only
    public SuccessGoodResponse(String message) {
        this.message = message;
        this.data = null;
    }

    // Constructor for success with both message and data
    public SuccessGoodResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}