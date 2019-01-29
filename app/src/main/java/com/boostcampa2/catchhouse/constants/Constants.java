package com.boostcampa2.catchhouse.constants;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.boostcampa2.catchhouse.constants.Constants.UserStatus.SIGN_IN_SUCCESS;
import static com.boostcampa2.catchhouse.constants.Constants.UserStatus.SIGN_UP_SUCCESS;

public class Constants {

    public static final int GALLERY = 1003;
    public static final int GOOGLE_SIGN_IN = 1000;

    //gender
    public static final String MALE = "male";
    public static final String FEMALE = "female";

    //facebook readPermissions
    public static final String E_MAIL = "email";
    public static final String PUBLIC_PROFILE = "public_profile";


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({SIGN_IN_SUCCESS, SIGN_UP_SUCCESS})
    public @interface UserStatus {
        String SIGN_IN_SUCCESS = "singInSuccess";
        String SIGN_UP_SUCCESS = "signUpSuccess";
    }
}
