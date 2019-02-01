package com.swsnack.catchhouse.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.roomsdata.pojo.Address;
import com.swsnack.catchhouse.databinding.ItemMapAddressBinding;

public class AddressBindingAdapter extends com.swsnack.catchhouse.adapters.BaseRecyclerViewAdapter<Address, AddressBindingAdapter.AddressViewHolder> {

    public AddressBindingAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(AddressViewHolder holder, int position) {
        Address address = getItem(position);
        holder.binding.setAddress(address);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_address, parent, false);
        return new AddressViewHolder(view);
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {

        ItemMapAddressBinding binding;

        public AddressViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }


    }
}