package com.swsnack.catchhouse.adapter.bindingadapter;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.swsnack.catchhouse.adapter.AddressListAdapter;
import com.swsnack.catchhouse.data.model.Address;

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
