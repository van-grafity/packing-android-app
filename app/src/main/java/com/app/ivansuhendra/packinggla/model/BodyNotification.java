package com.app.ivansuhendra.packinggla.model;

public class BodyNotification {
    private String location;
    private String notification;
    private String status;
    private String subType;
    private String type;
    private String typeReferenceId;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeReferenceId() {
        return typeReferenceId;
    }

    public void setTypeReferenceId(String typeReferenceId) {
        this.typeReferenceId = typeReferenceId;
    }
}