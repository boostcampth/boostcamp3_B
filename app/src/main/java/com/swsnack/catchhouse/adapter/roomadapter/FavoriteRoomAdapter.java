package com.swsnack.catchhouse.adapter.roomadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.BaseRecyclerViewAdapter;
import com.swsnack.catchhouse.data.entity.RoomEntity;
import com.swsnack.catchhouse.databinding.ItemMyFavoriteRoomBinding;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteRoomAdapter extends BaseRecyclerViewAdapter<RoomEntity, FavoriteRoomItemHolder> {

    private UserViewModel mUserViewModel;

    public FavoriteRoomAdapter(Context context, UserViewModel userViewModel) {
        super(context);
        this.mUserViewModel = userViewModel;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ((FavoriteRoomItemHolder) holder).onAttachHolder();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((FavoriteRoomItemHolder) holder).onDetachHolder();
    }

    @Override
    public void onBindView(FavoriteRoomItemHolder holder, int position) {
        holder.getBinding().setData(arrayList.get(position));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyFavoriteRoomBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_my_favorite_room, parent, false);

        FavoriteRoomItemHolder holder = new FavoriteRoomItemHolder(binding);
        binding.setLifecycleOwner(holder);
        binding.setHandler(mUserViewModel);
        return holder;
    }

    public void setList(List<RoomEntity> newRoomList) {
        if (arrayList == null) {
            arrayList = newRoomList;
            notifyDataSetChanged();
            return;
        }

        RoomListDiffUtil diffCallback = new RoomListDiffUtil(this.arrayList, newRoomList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback);
        this.arrayList = newRoomList;
        result.dispatchUpdatesTo(this);
    }
}
