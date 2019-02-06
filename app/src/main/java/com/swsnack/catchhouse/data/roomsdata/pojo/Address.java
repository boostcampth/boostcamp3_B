package com.swsnack.catchhouse.data.roomsdata.pojo;

public class Address {
    private String name;
    private String address;
    private double longitude;
    private double latitude;

    // FIXME 이 생성자는 불필요합니다.
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

    // FIXME pojo클래스에서 setter가 필요한 경우는 없을것 같습니다. 이 클래스뿐만 아니라 다른 코드도 불필요한 setter는 모두 제거해주세요
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
