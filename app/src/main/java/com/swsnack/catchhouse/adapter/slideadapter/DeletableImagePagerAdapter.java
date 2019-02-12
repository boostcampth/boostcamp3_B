package com.swsnack.catchhouse.adapter.slideadapter;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.BaseViewPagerAdapter;
import com.swsnack.catchhouse.databinding.ItemDeletableImagePagerBinding;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;

import java.util.List;

public class DeletableImagePagerAdapter extends BaseViewPagerAdapter<Uri, RoomsViewModel> {

    public DeletableImagePagerAdapter(List<Uri> uris, RoomsViewModel roomsViewModel){
        super(uris, roomsViewModel);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ItemDeletableImagePagerBinding binding =
                DataBindingUtil.bind(inflater.inflate(R.layout.item_deletable_image_pager, container, false));

        if(binding != null) {
            binding.ivVpImage.setOnClickListener(__ ->
                    mViewModel.onClickDeleteButton(position)
            );

            try {
                Glide.with(container.getContext())
                        .load(mList.get(position))
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
    public int getItemPosition(@NonNull Object object) {
        super.getItemPosition(object);
        return POSITION_NONE;
    }
}