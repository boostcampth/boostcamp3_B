package com.swsnack.catchhouse.view.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.ActivityChattingMessageBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;

public class ChattingMessageActivity extends BaseActivity<ActivityChattingMessageBinding> {

    private ChattingViewModel mViewModel;

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
        mViewModel = ViewModelProviders.of(this).get(ChattingViewModel.class);
    }
}
