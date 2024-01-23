package com.app.ivansuhendra.packinggla.model;

public class APIResponse {
    private String status;
    private String message;
    private APIModels data;


    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public APIModels getData() {
        return data;
    }
}
