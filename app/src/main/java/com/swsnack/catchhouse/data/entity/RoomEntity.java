package com.swsnack.catchhouse.data.entity;

import java.util.List;

import androidx.annotation.NonNull;

public interface RoomEntity {

    String getRoomUid();

    void setRoomUid(@NonNull String roomUid);

    String getFirebaseUuid();

    void setFirebaseUuid(String firebaseUuid);

    String getPrice();

    void setPrice(String price);

    String getFrom();

    void setFrom(String from);

    String getTo();

    void setTo(String to);

    String getTitle();

    void setTitle(String title);

    String getContent();

    void setContent(String content);

    List<String> getImages();

    void setImages(List<String> images);

    String getUuid();

    void setUuid(String uuid);

    String getAddress();

    void setAddress(String address);

    String getAddressName();

    void setAddressName(String addressName);

    String getSize();

    void setSize(String size);

    boolean isOptionStandard();

    void setOptionStandard(boolean optionStandard);

    boolean isOptionGender();

    void setOptionGender(boolean optionGender);

    boolean isOptionPet();

    void setOptionPet(boolean optionPet);

    boolean isOptionSmoking();

    void setOptionSmoking(boolean optionSmoking);

    double getLatitude();

    void setLatitude(double latitude);

    double getLongitude();

    void setLongitude(double longitude);

    boolean isDeleted();

    void setDeleted(boolean deleted);
}
