package com.swsnack.catchhouse.data.model;

public class Address {
    private String name;
    private String address;
    private double longitude;
    private double latitude;

    public Address() {

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

    public String getAddress() {
        return address;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
