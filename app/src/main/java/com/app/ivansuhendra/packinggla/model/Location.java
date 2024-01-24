package com.app.ivansuhendra.packinggla.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    private int id;
    @SerializedName("location_name")
    private String name;

    public Location(int id, String name) {
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

    @Override
    public String toString() {
        return name;
    }
}
