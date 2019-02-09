package com.swsnack.catchhouse.adapters.bindingadapters;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.swsnack.catchhouse.adapters.AddressBindingAdapter;
import com.swsnack.catchhouse.data.roomsdata.pojo.Address;
import com.swsnack.catchhouse.view.adapters.ImageSlideAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomsDataBinding {

    @BindingAdapter("adapter")
    public static void setItems(ViewPager viewPager, List<Uri> items) {
        ImageSlideAdapter adapter = (ImageSlideAdapter) viewPager.getAdapter();

        if (adapter != null && items != null) {
            adapter.setItem(items);
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("adapter")
    public static void setRecyclerItem(RecyclerView recyclerview, List<Address> items) {
        AddressBindingAdapter adapter = (AddressBindingAdapter) recyclerview.getAdapter();

        if (adapter != null && items != null) {
            adapter.updateItems(items);
        }
    }

    @BindingAdapter("address")
    public static void setAddressText(EditText editText, Address address) {

        if(editText != null && address != null) {
            editText.setText(address.getAddress());
        }
    }
}
