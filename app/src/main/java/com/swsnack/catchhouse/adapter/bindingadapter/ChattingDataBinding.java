package com.swsnack.catchhouse.adapter.bindingadapter;

import androidx.annotation.RequiresApi;
import androidx.databinding.BindingAdapter;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.chattingadapter.ChattingListAdapter;
import com.swsnack.catchhouse.adapter.chattingadapter.ChattingMessageAdapter;
import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.data.model.Message;
import com.swsnack.catchhouse.data.model.User;
import com.swsnack.catchhouse.util.DataConverter;

import java.util.ArrayList;
import java.util.List;

public class ChattingDataBinding {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @BindingAdapter({"setChattingList"})
    public static void setList(RecyclerView recyclerView, List<Chatting> chattingList) {
        ChattingListAdapter chattingListAdapter = (ChattingListAdapter) recyclerView.getAdapter();
        if(chattingListAdapter == null) {
            return;
        }

        if(chattingList == null) {
            chattingListAdapter.setList(new ArrayList<>());
            return;
        }

        List<Chatting> orderedList = DataConverter.sortByTimeStamp(chattingList);
        chattingListAdapter.setList(orderedList);
    }

    @BindingAdapter({"showNoChat"})
    public static void shoNoChatting(TextView textView, List<Chatting> chattingList) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            textView.setVisibility(View.GONE);
            return;
        }
        if(chattingList == null) {
            textView.setVisibility(View.VISIBLE);
            return;
        }
        if(chattingList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            return;
        }
        textView.setVisibility(View.GONE);
    }

    @BindingAdapter({"setChattingUserProfile"})
    public static void setProfile(ImageView imageView, User user) {
        if (user == null) {
            imageView.setImageResource(R.drawable.profile);
            return;
        }

        String uri = user.getProfile();
        if (uri == null) {
            imageView.setImageResource(R.drawable.profile);
            return;
        }
        Glide.with(AppApplication.getAppContext()).load(Uri.parse(uri)).apply(new RequestOptions().circleCrop()).into(imageView);
    }

    @BindingAdapter({"setChattingMessage"})
    public static void setMessage(RecyclerView recyclerView, List<Message> chattingMessages) {
        ChattingMessageAdapter chattingMessageAdapter = (ChattingMessageAdapter) recyclerView.getAdapter();
        if(chattingMessageAdapter == null) {
            return;
        }

        if (chattingMessages == null || chattingMessages.size() == 0) {
            chattingMessageAdapter.setList(new ArrayList<>());
            return;
        }

        chattingMessageAdapter.setList(chattingMessages);
        recyclerView.scrollToPosition(chattingMessages.size() - 1);
    }

    @BindingAdapter({"setUserData"})
    public static void setUserData(RecyclerView recyclerView, User user) {
        ChattingMessageAdapter chattingMessageAdapter = (ChattingMessageAdapter) recyclerView.getAdapter();
        if(chattingMessageAdapter == null) {
            return;
        }
        if (user == null) {
            return;
        }

        chattingMessageAdapter.setUserData(user);
    }
}
