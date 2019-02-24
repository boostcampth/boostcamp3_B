package com.swsnack.catchhouse.adapter.chattingadapter;

import com.swsnack.catchhouse.databinding.ItemChattingListBinding;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;

public class ChattingListItemHolder extends RecyclerView.ViewHolder implements LifecycleOwner {

    private ItemChattingListBinding mBinding;
    private LifecycleRegistry mLifeCycle = new LifecycleRegistry(this);

    ChattingListItemHolder(@NonNull ItemChattingListBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
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

    @NonNull
    public ItemChattingListBinding getBinding() {
        return mBinding;
    }
}