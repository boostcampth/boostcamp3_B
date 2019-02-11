package com.swsnack.catchhouse.view.adapters;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.ItemViewpagerBinding;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;

import java.util.List;

public class ImageSlideAdapter extends PagerAdapter {

    private List<Uri> mImageUriList;
    private RoomsViewModel mViewModel;

    public ImageSlideAdapter(RoomsViewModel viewModel, List<Uri> uriList) {
        mViewModel = viewModel;
        mImageUriList = uriList;
    }

    public void setItem(List<Uri> item) {
        mImageUriList = item;
    }

    @Override
    public int getCount() {
        return mImageUriList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ItemViewpagerBinding binding =
                DataBindingUtil.bind(inflater.inflate(R.layout.item_viewpager, container, false));

        if(binding != null) {
            binding.ivVpDelete.setOnClickListener(__ ->
                    mViewModel.onClickDeleteButton(position)
            );

            try {
                Glide.with(container.getContext())
                        .load(mImageUriList.get(position))
                        .apply(new RequestOptions().override(340, 324))
                        .into(binding.ivVpImage);

                String text = (position + 1) + "/" + getCount() + "";
                binding.tvVpNumber.setText(text);

                container.addView(binding.getRoot());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return binding.getRoot();
        }
        return container;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
        container.invalidate();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        super.getItemPosition(object);
        return POSITION_NONE;
    }
}