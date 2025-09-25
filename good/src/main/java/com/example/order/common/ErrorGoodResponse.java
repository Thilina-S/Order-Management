package com.example.order.common;

import lombok.Getter;

@Getter
public class ErrorGoodResponse implements GoodResponse {
    private final String errorMessage;
    private final String errorCode; // Optional: add error code for better handling

    public ErrorGoodResponse(String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = "ERROR";
    }

    public ErrorGoodResponse(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}