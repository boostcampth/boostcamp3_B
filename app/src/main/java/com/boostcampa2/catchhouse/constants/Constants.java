package com.boostcampa2.catchhouse.constants;

public class Constants {

    //sign in request code
    public enum SignInRequestCode {
        GOOGLE_SIGN_IN(1000), FACEBOOK_SIGN_IN(1001), E_MAIL_SIGN_IN(10002);

        private final int requestCode;

        SignInRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public int getRequestCode() {
            return this.requestCode;
        }
    }

    public static final int GALLERY = 1003;

    //gender
    public static final String MALE = "male";
    public static final String FEMALE = "female";

    //facebook readPermissions
    public static final String E_MAIL = "email";
    public static final String PUBLIC_PROFILE = "public_profile";

    //success code
    public static final String SIGN_IN_SUCCESS = "signInSuccess";
}
