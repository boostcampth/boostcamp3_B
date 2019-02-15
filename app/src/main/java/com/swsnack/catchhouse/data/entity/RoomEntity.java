package com.swsnack.catchhouse.data.entity;

import com.swsnack.catchhouse.data.db.room.local.TypeConverter;
import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import static com.swsnack.catchhouse.Constant.DatabaseKey.ROOM_TABLE;

@Entity(tableName = ROOM_TABLE,
        indices = {@Index(value = {"room_uid"})})
public class RoomEntity {

    @PrimaryKey
    @ColumnInfo(name = "room_uid")
    @NonNull
    private String roomUid;
    private String price;
    private String from;
    private String to;
    private String title;
    private String content;
    @TypeConverters({TypeConverter.class})
    private List<String> images;
    @ColumnInfo(name = "writer_uuid")
    private String uuid;
    private String address;
    private String addressName;
    private String size;
    private boolean optionStandard;
    private boolean optionGender;
    private boolean optionPet;
    private boolean optionSmoking;

    public RoomEntity(@NonNull String roomUid,
                      String price,
                      String from,
                      String to,
                      String title,
                      String content,
                      List<String> images,
                      String uuid,
                      String address,
                      String addressName,
                      String size,
                      boolean optionStandard,
                      boolean optionGender,
                      boolean optionPet,
                      boolean optionSmoking) {

        this.roomUid = roomUid;
        this.price = price;
        this.from = from;
        this.to = to;
        this.title = title;
        this.content = content;
        this.images = images;
        this.uuid = uuid;
        this.address = address;
        this.addressName = addressName;
        this.size = size;
        this.optionStandard = optionStandard;
        this.optionGender = optionGender;
        this.optionPet = optionPet;
        this.optionSmoking = optionSmoking;
    }

    @NonNull
    public String getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(@NonNull String roomUid) {
        this.roomUid = roomUid;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public static RoomEntity toRoomEntity(Room room) {
        return new RoomEntity(room.getKey(),
                room.getPrice(),
                room.getFrom(),
                room.getTo(),
                room.getTitle(),
                room.getContent(),
                room.getImages(),
                room.getUuid(),
                room.getAddress(),
                room.getAddressName(),
                room.getSize(),
                room.isOptionStandard(),
                room.isOptionGender(),
                room.isOptionPet(),
                room.isOptionSmoking());
    }
}
