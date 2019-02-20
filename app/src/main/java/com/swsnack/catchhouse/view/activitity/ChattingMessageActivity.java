package com.swsnack.catchhouse.view.activitity;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.chattingadapter.ChattingMessageAdapter;
import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.databinding.ActivityChattingMessageBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModelFactory;

import static com.swsnack.catchhouse.Constant.FirebaseKey.UUID;
import static com.swsnack.catchhouse.Constant.ParcelableData.CHATTING_DATA;
import static com.swsnack.catchhouse.Constant.ParcelableData.USER_DATA;
import static com.swsnack.catchhouse.Constant.SuccessKey.SEND_MESSAGE_SUCCESS;

public class ChattingMessageActivity extends BaseActivity<ActivityChattingMessageBinding> {

    private ChattingViewModel mViewModel;

    @Override
    public void onSuccess(String success) {
        super.onSuccess(success);

        if (success.equals(SEND_MESSAGE_SUCCESS)) {
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

        getBinding().tbChatMessage.setNavigationIcon(R.drawable.back_button_primary);
        getBinding().tbChatMessage.setNavigationOnClickListener(__ ->
                finish()
        );

        if (getIntent().getParcelableExtra(USER_DATA) != null
                && getIntent().getSerializableExtra(CHATTING_DATA) != null) {
            mViewModel.setChattingMessage((Chatting) getIntent().getSerializableExtra(CHATTING_DATA));
            mViewModel.setDestinationUserData(getIntent().getParcelableExtra(USER_DATA));
        } else if (getIntent().getStringExtra(UUID) != null) {
            // set dummy data
            String destinationUuid = getIntent().getStringExtra(UUID);
            mViewModel.setDestinationUuid(destinationUuid);
            mViewModel.getStoredMessage(destinationUuid);
        } else {
            throw new RuntimeException("chatting destination user's not exist");
        }

        ChattingMessageAdapter messageAdapter = new ChattingMessageAdapter(getApplicationContext(), mViewModel);
        getBinding().rvChattingMessage.setAdapter(messageAdapter);
        getBinding().rvChattingMessage.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        getBinding().etChattingMessageContent.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (getBinding().etChattingMessageContent.getText().toString().trim().equals("")) {
                    return true;
                }
                mViewModel.sendNewMessage(messageAdapter.getItemCount(), getBinding().etChattingMessageContent.getText().toString());
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.getNewMessage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewModel.cancelChangingMessagesListening();
    }

    private void init() {
        mViewModel = ViewModelProviders.of(this, new ChattingViewModelFactory(this)).get(ChattingViewModel.class);
        getBinding().setHandler(mViewModel);
        getBinding().setLifecycleOwner(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.cancelChangingMessagesListening();
    }
}
