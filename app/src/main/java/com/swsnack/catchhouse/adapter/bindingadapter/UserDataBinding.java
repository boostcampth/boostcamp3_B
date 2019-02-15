package com.swsnack.catchhouse.adapter.bindingadapter;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.adapter.roomadapter.FavoriteRoomAdapter;
import com.swsnack.catchhouse.data.entity.RoomEntity;

import java.util.List;

public class UserDataBinding {

    @BindingAdapter({"setProfile"})
    public static void setProfile(ImageView imageView, Bitmap profileBitmap) {
        Glide.with(imageView).load(profileBitmap).apply(RequestOptions.centerCropTransform()).into(imageView);
    }

    @BindingAdapter({"setFavoriteRoom"})
    public static void setFavoriteRoom(RecyclerView recyclerView, List<RoomEntity> roomList) {
        if(roomList == null) {
            return;
        }

        FavoriteRoomAdapter favoriteRoomAdapter = (FavoriteRoomAdapter) recyclerView.getAdapter();
        favoriteRoomAdapter.setList(roomList);
    }

    @BindingAdapter({"setRoomImage"})
    public static void setRoomImage(ImageView imageView, List<String> uriList) {
        if(uriList == null) {
            return;
        }

        Glide.with(AppApplication.getAppContext())
                .load(Uri.parse(uriList.get(0)))
                .into(imageView);
    }
}
