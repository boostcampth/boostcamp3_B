package com.swsnack.catchhouse;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

public class AppApplication extends MultiDexApplication {

    private static AppApplication INSTANCE;

    public static AppApplication getAppContext() {
        if (INSTANCE == null) {
            throw new RuntimeException("AppApplication class is not accessible");
        }
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
