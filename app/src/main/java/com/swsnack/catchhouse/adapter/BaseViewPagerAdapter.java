package com.swsnack.catchhouse.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public abstract class BaseViewPagerAdapter<T, VM> extends PagerAdapter {

    protected VM mViewModel;
    protected List<T> mList;

    public BaseViewPagerAdapter(VM viewModel) {
        mViewModel = viewModel;
    }

    public void setItem(List<T> items) {
        mList = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
        container.invalidate();
    }
}
