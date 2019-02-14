package com.swsnack.catchhouse.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.pojo.Address;
import com.swsnack.catchhouse.databinding.ItemMapAddressBinding;

public class AddressListAdapter extends BaseRecyclerViewAdapter<Address, AddressListAdapter.AddressViewHolder> {

    public AddressListAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(AddressViewHolder holder, int position) {
        Address address = getItem(position);
        holder.binding.setAddress(address);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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