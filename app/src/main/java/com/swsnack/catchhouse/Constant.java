package com.swsnack.catchhouse;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.swsnack.catchhouse.Constant.ExceptionReason.DELETED_USER;
import static com.swsnack.catchhouse.Constant.ExceptionReason.DELETE_EXCEPTION;
import static com.swsnack.catchhouse.Constant.ExceptionReason.DUPLICATE;
import static com.swsnack.catchhouse.Constant.ExceptionReason.DUPLICATE_EMAIL;
import static com.swsnack.catchhouse.Constant.ExceptionReason.DUPLICATE_NICK_NAME;
import static com.swsnack.catchhouse.Constant.ExceptionReason.FAILED_LOAD_IMAGE;
import static com.swsnack.catchhouse.Constant.ExceptionReason.FAILED_UPDATE;
import static com.swsnack.catchhouse.Constant.ExceptionReason.IN_SUFFICIENT_INFO;
import static com.swsnack.catchhouse.Constant.ExceptionReason.MISMATCH_USER_INFO;
import static com.swsnack.catchhouse.Constant.ExceptionReason.NOT_SIGNED_USER;
import static com.swsnack.catchhouse.Constant.ExceptionReason.SAME_NICK_NAME;
import static com.swsnack.catchhouse.Constant.ExceptionReason.SHORT_PASSWORD;
import static com.swsnack.catchhouse.Constant.ExceptionReason.SIGN_IN_EXCEPTION;
import static com.swsnack.catchhouse.Constant.ExceptionReason.SIGN_UP_EXCEPTION;
import static com.swsnack.catchhouse.Constant.FacebookData.GENDER;
import static com.swsnack.catchhouse.Constant.FacebookData.KEY;
import static com.swsnack.catchhouse.Constant.FacebookData.NAME;
import static com.swsnack.catchhouse.Constant.FacebookData.VALUE;
import static com.swsnack.catchhouse.Constant.FirebaseKey.CHATTING;
import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_USER;
import static com.swsnack.catchhouse.Constant.FirebaseKey.MESSAGE;
import static com.swsnack.catchhouse.Constant.FirebaseKey.NICK_NAME;
import static com.swsnack.catchhouse.Constant.FirebaseKey.STORAGE_PROFILE;
import static com.swsnack.catchhouse.Constant.FirebaseKey.STORAGE_ROOM_IMAGE;
import static com.swsnack.catchhouse.Constant.FirebaseKey.UUID;
import static com.swsnack.catchhouse.Constant.Gender.FEMALE;
import static com.swsnack.catchhouse.Constant.Gender.MALE;
import static com.swsnack.catchhouse.Constant.ParcelableData.CHATTING_DATA;
import static com.swsnack.catchhouse.Constant.ParcelableData.USER_DATA;
import static com.swsnack.catchhouse.Constant.PostException.EMPTY_PRICE_FIELD;
import static com.swsnack.catchhouse.Constant.PostException.EMPTY_ROOM_IMAGE;
import static com.swsnack.catchhouse.Constant.PostException.EMPTY_TITLE_FIELD;
import static com.swsnack.catchhouse.Constant.PostException.NETWORK_ERROR;
import static com.swsnack.catchhouse.Constant.PostException.NOT_SELECTION_DATE;
import static com.swsnack.catchhouse.Constant.SignInMethod.E_MAIL;
import static com.swsnack.catchhouse.Constant.SignInMethod.FACEBOOK;
import static com.swsnack.catchhouse.Constant.SignInMethod.GOOGLE;
import static com.swsnack.catchhouse.Constant.SuccessKey.DELETE_USER_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.SEND_MESSAGE_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.SIGN_IN_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.SIGN_OUT_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.SIGN_UP_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.UPDATE_NICK_NAME_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.UPDATE_PASSWORD_SUCCESS;
import static com.swsnack.catchhouse.Constant.SuccessKey.UPDATE_PROFILE_SUCCESS;

public class Constant {

    public static final int GALLERY = 1003;
    public static final int GOOGLE_SIGN_IN = 1000;
    public static final int FILTER = 1005;
    public static final String INTENT_FILTER = "1006";
    public static final String INTENT_ROOM = "1007";
    public static final String INTENT_LON = "1008";
    public static final String INTENT_LAT = "1009";

