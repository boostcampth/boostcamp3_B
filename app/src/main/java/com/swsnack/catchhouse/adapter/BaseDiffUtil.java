package com.swsnack.catchhouse.adapter;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public abstract class BaseDiffUtil<T> extends DiffUtil.Callback {

    protected final List<T> mOldList;
    protected final List<T> mNewList;

    public BaseDiffUtil(List<T> mOldList, List<T> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldListIndex, int newListIndex) {
        return mOldList.get(oldListIndex).equals(mNewList.get(newListIndex));
    }
}
