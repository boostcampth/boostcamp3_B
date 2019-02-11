package com.swsnack.catchhouse.data.userdata.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String eMail;
    private String nickName;
    private String gender;
    private String profile;
    private String myRoom;

    public User() {
    }

    public User(String name) {
        this(name, null);
    }

    public User(String name, String gender) {
        this(null, name, gender);
    }

    public User(String eMail, String nickName, String gender) {
        this.eMail = eMail;
        this.nickName = nickName;
        this.gender = gender;
    }

    protected User(Parcel in) {
        eMail = in.readString();
        nickName = in.readString();
        gender = in.readString();
        profile = in.readString();
        myRoom = in.readString();
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
        dest.writeString(eMail);
        dest.writeString(nickName);
        dest.writeString(gender);
        dest.writeString(profile);
        dest.writeString(myRoom);
    }

    public String getEMail() {
        return eMail;
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

    public String getMyRoom() {
        return myRoom;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
