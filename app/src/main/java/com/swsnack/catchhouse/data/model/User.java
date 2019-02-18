package com.swsnack.catchhouse.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class User implements Parcelable {

    @Nullable
    private String uuid;
    private String email;
    private String nickName;
    private String gender;
    private String profile;
    private String myRoom;
    //1일때 회원가입한 유저, 0일때 탈퇴한 유저
    private int isSigned;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public User(String name) {
        this(name, null);
    }

    public User(String name, String gender) {
        this(null, name, gender);
    }

    public User(String eMail, String nickName, String gender) {
        this.email = eMail;
        this.nickName = nickName;
        this.gender = gender;
        this.isSigned = 1;
    }

    protected User(Parcel in) {
        email = in.readString();
        nickName = in.readString();
        gender = in.readString();
        profile = in.readString();
        myRoom = in.readString();
        isSigned = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(nickName);
        dest.writeString(gender);
        dest.writeString(profile);
        dest.writeString(myRoom);
        dest.writeInt(isSigned);
    }

    @Nullable
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public String getNickName() {
        return nickName;
    }

    public String getGender() {
        return gender;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(int isSigned) {
        this.isSigned = isSigned;
    }
}
