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
