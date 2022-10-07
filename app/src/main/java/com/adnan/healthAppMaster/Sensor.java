package com.adnan.healthAppMaster;

public class Sensor {
    private String position, value;

    public Sensor() {
    }

    public Sensor(String position, String value) {
        this.position = position;
        this.value = value;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
