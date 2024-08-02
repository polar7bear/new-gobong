package com.sns.gobong.util.api;

public class ApiResponse<T> {

    private boolean success;
    private ApiError apiError;
    private T data;
    private long cost = -1;

    private boolean checkedContext() {
        return ResponseContext.requestAt.get() != null;
    }

    public ApiResponse(T data) {
        this.data = data;
        if (checkedContext()) {
            this.cost = System.currentTimeMillis() - ResponseContext.requestAt.get();
        }
    }

    public ApiResponse(boolean success, ApiError apiError) {
        this.success = success;
        this.apiError = apiError;
        if (checkedContext()) {
            this.cost = System.currentTimeMillis() - ResponseContext.requestAt.get();
        }
    }

    public ApiResponse(boolean success, ApiError apiError, T data) {
        this.success = success;
        this.apiError = apiError;
        this.data = data;
        if (checkedContext()) {
            this.cost = System.currentTimeMillis() - ResponseContext.requestAt.get();
        }
    }
}
