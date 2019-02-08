package com.swsnack.catchhouse.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.swsnack.catchhouse.data.chattingdata.pojo.Chatting;
import com.swsnack.catchhouse.databinding.ItemChattingListBinding;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;

import java.util.List;

public class ChattingListAdapter extends BaseRecyclerViewAdapter<Chatting, ChattingListAdapter.ChattingItemHolder> {

    private ChattingViewModel mChattingViewModel;

    public ChattingListAdapter(Context context, ChattingViewModel chattingViewModel) {
        super(context);
        this.mChattingViewModel = chattingViewModel;
    }

    @Override
    public void onBindView(ChattingItemHolder holder, int position) {
        holder.mBinding.setLifecycleOwner(holder);
        holder.mBinding.setHandler(mChattingViewModel);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChattingItemHolder(ItemChattingListBinding.inflate(LayoutInflater.from(viewGroup.getContext())));
    }

    class ChattingItemHolder extends RecyclerView.ViewHolder {

        ItemChattingListBinding mBinding;

        ChattingItemHolder(@NonNull ItemChattingListBinding binding) {
            super(binding.getRoot());
        }
    }

    public void setItem(List<Chatting> newChattingList) {

    }
}
