package com.swsnack.catchhouse.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.FragmentChatListBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}