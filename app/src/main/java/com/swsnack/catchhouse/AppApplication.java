package com.swsnack.catchhouse;

import android.content.Context;

import com.facebook.stetho.Stetho;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

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
        Stetho.initializeWithDefaults(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