    public static final String MSG_ERROR_GET_ADDRESS = "주소를 찾을 수 없습니다.";
    public static final String DEFAULT_DATE_STRING = "YYYY-MM-DD";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({SIGN_IN_SUCCESS, SIGN_UP_SUCCESS, DELETE_USER_SUCCESS, UPDATE_NICK_NAME_SUCCESS,
            UPDATE_PASSWORD_SUCCESS, UPDATE_PROFILE_SUCCESS, SEND_MESSAGE_SUCCESS, SIGN_OUT_SUCCESS})
    public @interface SuccessKey {
        String SIGN_IN_SUCCESS = "singInSuccess";
        String SIGN_UP_SUCCESS = "signUpSuccess";
        String SIGN_OUT_SUCCESS = "signOutSuccess";
        String DELETE_USER_SUCCESS = "deleteSuccess";
        String UPDATE_NICK_NAME_SUCCESS = "updateNickNameSuccess";
        String UPDATE_PASSWORD_SUCCESS = "updatePasswordSuccess";
        String UPDATE_PROFILE_SUCCESS = "updateProfileSuccess";
        String SEND_MESSAGE_SUCCESS = "sendMessageSuccess";
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
    @StringDef({DB_USER, STORAGE_PROFILE, STORAGE_ROOM_IMAGE, NICK_NAME, CHATTING,
            MESSAGE, UUID})
    public @interface FirebaseKey {
        String DB_USER = "users";
        String DB_ROOM = "rooms";
        String DB_LOCATION = "location";
        String STORAGE_PROFILE = "profile";
        String STORAGE_ROOM_IMAGE = "roomImage";
        String NICK_NAME = "nickName";
        String CHATTING = "chatting";
        String MESSAGE = "messages";
        String UUID = "uuid";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({IN_SUFFICIENT_INFO, SHORT_PASSWORD, DUPLICATE_EMAIL, MISMATCH_USER_INFO,
            FAILED_LOAD_IMAGE, SIGN_IN_EXCEPTION, SIGN_UP_EXCEPTION, NOT_SIGNED_USER, DELETED_USER,
            SAME_NICK_NAME, DUPLICATE_NICK_NAME, DELETE_EXCEPTION, DUPLICATE, FAILED_UPDATE})
    public @interface ExceptionReason {
        String IN_SUFFICIENT_INFO = "InSufficientInfo";
        String SHORT_PASSWORD = "shortPassword";
        String DUPLICATE_EMAIL = "duplicateEmail";
        String NOT_SIGNED_USER = "notSignedUser";
        String MISMATCH_USER_INFO = "mismatchUserInfo";
        String FAILED_LOAD_IMAGE = "failedLoadImage";
        String SIGN_IN_EXCEPTION = "signInException";
        String SIGN_UP_EXCEPTION = "signUpException";
        String DELETE_EXCEPTION = "deleteException";
        String DELETED_USER = "deletedUser";
        String SAME_NICK_NAME = "sameNickName";
        String DUPLICATE = "duplicate";
        String DUPLICATE_NICK_NAME = "duplicateNickName";
        String FAILED_UPDATE = "failedUpdate";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({FACEBOOK, GOOGLE, E_MAIL})
    public @interface SignInMethod {
        String FACEBOOK = "facebook.com";
        String GOOGLE = "google.com";
        String E_MAIL = "password";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({EMPTY_ROOM_IMAGE, EMPTY_PRICE_FIELD, NOT_SELECTION_DATE, EMPTY_TITLE_FIELD,
            NETWORK_ERROR})
    public @interface PostException {
        String EMPTY_ROOM_IMAGE = "EmptyRoomImage";
        String EMPTY_PRICE_FIELD = "EmptyPriceField";
        String NOT_SELECTION_DATE = "NotSelectionDate";
        String EMPTY_TITLE_FIELD = "EmptyTitleField";
        String NETWORK_ERROR = "NetworkError";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef
    public @interface Chatting {
        String NO_CHAT_ROOM = "noChatRoom";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({USER_DATA, CHATTING_DATA})
    public @interface ParcelableData {
        String USER_DATA = "userData";
        String CHATTING_DATA = "chattingData";
    }
}
