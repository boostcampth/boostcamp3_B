package com.swsnack.catchhouse.adapter.bindingadapter;

import androidx.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class UserDataBinding {

    @BindingAdapter({"setProfile"})
    public static void setProfile(ImageView imageView, Bitmap profileBitmap) {
        Glide.with(imageView).load(profileBitmap).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
