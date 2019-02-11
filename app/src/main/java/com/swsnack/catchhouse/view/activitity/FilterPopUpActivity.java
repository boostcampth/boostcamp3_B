package com.swsnack.catchhouse.view.activities;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.constants.Constants;
import com.swsnack.catchhouse.databinding.ActivityFilterPopUpBinding;
import com.swsnack.catchhouse.view.BaseActivity;

public class FilterPopUpActivity extends BaseActivity<ActivityFilterPopUpBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_filter_pop_up;
    }

    // FIXME 아무것도 하는일이 없는 불필요한 코드입니다.
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


        // TODO: 2019-02-02 팝업에 대한 result 처리 추가 필요
        Intent intent = new Intent();
        intent.putExtra("data1","dd");
        setResult(Constants.FILTER, intent);
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
