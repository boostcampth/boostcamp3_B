package com.boostcampa2.catchhouse.view;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<B extends ViewDataBinding, V extends ViewModel, F extends ViewModelProvider.Factory> extends AppCompatActivity {

    protected B mBinding;
    protected V mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, setLayout());

        if (mViewModel != null) {
            mViewModel = ViewModelProviders.of(this, setViewModelFactory()).get(setViewModel());
        }
    }

    protected abstract int setLayout();

    protected abstract Class<V> setViewModel();

    @Nullable
    protected abstract F setViewModelFactory();

    protected B getBinding() {
        return mBinding;
    }

    protected V getViewModel() {
        return mViewModel;
    }

}
