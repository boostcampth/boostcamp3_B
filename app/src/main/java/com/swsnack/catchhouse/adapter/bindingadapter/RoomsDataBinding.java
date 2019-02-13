package com.swsnack.catchhouse.adapter.bindingadapter;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.swsnack.catchhouse.adapter.AddressBindingAdapter;
import com.swsnack.catchhouse.data.pojo.Address;
import com.swsnack.catchhouse.adapter.slideadapter.ImageSlideAdapter;

import java.util.List;

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
