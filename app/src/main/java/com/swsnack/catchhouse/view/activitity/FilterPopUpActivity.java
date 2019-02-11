package com.swsnack.catchhouse.view.activitity;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.Constant;
import com.swsnack.catchhouse.databinding.ActivityFilterPopUpBinding;
import com.swsnack.catchhouse.view.BaseActivity;

public class FilterPopUpActivity extends BaseActivity<ActivityFilterPopUpBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_filter_pop_up;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setFinishOnTouchOutside(false);


        // TODO: 2019-02-02 팝업에 대한 result 처리 추가 필요
        Intent intent = new Intent();
        intent.putExtra("data1","dd");
        setResult(Constant.FILTER, intent);
        Log.d("csh","onStop");
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
