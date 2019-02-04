package com.swsnack.catchhouse.adapters.bindingadapters;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;

import com.swsnack.catchhouse.adapters.AddressBindingAdapter;
import com.swsnack.catchhouse.data.roomsdata.pojo.Address;
import com.swsnack.catchhouse.view.adapters.ImageSlideAdapter;

import java.util.List;

public class RoomsDataBinding {

    @BindingAdapter("viewpager_item")
    public static void setViewpagerItem(ViewPager viewPager, List<Uri> items) {
        ImageSlideAdapter adapter;

        if (items != null) {
            adapter = (ImageSlideAdapter) viewPager.getAdapter();
            adapter.setUri(items);
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("recycler_item")
    public static void setRecyclerItem(RecyclerView recyclerview, List<Address> items) {
        AddressBindingAdapter adapter;

        if (items != null) {
            adapter = (AddressBindingAdapter) recyclerview.getAdapter();
            adapter.updateItems(items);
        }
    }
}
