package com.swsnack.catchhouse.data.pojo;

import com.swsnack.catchhouse.data.pojo.Address;

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
    private Address address;

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
                Address address,
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

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<String> getImages() {
        return images;
    }

    public String getUUID() {
        return UUID;
    }

    public Address getAddress() {
        return address;
    }

    public boolean isOptionStandard() {
        return optionStandard;
    }

    public boolean isOptionGender() {
        return optionGender;
    }

    public boolean isOptionPet() {
        return optionPet;
    }

    public boolean isOptionSmoking() {
        return optionSmoking;
    }

}
