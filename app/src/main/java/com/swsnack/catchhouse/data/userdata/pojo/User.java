package com.swsnack.catchhouse.data.userdata.pojo;

public class User {

    private String eMail;
    private String nickName;
    private String gender;
    private String profile;
    private String myRoom;

    public User() {

    }

    public User(String name) {
        this.nickName = name;
    }

    public User(String name, String gender) {
        this.nickName = name;
        this.gender = gender;
    }

    public User(String eMail, String nickName, String gender) {
        this.eMail = eMail;
        this.nickName = nickName;
        this.gender = gender;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getMyRoom() {
        return myRoom;
    }

    public void setMyRoom(String myRoom) {
        this.myRoom = myRoom;
    }
}
