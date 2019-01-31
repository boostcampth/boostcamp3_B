package com.swsnack.catchhouse.view.adapters;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.databinding.SliderBinding;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

public class ImageSlideAdapter extends PagerAdapter {

    ArrayList<Uri> mUri;
    private LayoutInflater inflater;
    SliderBinding mBinding;
    RoomsViewModel mViewModel;

    public ImageSlideAdapter(RoomsViewModel viewModel) {
        mViewModel = viewModel;
    }

    public void setUri(ArrayList<Uri> uri) {
        mUri = uri;
    }

    public ArrayList<Uri> getUri() {
        return mUri;
    }

    @Override
    public int getCount() {
        if (mUri == null) {
            return 0;
        } else {
            return mUri.size();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = LayoutInflater.from(container.getContext());
        mBinding = DataBindingUtil.bind(inflater.inflate(R.layout.slider, container, false));

        mBinding.imageView2.setOnClickListener(__ ->
                mViewModel.onClickDeleteButton(position)
        );

        try {
            Glide.with(container.getContext())
                    .load(mUri.get(position))
                    .apply(new RequestOptions().override(340, 324))
                    .into(mBinding.imageView);
            String text = (position + 1) + "/" + Integer.toString(getCount());
            mBinding.textView.setText(text);
            container.addView(mBinding.getRoot());

        } catch (Exception e) {
        }

        return mBinding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        container.invalidate();
    }

    @Override
    public int getItemPosition(@android.support.annotation.NonNull Object object) {
        super.getItemPosition(object);
        return POSITION_NONE;
    }
}