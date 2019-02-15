package com.swsnack.catchhouse.data.pojo;

import android.graphics.Bitmap;

import java.util.List;

public class RoomData {

    private String key;
    /**
     * 가격(1박)
     **/
    private String price;
    /**
     * 시작 날짜
     **/
    private String from;
    /**
     * 종료 날짜
     **/
    private String to;
    /**
     * 제목
     **/
    private String title;
    /**
     * 내용
     **/
    private String content;
    /**
     * 이미지들 링크
     **/
    private List<String> images;
    /**
     * 작성자
     **/
    private String UUID;
    /**
     * 주소
     **/
    private String address;
    /**
     * 주소 명
     **/
    private String addressName;
    /**
     * 위도
     **/
    private double latitude;
    /**
     * 경도
     **/
    private double longitude;
    /**
     * 방 사이즈
     */
    private String size;

    /* 옵션 선택하지 않을경우 false */
    /**
     * 기본 옵션
     **/
    private boolean optionStandard;
    /**
     * 성별 옵션
     **/
    private boolean optionGender;
    /**
     * 반려동물 옵션
     **/
    private boolean optionPet;
    /**
     * 흡연 옵션
     **/
    private boolean optionSmoking;

    private Bitmap image;

    public RoomData(String key, String price, String from, String to, String title, String content, List<String> images, String UUID, String address, String addressName, double latitude, double longitude, String size, boolean optionStandard, boolean optionGender, boolean optionPet, boolean optionSmoking, Bitmap image) {
        this.key = key;
        this.price = price;
        this.from = from;
        this.to = to;
        this.title = title;
        this.content = content;
        this.images = images;
        this.UUID = UUID;
        this.address = address;
        this.addressName = addressName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.size = size;
        this.optionStandard = optionStandard;
        this.optionGender = optionGender;
        this.optionPet = optionPet;
        this.optionSmoking = optionSmoking;
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isOptionStandard() {
        return optionStandard;
    }

    public void setOptionStandard(boolean optionStandard) {
        this.optionStandard = optionStandard;
    }

    public boolean isOptionGender() {
        return optionGender;
    }

    public void setOptionGender(boolean optionGender) {
        this.optionGender = optionGender;
    }

    public boolean isOptionPet() {
        return optionPet;
    }

    public void setOptionPet(boolean optionPet) {
        this.optionPet = optionPet;
    }

    public boolean isOptionSmoking() {
        return optionSmoking;
    }

    public void setOptionSmoking(boolean optionSmoking) {
        this.optionSmoking = optionSmoking;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
