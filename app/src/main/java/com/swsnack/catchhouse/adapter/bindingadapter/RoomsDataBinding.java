package com.swsnack.catchhouse.adapter.bindingadapter;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.swsnack.catchhouse.adapter.AddressListAdapter;
import com.swsnack.catchhouse.adapter.slideadapter.ImagePagerAdapter;
import com.swsnack.catchhouse.adapter.slideadapter.DeletableImagePagerAdapter;
import com.swsnack.catchhouse.data.pojo.Address;

import java.util.List;

public class RoomsDataBinding {

    @BindingAdapter("adapter")
    public static void setDeletableImageItems(ViewPager viewPager, List<Uri> items) {
        DeletableImagePagerAdapter adapter = (DeletableImagePagerAdapter) viewPager.getAdapter();

        if (adapter != null && items != null) {
            adapter.setItem(items);
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("adapter")
    public static void setImageItems(ViewPager viewPager, List<String> items) {
        ImagePagerAdapter adapter = (ImagePagerAdapter) viewPager.getAdapter();

        if (adapter != null && items != null) {
            adapter.setItem(items);
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("adapter")
    public static void setRecyclerItem(RecyclerView recyclerview, List<Address> items) {
        AddressListAdapter adapter = (AddressListAdapter) recyclerview.getAdapter();

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
