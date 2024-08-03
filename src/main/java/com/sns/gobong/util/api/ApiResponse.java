package com.sns.gobong.util.api;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private ApiError apiError;
    private T data;
    private String ms = "";

    private boolean checkedContext() {
        return ResponseContext.requestAt.get() != null;
    }

    private void calculateCost() {
        if (checkedContext()) {
            long cost = System.currentTimeMillis() - ResponseContext.requestAt.get();
            this.ms = cost + " ms";
        }
    }

    public ApiResponse(T data) {
        this.data = data;
        calculateCost();
    }

    public ApiResponse(String message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
        calculateCost();
    }

    public ApiResponse(String message, ApiError apiError) {
        this.success = false;
        this.message = message;
        this.apiError = apiError;
        calculateCost();
    }

    public ApiResponse(String message, ApiError apiError, T data) {
        this.success = false;
        this.message = message;
        this.apiError = apiError;
        this.data = data;
        calculateCost();
    }
}
