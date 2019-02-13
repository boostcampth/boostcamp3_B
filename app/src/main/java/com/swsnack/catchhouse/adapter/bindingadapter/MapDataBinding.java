package com.swsnack.catchhouse.adapter.bindingadapter;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.swsnack.catchhouse.adapter.AddressListAdapter;
import com.swsnack.catchhouse.data.pojo.Address;

import java.util.List;
@InverseBindingMethods({
        @InverseBindingMethod(type = SeekBar.class, attribute = "android:progress"),
})
public class MapDataBinding {

    @BindingAdapter({"setAddressList"})
    public static void setAddressList(RecyclerView recyclerView, List<Address> addressList) {
        AddressListAdapter addressListAdapter = (AddressListAdapter)recyclerView.getAdapter();
        if(addressList!=null) {
            for(int i=0; i<addressList.size(); i++) {
                Log.v("csh",i+":"+addressList.get(i).getAddress());
            }
        }
        addressListAdapter.updateItems(addressList);
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, Boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("android:checked")
    public static void setChecked(CompoundButton checkableView, Boolean isChecked) {
        checkableView.setChecked(isChecked != null ? isChecked : false);
    }

    @BindingAdapter("android:progress")
    public static void setProgress(SeekBar view, int progress) {
        if (progress != view.getProgress()) {
            view.setProgress(progress);
        }
    }



}
