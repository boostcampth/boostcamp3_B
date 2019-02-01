package com.swsnack.catchhouse.adapters.bindingadapters;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.v4.view.ViewPager;

import com.swsnack.catchhouse.view.adapters.ImageSlideAdapter;

import java.util.ArrayList;

public class RoomsDataBinding {

    @BindingAdapter("items")
    public static void setItems(ViewPager viewPager, ArrayList<Uri> items) {
        ImageSlideAdapter adapter;

        adapter = (ImageSlideAdapter) viewPager.getAdapter();
        adapter.setUri(items);
        adapter.notifyDataSetChanged();
    }
}
