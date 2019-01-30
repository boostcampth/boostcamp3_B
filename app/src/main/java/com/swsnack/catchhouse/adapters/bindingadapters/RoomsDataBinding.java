package com.swsnack.catchhouse.adapters.bindingadapters;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.v4.view.ViewPager;

import com.swsnack.catchhouse.view.Adapter;

import java.util.ArrayList;

public class RoomsDataBinding {

    @BindingAdapter("app:items")
    public static void setItems(ViewPager vp, ArrayList<Uri> items) {
        Adapter adapter;

        if (vp.getAdapter() == null) {
            adapter = new Adapter();
            vp.setAdapter(adapter);
        } else {
            adapter = (Adapter) vp.getAdapter();
        }

        if (items != null) {
            adapter.setUriArray(items);
            adapter.notifyDataSetChanged();
        }
    }
}
