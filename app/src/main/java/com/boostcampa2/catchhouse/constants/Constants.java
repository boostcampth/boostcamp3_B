package com.boostcampa2.catchhouse.constants;

public class Constants {

    //sign in request code
    public enum SignInRequestCode {
        GOOGLE_SIGN_IN(1000), FACEBOOK_SIGN_IN(1001), E_MAIL_SIGN_IN(10002);

        final private int requestCode;

        SignInRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public int getRequestCode() {
            return this.requestCode;
        }
    }

    public static int GALLERY = 1003;
}
