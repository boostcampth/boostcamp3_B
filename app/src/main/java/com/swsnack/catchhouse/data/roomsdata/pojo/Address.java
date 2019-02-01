package com.swsnack.catchhouse.data.roomsdata.pojo;

public class Address {
    private String name;
    private String address;
    private double longitude;
    private double latitude;

    public Address() {
        this.name = "";
        this.address = "";
        this.longitude = 0;
        this.latitude = 0;
    }

    public Address(String name, String address, double longitude, double latitude) {
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}