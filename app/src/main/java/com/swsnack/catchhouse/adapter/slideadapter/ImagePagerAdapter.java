package com.swsnack.catchhouse.adapter.slideadapter;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.BaseViewPagerAdapter;
import com.swsnack.catchhouse.databinding.ItemImagePagerBinding;
import com.swsnack.catchhouse.viewmodel.postviewmodel.PostViewModel;

import java.util.List;

public class ImagePagerAdapter extends BaseViewPagerAdapter<String, PostViewModel> {

    public ImagePagerAdapter(List<String> url, PostViewModel postViewModel) {
        super(url, postViewModel);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ItemImagePagerBinding binding = DataBindingUtil.bind(inflater.inflate(R.layout.item_image_pager, container, false));

        try {
            Glide.with(container.getContext())
                    .load(mList.get(position))
                    .into(binding.ivImagePager);

            String text = (position + 1) + "/" + getCount() + "";
            binding.tvImagePagerNumber.setText(text);

            container.addView(binding.getRoot());
            return binding.getRoot();
        } catch (Exception e) {
            Log.e("Test", "error", e);
            return null;
        }

    }
}
