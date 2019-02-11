package com.swsnack.catchhouse.adapter.chattingadapter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.swsnack.catchhouse.databinding.ItemChattingListBinding;

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