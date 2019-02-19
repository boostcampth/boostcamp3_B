package com.swsnack.catchhouse.data.model;


public class RoomCard {

    private String image;
    private String title;
    private String content;
    private String address;
    private String addressName;
    private String totalPrice;
    private String size;
    private String uuid;

    public RoomCard(String image, String title, String content, String address, String addressName, String totalPrice, String size, String uuid) {
        this.image = image;
        this.title = title;
        this.content = content;
        this.address = address;
        this.addressName = addressName;
        this.totalPrice = totalPrice;
        this.size = size;
        this.uuid = uuid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
