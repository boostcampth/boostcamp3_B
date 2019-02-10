package com.swsnack.catchhouse.view.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.ActivityChattingMessageBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;

public class ChattingMessageActivity extends BaseActivity<ActivityChattingMessageBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_chatting_message;
    }
}
