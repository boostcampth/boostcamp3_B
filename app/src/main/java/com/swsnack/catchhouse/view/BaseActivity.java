package com.swsnack.catchhouse.view;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;


public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity {

    private B mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayout());
    }

    protected void showSnackMessage(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    protected abstract int getLayout();

    protected B getBinding() {
        return mBinding;
    }

    protected void createViewModel(@NonNull Class viewModelClass) {
        createViewModel(viewModelClass, null);
    }

    protected void createViewModel(@NonNull Class viewModelClass, @Nullable ViewModelProvider.Factory factory) {
        ViewModelProviders.of(this, factory).get(viewModelClass);
    }
}
