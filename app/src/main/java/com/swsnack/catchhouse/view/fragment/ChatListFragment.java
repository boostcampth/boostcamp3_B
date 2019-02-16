package com.swsnack.catchhouse.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.chattingadapter.ChattingListAdapter;
import com.swsnack.catchhouse.adapter.chattingadapter.ChattingListItemHolder;
import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.data.model.User;
import com.swsnack.catchhouse.databinding.FragmentChatListBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.activitity.BottomNavActivity;
import com.swsnack.catchhouse.view.activitity.ChattingMessageActivity;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;

import java.util.ArrayList;
import java.util.Objects;

import static com.swsnack.catchhouse.Constant.FirebaseKey.UUID;
import static com.swsnack.catchhouse.Constant.ParcelableData.CHATTING_DATA;
import static com.swsnack.catchhouse.Constant.ParcelableData.USER_DATA;

public class ChatListFragment extends BaseFragment<FragmentChatListBinding, ChattingViewModel> {

    @Override
    protected int getLayout() {
        return R.layout.fragment_chat_list;
    }

    @Override
    protected Class<ChattingViewModel> getViewModelClass() {
        return ChattingViewModel.class;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavActivity) {
            ((BottomNavActivity) Objects.requireNonNull(getActivity())).setViewPagerListener(this::getChattingList);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().setHandler(getViewModel());

        ChattingListAdapter chattingListAdapter = new ChattingListAdapter(getContext(), getViewModel());
        getBinding().rvChatList.setAdapter(chattingListAdapter);
        getBinding().rvChatList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));

        chattingListAdapter.setOnItemClickListener((v, position) -> {
            Chatting chatting = chattingListAdapter.getItem(position);
            User user = ((ChattingListItemHolder) v).getBinding().getUserData();

            startActivity(
                    new Intent(getContext(),
                            ChattingMessageActivity.class)
                            .putExtra(CHATTING_DATA, chatting)
                            .putExtra(USER_DATA, user));
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getViewModel().getChattingRoomList();
    }

    @Override
    public void onStop() {
        super.onStop();
        getViewModel().cancelChattingListChangingListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.cancelChattingListChangingListening();
    }

    private void getChattingList() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getBinding().tvChatListNotSigned.setVisibility(View.VISIBLE);
            getViewModel().setChattingList(new ArrayList<>());
        } else {
            getViewModel().cancelChattingListChangingListening();
            getViewModel().getChattingRoomList();
            getBinding().tvChatListNotSigned.setVisibility(View.GONE);
        }
    }
}