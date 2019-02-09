package com.swsnack.catchhouse.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapters.chattingadapter.ChattingListAdapter;
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
        super.onCreateView(inflater, container, savedInstanceState);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChattingListAdapter chattingListAdapter = new ChattingListAdapter(getContext(), getViewModel());
        getBinding().rvChatList.setAdapter(chattingListAdapter);
        getBinding().rvChatList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));

        getViewModel().setChattingList();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}