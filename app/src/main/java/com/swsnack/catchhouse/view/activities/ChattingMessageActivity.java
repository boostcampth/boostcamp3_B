package com.swsnack.catchhouse.view.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.ActivityChattingMessageBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModelFactory;

public class ChattingMessageActivity extends BaseActivity<ActivityChattingMessageBinding> implements ViewModelListener {

    private ChattingViewModel mViewModel;

    @Override
    public void onError(String errorMessage) {

    }

    @Override
    public void onSuccess(String success) {

    }

    @Override
    public void isWorking() {

    }

    @Override
    public void isFinished() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_chatting_message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        mViewModel = ViewModelProviders.of(this, new ChattingViewModelFactory(this)).get(ChattingViewModel.class);
    }
}
