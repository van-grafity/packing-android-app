package com.app.ivansuhendra.packinggla.model;

import com.google.gson.annotations.SerializedName;

public class MachineType {
    @SerializedName("machine_type")
    private String name;

    public String getName() {
        return name;
    }
}
