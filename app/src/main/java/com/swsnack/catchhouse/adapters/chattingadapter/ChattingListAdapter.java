package com.swsnack.catchhouse.adapters.chattingadapter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.swsnack.catchhouse.adapters.BaseRecyclerViewAdapter;
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
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ((ChattingItemHolder) holder).onAttachHolder();

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((ChattingItemHolder) holder).onDetachHolder();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemChattingListBinding binding = ItemChattingListBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);

        ChattingItemHolder viewHolder = new ChattingItemHolder(binding);

        binding.setLifecycleOwner(viewHolder);
        binding.setHandler(mChattingViewModel);
        mChattingViewModel.getUser(i,
                binding::setData,
                error -> {
                });
        return viewHolder;
    }

    class ChattingItemHolder extends RecyclerView.ViewHolder implements LifecycleOwner {

        LifecycleRegistry mLifeCycle = new LifecycleRegistry(this);

        ChattingItemHolder(@NonNull ItemChattingListBinding binding) {
            super(binding.getRoot());
            mLifeCycle.markState(Lifecycle.State.INITIALIZED);
        }

        void onAttachHolder() {
            mLifeCycle.markState(Lifecycle.State.STARTED);
        }

        void onDetachHolder() {
            mLifeCycle.markState(Lifecycle.State.DESTROYED);
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return mLifeCycle;
        }
    }

    public void setList(List<Chatting> newChattingList) {
        if (arrayList == null) {
            arrayList = newChattingList;
            notifyDataSetChanged();
            return;
        }

        ChattingDiffUtil diffCallback = new ChattingDiffUtil(this.arrayList, newChattingList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback);
        this.arrayList = newChattingList;
        result.dispatchUpdatesTo(this);
    }
}
