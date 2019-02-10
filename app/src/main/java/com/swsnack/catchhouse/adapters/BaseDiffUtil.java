package com.swsnack.catchhouse.adapters;

import android.support.v7.util.DiffUtil;
import android.util.Log;

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
    public boolean areItemsTheSame(int i, int i1) {
        Log.d("디프", "i : " + i + "i1 : " + i1 + "   " + mOldList.get(i).equals(mNewList.get(i1)));
        return mOldList.get(i).equals(mNewList.get(i1));
    }
}
