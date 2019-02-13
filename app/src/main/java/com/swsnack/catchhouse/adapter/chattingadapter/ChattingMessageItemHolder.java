package com.swsnack.catchhouse.adapter.chattingadapter;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swsnack.catchhouse.databinding.ItemChattingMessageBinding;

public class ChattingMessageItemHolder extends RecyclerView.ViewHolder implements LifecycleOwner {

    private ItemChattingMessageBinding mBinding;
    private LifecycleRegistry mLifeCycle = new LifecycleRegistry(this);

    ChattingMessageItemHolder(@NonNull ItemChattingMessageBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
        this.mLifeCycle.markState(Lifecycle.State.INITIALIZED);
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
        return this.mLifeCycle;
    }

    @NonNull
    public ItemChattingMessageBinding getBinding() {
        return this.mBinding;
    }
}
