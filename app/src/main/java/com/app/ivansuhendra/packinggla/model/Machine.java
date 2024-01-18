package com.app.ivansuhendra.packinggla.model;

import com.google.gson.annotations.SerializedName;

public class Machine {
    private int id;
    private String model;
    @SerializedName("serial_number")
    private String serialNumber;

    @SerializedName("machine_type")
    private MachineType machineType;

    private Brand brand;

    public Machine(String model, String serialNumber) {
        this.model = model;
        this.serialNumber = serialNumber;
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public MachineType getMachineType() {
        return machineType;
    }

    public Brand getBrand() {
        return brand;
    }
}
