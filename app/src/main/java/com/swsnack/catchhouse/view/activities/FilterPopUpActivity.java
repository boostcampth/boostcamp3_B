package com.swsnack.catchhouse.view.activities;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.ActivityFilterPopUpBinding;
import com.swsnack.catchhouse.view.BaseActivity;

public class FilterPopUpActivity extends BaseActivity<ActivityFilterPopUpBinding> {

    @Override
    protected int setLayout() {
        return R.layout.activity_filter_pop_up;
    }

    @Override
    protected ActivityFilterPopUpBinding getBinding() {
        return super.getBinding();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setFinishOnTouchOutside(false);
        Intent intent = new Intent();
        intent.putExtra("data1","dd");
        setResult(1000, intent);
        Log.d("csh","onStop");
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void createViewModel(@NonNull Class viewModelClass) {
        super.createViewModel(viewModelClass);
    }

    @Override
    protected void createViewModel(@NonNull Class viewModelClass, @Nullable ViewModelProvider.Factory factory) {
        super.createViewModel(viewModelClass, factory);
    }

}
