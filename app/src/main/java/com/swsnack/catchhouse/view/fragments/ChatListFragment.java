package com.swsnack.catchhouse.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapters.chattingadapter.ChattingListAdapter;
import com.swsnack.catchhouse.databinding.FragmentChatListBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.view.activities.BottomNavActivity;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;

import java.util.ArrayList;
import java.util.Objects;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof BottomNavActivity) {
            ((BottomNavActivity) Objects.requireNonNull(getActivity())).setViewPagerListener(this::getChattingList);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().setHandler(getViewModel());
        getBinding().setLifecycleOwner(this);

        ChattingListAdapter chattingListAdapter = new ChattingListAdapter(getContext(), getViewModel());
        getBinding().rvChatList.setAdapter(chattingListAdapter);
        getBinding().rvChatList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));

    }

    public void getChattingList() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getBinding().tvChatListNotSigned.setVisibility(View.VISIBLE);
        } else {
            getBinding().tvChatListNotSigned.setVisibility(View.GONE);
        }
        /* set dummy data*/
        getViewModel().setChattingList(new ArrayList<>());
        getViewModel().getChattingRoomList();
        getViewModel().setChattingList();
    }

}