package com.swsnack.catchhouse.adapter.roomadapter;

import com.swsnack.catchhouse.databinding.ItemMyFavoriteRoomBinding;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;

 class FavoriteRoomItemHolder extends RecyclerView.ViewHolder implements LifecycleOwner {

    private ItemMyFavoriteRoomBinding mBinding;
    private LifecycleRegistry mLifeCycle = new LifecycleRegistry(this);

    FavoriteRoomItemHolder(@NonNull ItemMyFavoriteRoomBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
        this.mLifeCycle.setCurrentState(Lifecycle.State.INITIALIZED);
    }

    void onAttachHolder() {
        mLifeCycle.setCurrentState(Lifecycle.State.STARTED);
    }

    void onDetachHolder() {
        mLifeCycle.setCurrentState(Lifecycle.State.DESTROYED);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return this.mLifeCycle;
    }

    @NonNull
    public ItemMyFavoriteRoomBinding getBinding() {
        return this.mBinding;
    }
}
