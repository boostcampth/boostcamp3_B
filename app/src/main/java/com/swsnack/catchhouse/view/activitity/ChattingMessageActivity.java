package com.swsnack.catchhouse.view.activitity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.chattingadapter.ChattingMessageAdapter;
import com.swsnack.catchhouse.data.chattingdata.model.Chatting;
import com.swsnack.catchhouse.databinding.ActivityChattingMessageBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModelFactory;

import static com.swsnack.catchhouse.Constant.ParcelableData.CHATTING_DATA;
import static com.swsnack.catchhouse.Constant.ParcelableData.USER_DATA;

public class ChattingMessageActivity extends BaseActivity<ActivityChattingMessageBinding> {

    private ChattingViewModel mViewModel;

    @Override
    public void onSuccess(String success) {
        super.onSuccess(success);

        if (success.equals("sendSuccess")) {
            getBinding().etChattingMessageContent.setText("");
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_chatting_message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        if (getIntent().getParcelableExtra(USER_DATA) != null
                && getIntent().getSerializableExtra(CHATTING_DATA) != null) {
            mViewModel.setChattingMessage((Chatting) getIntent().getSerializableExtra(CHATTING_DATA));
            mViewModel.setDestinationUserData(getIntent().getParcelableExtra(USER_DATA));
        }

        mViewModel.getNewMessage();

        ChattingMessageAdapter messageAdapter = new ChattingMessageAdapter(getApplicationContext(), mViewModel);
        getBinding().rvChattingMessage.setAdapter(messageAdapter);
        getBinding().rvChattingMessage.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false));

        getBinding().etChattingMessageContent.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                mViewModel.sendNewMessage(getBinding().etChattingMessageContent.getText().toString());
                getBinding().etChattingMessageContent.setText("");
                return true;
            }
            return false;
        });

    }

    private void init() {
        mViewModel = ViewModelProviders.of(this, new ChattingViewModelFactory(this)).get(ChattingViewModel.class);
        getBinding().setHandler(mViewModel);
        getBinding().setLifecycleOwner(this);
    }

}
