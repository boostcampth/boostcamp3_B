package com.boostcampa2.catchhouse.data.userdata.pojo;

public class User {

    private String eMail;
    private String password;
    private String nickName;
    private String gender;
    private String profile;
    private String myRoom;

    public User() {

    }

    public User(String eMail, String password, String nickName, String gender, String profile, String myRoom) {
        this.eMail = eMail;
        this.password = password;
        this.nickName = nickName;
        this.gender = gender;
        this.profile = profile;
        this.myRoom = myRoom;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
