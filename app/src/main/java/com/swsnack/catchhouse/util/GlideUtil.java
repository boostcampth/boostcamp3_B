package com.swsnack.catchhouse.util;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.databinding.BindingAdapter;

public class GlideUtil {

    @BindingAdapter("setProfileImageCircle")
    public static void setProfileImage(ImageView imageView, Uri uri) {
        if (uri != null) {
            Glide.with(imageView).load(uri).apply(new RequestOptions().circleCrop()).into(imageView);
        }
    }

    @BindingAdapter("setProfileImageCenterCrop")
    public static void setImage(ImageView imageView, Uri uri) {
        if (uri != null) {
            Glide.with(imageView).load(uri).apply(new RequestOptions().centerCrop()).into(imageView);
        }
    }
}
