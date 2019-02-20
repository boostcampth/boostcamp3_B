package com.swsnack.catchhouse.util;

import android.app.Activity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class KeyboardUtil {
    private static final String TAG = KeyboardUtil.class.getSimpleName();

    public static void keyBoardClose(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e(TAG, "no keyboard");
        }
    }
}
