package com.swsnack.catchhouse.view;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;

import com.swsnack.catchhouse.viewmodel.ViewModelListener;


public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity implements ViewModelListener {

    private B mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayout());
        mBinding.setLifecycleOwner(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    public void onError(String errorMessage) {
        unFreezeUI();
        showSnackMessage(errorMessage);
    }

    @Override
    public void onSuccess(String success) {
        unFreezeUI();
    }

    @Override
    public void isWorking() {
        freezeUI();
    }

    @Override
    public void isFinished() {
        unFreezeUI();
    }

    protected void freezeUI() {
        getBinding().getRoot().setAlpha(0.6f);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void unFreezeUI() {
        getBinding().getRoot().setAlpha(1.0f);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
