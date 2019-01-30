package com.swsnack.catchhouse.viewmodel.userviewmodel;

public class InSufficientException extends Exception {

    private String mReason;

    InSufficientException(String reason) {
        super("reason : " + reason);
        mReason = reason;
    }

    public String getReason() {
        return mReason;
    }

}
