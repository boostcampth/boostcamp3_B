package com.swsnack.catchhouse.adapter.slideadapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.BaseViewPagerAdapter;
import com.swsnack.catchhouse.databinding.ItemZoomableImagePagerBinding;
import com.swsnack.catchhouse.viewmodel.postviewmodel.PostViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

public class ZoomableImagePagerAdapter extends BaseViewPagerAdapter<String, PostViewModel> {

    Application mApplication;

    public ZoomableImagePagerAdapter(List<String> imageList, PostViewModel postViewModel) {
        super(imageList, postViewModel);
        mApplication = AppApplication.getAppContext();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ItemZoomableImagePagerBinding binding =
                DataBindingUtil.bind(inflater.inflate(R.layout.item_zoomable_image_pager,
                        container, false));

        if(binding == null) {
            return container;
        }

        Glide.with(mApplication)
                .load(mList.get(position))
                .into(binding.pvItem);
        container.addView(binding.getRoot());

        return binding.getRoot();
    }


}
