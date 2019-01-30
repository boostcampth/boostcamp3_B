package com.swsnack.catchhouse.view;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity {

    private B mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, setLayout());
    }

    protected abstract int setLayout();

    protected B getBinding() {
        return mBinding;
    }

    protected void createViewModel(@NonNull Class viewModelClass) {
        ViewModelProviders.of(this).get(viewModelClass);
    }

    protected void createViewModel(@NonNull Class viewModelClass, @Nullable ViewModelProvider.Factory factory) {
        ViewModelProviders.of(this, factory).get(viewModelClass);
    }

}
