package com.swsnack.catchhouse.util;

import com.swsnack.catchhouse.AppApplication;

public class StringUtil {

    public static String getStringFromResource(int stringResource) {
        return AppApplication.getAppContext().getString(stringResource);
    }
}
