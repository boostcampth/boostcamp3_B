package com.swsnack.catchhouse.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.model.RoomCard;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.databinding.ItemMapAddressBinding;
import com.swsnack.catchhouse.databinding.ItemMapRoomBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class RoomCardListAdapter extends BaseRecyclerViewAdapter<Room, RoomCardListAdapter.RoomCardViewHolder> {

    public RoomCardListAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(RoomCardViewHolder holder, int position) {
        //RoomCard roomCard = getItem(position);
        Room room = getItem(position);
        holder.binding.setRoomCard(room);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_room, parent, false);
        return new RoomCardViewHolder(view);
    }

    public class RoomCardViewHolder extends RecyclerView.ViewHolder {
        ItemMapRoomBinding binding;

        public RoomCardViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            Log.v("csh"," RoomCardViewHolder 바인딩 설정 완료");
        }


    }
}