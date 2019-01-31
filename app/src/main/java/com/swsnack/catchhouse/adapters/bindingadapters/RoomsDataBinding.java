package com.swsnack.catchhouse.adapters.bindingadapters;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import com.swsnack.catchhouse.view.adapters.ImageSlideAdapter;

import java.util.ArrayList;

public class RoomsDataBinding {

    @BindingAdapter("app:items")
    public static void setItems(ViewPager vp, ArrayList<Uri> items) {
        ImageSlideAdapter adapter;

        if (vp.getAdapter() == null) {
            adapter = new ImageSlideAdapter();
            adapter.setUriArray(items);
            vp.setAdapter(adapter);
        } else {
            adapter = (ImageSlideAdapter) vp.getAdapter();
        }

        if (items != null) {
            adapter.setUriArray(items);
            adapter.notifyDataSetChanged();
        }
    }
}
