package com.app.ivansuhendra.packinggla.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Rack {
    private int id;
    @SerializedName("serial_number")
    private String name;
    private String description;
    private String area;
    private String level;

    public Rack(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
