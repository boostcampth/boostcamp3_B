package com.swsnack.catchhouse.adapter.bindingadapter;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.adapter.chattingadapter.ChattingListAdapter;
import com.swsnack.catchhouse.adapter.chattingadapter.ChattingMessageAdapter;
import com.swsnack.catchhouse.data.chattingdata.model.Chatting;
import com.swsnack.catchhouse.data.chattingdata.model.Message;
import com.swsnack.catchhouse.data.userdata.model.User;
import com.swsnack.catchhouse.util.DataConverter;

import java.util.List;

public class ChattingDataBinding {

    @BindingAdapter({"setChattingList"})
    public static void setList(RecyclerView recyclerView, List<Chatting> chattingList) {
        ChattingListAdapter chattingListAdapter = (ChattingListAdapter) recyclerView.getAdapter();
        List<Chatting> orderedList = DataConverter.reOrderedListByTimeStamp(chattingList);
        chattingListAdapter.setList(orderedList);
    }

    @BindingAdapter({"setChattingUserProfile"})
    public static void setProfile(ImageView imageView, User user) {
        if (user == null) {
            return;
        }

        String uri = user.getProfile();
        if (uri != null) {
            Glide.with(AppApplication.getAppContext()).load(Uri.parse(uri)).apply(new RequestOptions().circleCrop()).into(imageView);
        }
    }

    @BindingAdapter({"setChattingMessage"})
    public static void setMessage(RecyclerView recyclerView, List<Message> chattingMessages) {
        if (chattingMessages == null || chattingMessages.size() == 0) {
            return;
        }

        ChattingMessageAdapter chattingMessageAdapter = (ChattingMessageAdapter) recyclerView.getAdapter();
        chattingMessageAdapter.setList(chattingMessages);
        recyclerView.scrollToPosition(chattingMessages.size() - 1);
    }

    @BindingAdapter({"setUserData"})
    public static void setUserData(RecyclerView recyclerView, User user) {
        if (user == null) {
            return;
        }

        ChattingMessageAdapter chattingMessageAdapter = (ChattingMessageAdapter) recyclerView.getAdapter();
        chattingMessageAdapter.setUserData(user);
    }
}
