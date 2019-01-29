package com.boostcampa2.catchhouse.data.roomsdata.pojo;

import java.util.List;

public class Room {
    /** 가격(1박) **/
    private String price;
    /** 시작 날짜 **/
    private String from;
    /** 종료 날짜 **/
    private String to;
    /** 제목 **/
    private String title;
    /** 내용 **/
    private String content;
    /** 이미지들 링크 **/
    private List<String> images;
    /** 작성자 **/
    private String UUID;
    /** 주소 **/
    private String address;

    /* 옵션
     * 선택하지 않을경우 false */
    /** 기본 옵션 **/
    private boolean optionStandard;
    /** 성별 옵션 **/
    private boolean optionGender;
    /** 반려동물 옵션 **/
    private boolean optionPet;
    /** 흡연 옵션 **/
    private boolean optionSmoking;

    public Room(String price,
                String from,
                String to,
                String title,
                String content,
                List<String> images,
                String UUID,
                String address,
                boolean optionStandard,
                boolean optionGender,
                boolean optionPet,
                boolean optionSmoking) {
        this.price = price;
        this.from = from;
        this.to = to;
        this.title = title;
        this.content = content;
        this.images = images;
        this.UUID = UUID;
        this.address = address;
        this.optionStandard = optionStandard;
        this.optionGender = optionGender;
        this.optionPet = optionPet;
        this.optionSmoking = optionSmoking;
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
}
