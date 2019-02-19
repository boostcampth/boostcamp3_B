package com.swsnack.catchhouse.adapter.slideadapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.BaseViewPagerAdapter;
import com.swsnack.catchhouse.databinding.ItemDeletableImagePagerBinding;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

public class DeletableImagePagerAdapter extends BaseViewPagerAdapter<Uri, RoomsViewModel> {

    public DeletableImagePagerAdapter(List<Uri> uris, RoomsViewModel roomsViewModel) {
        super(roomsViewModel);
        mList = uris;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ItemDeletableImagePagerBinding binding =
                DataBindingUtil.bind(inflater.inflate(R.layout.item_deletable_image_pager, container, false));

        if (binding != null) {
            binding.ivVpImage.setColorFilter(Color.parseColor("#DCDCDC"), PorterDuff.Mode.MULTIPLY);

            binding.ivVpDelete.setOnClickListener(__ ->
                    mViewModel.onClickDeleteButton(position)
            );

            try {
                Glide.with(container.getContext())
                        .load(mList.get(position))
                        .apply(new RequestOptions().override(1000, 600).centerCrop())
                        .into(binding.ivVpImage);

                container.addView(binding.getRoot());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return binding.getRoot();
        }
        return container;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        super.getItemPosition(object);
        return POSITION_NONE;
    }
}