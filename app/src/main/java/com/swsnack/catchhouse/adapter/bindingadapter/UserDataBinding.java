package com.swsnack.catchhouse.adapter.bindingadapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.roomadapter.RoomListAdapter;
import com.swsnack.catchhouse.data.model.Room;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class UserDataBinding {

    @BindingAdapter({"setProfile"})
    public static void setProfile(ImageView imageView, Bitmap profileBitmap) {
        if (profileBitmap == null) {
            imageView.setImageResource(R.drawable.profile);
            return;
        }
        Glide.with(imageView).load(profileBitmap).apply(RequestOptions.centerCropTransform()).into(imageView);
    }

    @BindingAdapter({"setFavoriteRoom"})
    public static void setFavoriteRoom(RecyclerView recyclerView, List<Room> roomEntityList) {
        RoomListAdapter roomListAdapter = (RoomListAdapter) recyclerView.getAdapter();
        if (roomListAdapter == null) {
            return;
        }

        if (roomEntityList == null) {
            roomListAdapter.setList(new ArrayList<>());
            return;
        }

        List<Room> roomList = new ArrayList<>();
        for (Room room : roomEntityList) {
            roomList.add(room);
        }
        roomListAdapter.setList(roomList);
    }

    @BindingAdapter({"setRoomImage"})
    public static void setRoomImage(ImageView imageView, List<String> uriList) {
        if (uriList == null) {
            return;
        }

        Glide.with(AppApplication.getAppContext())
                .load(Uri.parse(uriList.get(0)))
                .apply(new RequestOptions().override(150, 150).centerCrop())
                .into(imageView);
    }

    @BindingAdapter({"setRecentRoom"})
    public static void setRecentRoom(RecyclerView recyclerView, List<Room> roomList) {
        RoomListAdapter roomListAdapter = (RoomListAdapter) recyclerView.getAdapter();

        if (roomListAdapter == null) {
            return;
        }

        if (roomList == null) {
            roomListAdapter.setList(new ArrayList<>());
            return;
        }

        roomListAdapter.setList(roomList);
        recyclerView.scrollToPosition(0);
    }
}
