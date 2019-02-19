package com.swsnack.catchhouse.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.slideadapter.ZoomableImagePagerAdapter;
import com.swsnack.catchhouse.databinding.FragmentPhotoViewBinding;
import com.swsnack.catchhouse.view.BaseFragment;
import com.swsnack.catchhouse.viewmodel.postviewmodel.PostViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PhotoViewFragment extends BaseFragment<FragmentPhotoViewBinding, PostViewModel> {

    @Override
    protected int getLayout() {
        return R.layout.fragment_photo_view;
    }

    @Override
    protected Class<PostViewModel> getViewModelClass() {
        return PostViewModel.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

////        getBinding().vpPhoto.setAdapter(
////                new ZoomableImagePagerAdapter(getViewModel().mImageList.getValue(), getViewModel()));
//        getBinding().tabPhoto.setupWithViewPager(getBinding().vpPhoto, true);

    }
}
