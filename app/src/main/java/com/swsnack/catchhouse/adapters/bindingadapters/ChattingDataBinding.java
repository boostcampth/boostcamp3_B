package com.swsnack.catchhouse.adapters.bindingadapters;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.adapters.chattingadapter.ChattingListAdapter;
import com.swsnack.catchhouse.data.chattingdata.model.Chatting;
import com.swsnack.catchhouse.data.userdata.model.User;

import java.util.List;

public class ChattingDataBinding {

    @BindingAdapter({"setList"})
    public static void setList(RecyclerView recyclerView, List<Chatting> chattingList) {
        ChattingListAdapter chattingListAdapter = (ChattingListAdapter) recyclerView.getAdapter();
        chattingListAdapter.setList(chattingList);
    }

    @BindingAdapter({"setChattingListProfile"})
    public static void setProfile(ImageView imageView, User user) {
        if(user == null) {
            return;
        }

        String uri = user.getProfile();
        if (uri != null) {
            Glide.with(AppApplication.getAppContext()).load(Uri.parse(uri)).apply(new RequestOptions().circleCrop()).into(imageView);
        }
    }
}
