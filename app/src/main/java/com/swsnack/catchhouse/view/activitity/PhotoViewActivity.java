package com.swsnack.catchhouse.view.activitity;

import android.content.Intent;
import android.os.Bundle;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.slideadapter.ZoomableImagePagerAdapter;
import com.swsnack.catchhouse.databinding.ActivityPhotoViewBinding;
import com.swsnack.catchhouse.view.BaseActivity;

import java.util.List;

import androidx.annotation.Nullable;

import static com.swsnack.catchhouse.Constant.ParcelableData.IMAGE_LIST_DATA;
import static com.swsnack.catchhouse.Constant.ParcelableData.VIEWPAGER_CURRENT_POSITION;

public class PhotoViewActivity extends BaseActivity<ActivityPhotoViewBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_photo_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBinding().tbPhotoView.setNavigationIcon(R.drawable.back_button_primary);
        getBinding().tbPhotoView.setNavigationOnClickListener(__ ->
                finish()
        );

        Intent intent = getIntent();

        List<String> imageList = intent.getStringArrayListExtra(IMAGE_LIST_DATA);
        int position = intent.getIntExtra(VIEWPAGER_CURRENT_POSITION, 0);

        if (imageList == null) {
            finish();
        } else {
            ZoomableImagePagerAdapter adapter =
                    new ZoomableImagePagerAdapter(imageList, null);
            getBinding().vpPhoto.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            getBinding().vpPhoto.setCurrentItem(position);
            getBinding().tabPhoto.setupWithViewPager(getBinding().vpPhoto, true);
        }
    }
}
