package com.swsnack.catchhouse.adapter.slideadapter;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
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
        ItemImagePagerBinding binding =
                DataBindingUtil.bind(inflater.inflate(R.layout.item_image_pager,
                        container, false));

        Glide.with(container.getContext())
                .load(mList.get(position))
                .thumbnail(0.2f)
                .listener(new RequestListener<Drawable>() {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                  binding.ivImagePagerRenew.setVisibility(View.VISIBLE);
                                  binding.ivImagePagerRenew.setOnClickListener(__ ->
                                          Glide.with(container.getContext())
                                                  .load(mList.get(position))
                                                  .thumbnail(0.2f)
                                                  .apply(new RequestOptions().override(1000, 600).centerCrop())
                                                  .listener(this)
                                                  .into(binding.ivImagePager)
                                  );
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                  if (binding.ivImagePagerRenew.getVisibility() == View.VISIBLE) {
                                      binding.ivImagePagerRenew.setVisibility(View.GONE);
                                  }
                                  return false;
                              }
                          }
                )
                .into(binding.ivImagePager);

        container.addView(binding.getRoot());
        return binding.getRoot();

    }
}
