package com.swsnack.catchhouse.constants;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.DELETED_USER;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.DUPLICATE_EMAIL;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.FAILED_LOAD_IMAGE;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.IN_SUFFICIENT_INFO;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.MISMATCH_USER_INFO;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.NOT_SIGNED_USER;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.SHORT_PASSWORD;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.SIGN_IN_EXCEPTION;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.SIGN_UP_EXCEPTION;
import static com.swsnack.catchhouse.constants.Constants.FacebookData.GENDER;
import static com.swsnack.catchhouse.constants.Constants.FacebookData.KEY;
import static com.swsnack.catchhouse.constants.Constants.FacebookData.NAME;
import static com.swsnack.catchhouse.constants.Constants.FacebookData.VALUE;
import static com.swsnack.catchhouse.constants.Constants.FirebaseKey.DB_USER;
import static com.swsnack.catchhouse.constants.Constants.FirebaseKey.STORAGE_PROFILE;
import static com.swsnack.catchhouse.constants.Constants.Gender.FEMALE;
import static com.swsnack.catchhouse.constants.Constants.Gender.MALE;
import static com.swsnack.catchhouse.constants.Constants.UserStatus.SIGN_IN_SUCCESS;
import static com.swsnack.catchhouse.constants.Constants.UserStatus.SIGN_UP_SUCCESS;

public class Constants {

    public static final int GALLERY = 1003;
    public static final int GOOGLE_SIGN_IN = 1000;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({SIGN_IN_SUCCESS, SIGN_UP_SUCCESS})
    public @interface UserStatus {
        String SIGN_IN_SUCCESS = "singInSuccess";
        String SIGN_UP_SUCCESS = "signUpSuccess";
        String DELETE_USER_SUCCESS = "deleteSuccess";
    }

    //magic constant
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({NAME, GENDER, KEY, VALUE})
    public @interface FacebookData {
        String NAME = "name";
        String GENDER = "gender";
        String KEY = "fields";
        String VALUE = "name,gender";
        String E_MAIL = "email";
        String PROFILE = "public_profile";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({MALE, FEMALE})
    public @interface Gender {
        String MALE = "male";
        String FEMALE = "female";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({DB_USER, STORAGE_PROFILE})
    public @interface FirebaseKey {
        String DB_USER = "users";
        String STORAGE_PROFILE = "profile";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({IN_SUFFICIENT_INFO, SHORT_PASSWORD, DUPLICATE_EMAIL, MISMATCH_USER_INFO,
            FAILED_LOAD_IMAGE, SIGN_IN_EXCEPTION, SIGN_UP_EXCEPTION, NOT_SIGNED_USER, DELETED_USER})
    public @interface ExceptionReason {
        String IN_SUFFICIENT_INFO = "InSufficientInfo";
        String SHORT_PASSWORD = "shortPassword";
        String DUPLICATE_EMAIL = "duplicateEmail";
        String NOT_SIGNED_USER = "notSignedUser";
        String MISMATCH_USER_INFO = "mismatchUserInfo";
        String FAILED_LOAD_IMAGE = "failedLoadImage";
        String SIGN_IN_EXCEPTION = "signInException";
        String SIGN_UP_EXCEPTION = "signUpException";
        String DELETED_USER = "deletedUser";
    }
}
