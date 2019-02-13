package com.swsnack.catchhouse.data.pojo;

import com.swsnack.catchhouse.data.pojo.Address;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Room implements Parcelable {
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
    private String uuid;
    /**
     * 주소
     **/
    private String address;
    /**
     * 주소 명
     **/
    private String addressName;
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

    public Room() {

    }

    public Room(String price,
                String from,
                String to,
                String title,
                String content,
                List<String> images,
                String UUID,
                String size,
                String address,
                String addressName,
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
        this.uuid = UUID;
        this.size = size;
        this.address = address;
        this.addressName = addressName;
        this.optionStandard = optionStandard;
        this.optionGender = optionGender;
        this.optionPet = optionPet;
        this.optionSmoking = optionSmoking;
    }

    private Room(Parcel in) {
        images = new ArrayList<>();

        price = in.readString();
        from = in.readString();
        to = in.readString();
        title = in.readString();
        content = in.readString();
        in.readStringList(images);
        uuid = in.readString();
        size = in.readString();
        address = in.readString();
        addressName = in.readString();
        optionStandard = in.readByte() != 0;
        optionGender = in.readByte() != 0;
        optionPet = in.readByte() != 0;
        optionSmoking = in.readByte() != 0;
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
        return uuid;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getSize() {
        return size;
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


    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(price);
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeStringList(images);
        dest.writeString(uuid);
        dest.writeString(size);
        dest.writeString(address);
        dest.writeString(addressName);
        dest.writeByte((byte) (optionStandard ? 1 : 0));
        dest.writeByte((byte) (optionGender ? 1 : 0));
        dest.writeByte((byte) (optionPet ? 1 : 0));
        dest.writeByte((byte) (optionSmoking ? 1 : 0));
    }
}
