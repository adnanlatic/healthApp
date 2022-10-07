package com.adnan.healthAppMaster;

public class Alerts {
    private String sensorType, message;

    public Alerts() {
    }

    public Alerts(String sensorType, String message) {
        this.sensorType = sensorType;
        this.message = message;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
